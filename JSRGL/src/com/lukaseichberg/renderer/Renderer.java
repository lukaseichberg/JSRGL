package com.lukaseichberg.renderer;

import java.util.ArrayList;
import java.util.List;

import com.lukaseichberg.buffer.FrameBufferInterface;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.maths.Vec4f;
import com.lukaseichberg.sampler.Sampler;
import com.lukaseichberg.shader.ShaderProgram;
import com.lukaseichberg.structs.Vertex;

public class Renderer {
	
	private static ShaderProgram shaderProgram;
	private static FrameBufferInterface<Float> depthBuffer;
	private static FrameBufferInterface<Vec3f> colorBuffer;
	private static float nearPlane = 0.1f;
	private static float farPlane = 100;
	public static boolean perspectiveCorrection = true;
	public static boolean backfaceCulling = true;
	public static boolean frontfaceCulling = false;
	public static boolean depthBuffering = true;
	public static boolean depthMasking = true;
	public static boolean colorBuffering = true;
	public static final int BLEND_OPAQUE = 0;
	public static final int BLEND_ADD = 1;
	public static int blend = BLEND_OPAQUE;
	private static boolean isBack = false;
	private static Sampler<Vec3f> sampler;
//	public static boolean colorBuffering = true;
	
	public static void renderTriangle(Vertex v0, Vertex v1, Vertex v2) {
		Vertex tmp0 = shaderProgram.processVertex(v0);
		Vertex tmp1 = shaderProgram.processVertex(v1);
		Vertex tmp2 = shaderProgram.processVertex(v2);
//		float jitter = 75f;
//		tmp0.pos.x = (float) Math.floor(tmp0.pos.x * jitter) / jitter;
//		tmp0.pos.y = (float) Math.floor(tmp0.pos.y * jitter) / jitter;
//		tmp0.pos.z = (float) Math.floor(tmp0.pos.z * jitter) / jitter;
//		tmp1.pos.x = (float) Math.floor(tmp1.pos.x * jitter) / jitter;
//		tmp1.pos.y = (float) Math.floor(tmp1.pos.y * jitter) / jitter;
//		tmp1.pos.z = (float) Math.floor(tmp1.pos.z * jitter) / jitter;
//		tmp2.pos.x = (float) Math.floor(tmp2.pos.x * jitter) / jitter;
//		tmp2.pos.y = (float) Math.floor(tmp2.pos.y * jitter) / jitter;
//		tmp2.pos.z = (float) Math.floor(tmp2.pos.z * jitter) / jitter;

		Vec3f t0 = new Vec3f(tmp0.pos.x, tmp0.pos.y, tmp0.pos.z);
		Vec3f t1 = new Vec3f(tmp1.pos.x, tmp1.pos.y, tmp1.pos.z);
		Vec3f t2 = new Vec3f(tmp2.pos.x, tmp2.pos.y, tmp2.pos.z);
		
		float dot = 0;
		dot = t0.dot(t1.sub(t0).cross(t2.sub(t0)));

		isBack = (dot > 0);
		boolean cull = (isBack && backfaceCulling) || (!isBack && frontfaceCulling);
		
		if (!cull) {
			List<Vertex> vertices = new ArrayList<>();
			vertices.add(tmp0);
			vertices.add(tmp1);
			vertices.add(tmp2);
			
			vertices = Clipper.clipZ(vertices, nearPlane, farPlane);
			perspectiveDivide(vertices);
			vertices = Clipper.clipScreenSpace(vertices);
			toScreenSpace(vertices);
			
			fillTriangleFan(vertices);
		}
	}
	
	public static void renderLine(Vertex v0, Vertex v1) {
		Vertex tmp0 = shaderProgram.processVertex(v0);
		Vertex tmp1 = shaderProgram.processVertex(v1);

		List<Vertex> vertices = new ArrayList<>();
		vertices.add(tmp0);
		vertices.add(tmp1);
		
		vertices = Clipper.clipZ(vertices, nearPlane, farPlane);
		perspectiveDivide(vertices);
		//vertices = Clipper.clipScreenSpace(vertices);
		toScreenSpace(vertices);
		
		fillLine(vertices);
	}
	
	public static void renderPixel(Vertex v) {
		Vertex tmp = shaderProgram.processVertex(v);

		if (tmp.pos.z >= nearPlane) {
			perspectiveDivide(tmp);
			toScreenSpace(tmp);
			fillPixel(tmp);
		}
	}
	
	private static void fillLine(List<Vertex> vertices) {
		if (vertices.size() == 2) {
			Vertex v0 = vertices.get(0);
			Vertex v1 = vertices.get(1);

			float xSteps = (float) Math.floor(Math.abs(v1.pos.x - v0.pos.x));
			float ySteps = (float) Math.floor(Math.abs(v1.pos.y - v0.pos.y));
			int steps = (int) Math.max(xSteps, ySteps);
			
			for (int i = 0; i < steps; i++) {
				float value = (float) i / (steps + 0.5f);
				Vertex v = v0.lerp(v1, value);
				fillPixel(v);
			}
		}
	}
	
	private static void fillTriangleFan(List<Vertex> vertices) {
		if (vertices.size() > 2) {
			for (int i = 1; i < vertices.size() - 1; i++) {
				fillTriangle(vertices.get(0), vertices.get(i), vertices.get(i + 1));
			}
		}
	}
	
	public float ccw(Vec4f a, Vec4f b, Vec4f c) {
		return (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
	}
	
	private static void fillTriangle(Vertex v0, Vertex v1, Vertex v2) {
		Vertex vt0 = v0.clone();
		Vertex vt1 = v1.clone();
		Vertex vt2 = v2.clone();
		
		if (vt0.pos.y > vt1.pos.y) vt0.swap(vt1);
		if (vt1.pos.y > vt2.pos.y) vt1.swap(vt2);
		if (vt0.pos.y > vt1.pos.y) vt0.swap(vt1);
		
		float value = (vt1.pos.y - vt0.pos.y) / (vt2.pos.y - vt0.pos.y);
		Vertex v = vt0.lerp(vt2, value);
		
		if (vt1.pos.x > v.pos.x) vt1.swap(v);
		
		fillBottomFlatTriangle(vt0, vt1, v);
		fillTopFlatTriangle(vt1, v, vt2);
	}
	
	private static void fillBottomFlatTriangle(Vertex v0, Vertex v1, Vertex v2) {
		int startY = (int) Math.ceil(v0.pos.y - 0.5f);
		int endY = (int) Math.ceil(v2.pos.y - 0.5f);
		
		for (int y = startY; y < endY; y++) {
			float value = (float) (y + 0.5f - v0.pos.y) / (v2.pos.y - v0.pos.y);
			Vertex left = v0.lerp(v1, value);
			Vertex right = v0.lerp(v2, value);
			
			fillHorizontalLine(left, right);
		}
	}
	
	private static void fillTopFlatTriangle(Vertex v0, Vertex v1, Vertex v2) {
		int startY = (int) Math.ceil(v0.pos.y - 0.5f);
		int endY = (int) Math.ceil(v2.pos.y - 0.5f);
		
		for (int y = startY; y < endY; y++) {
			float value = (float) (y + 0.5f - v0.pos.y) / (v2.pos.y - v0.pos.y);
			Vertex left = v0.lerp(v2, value);
			Vertex right = v1.lerp(v2, value);
			
			fillHorizontalLine(left, right);
		}
	}
	
	private static void fillHorizontalLine(Vertex v0, Vertex v1) {
		int startX = (int) Math.ceil(v0.pos.x - 0.5f);
		int endX = (int) Math.ceil(v1.pos.x - 0.5f);
		
		for (int x = startX; x < endX; x++) {
			float value = (float) (x + 0.5f - v0.pos.x) / (v1.pos.x - v0.pos.x);
			Vertex v = v0.lerp(v1, value);
			fillPixel(v);
		}
	}
	
	public static void setSampler(Sampler<Vec3f> sampler) {
		Renderer.sampler = sampler;
	}
	
	private static void fillPixel(Vertex v) {
		int x = (int) v.pos.x;
		int y = (int) v.pos.y;
		
		if (x < 0 || x >= colorBuffer.getWidth() || y < 0 || y >= colorBuffer.getHeight()) return;
		
		// Perspective correction
		float z = 1 / v.pos.z;
		if (perspectiveCorrection) {
			v._mul(z);
		} else {
			v.pos._mul(z);
		}
		v.pos.w = 1 / (v.pos.w * z);
			
		if (v.pos.w > depthBuffer.get(x, y) || !depthMasking) {
			shaderProgram.setIsBack(isBack);
			shaderProgram.setSampler(sampler);
			v = shaderProgram.processFragment(v);
			if (v == null) {
				return;
			}
			
			//if (shaderProgram.isFragmentDiscarded()) return;
			if (depthBuffering) depthBuffer.set(x, y, v.pos.w);
			
			if (colorBuffering) {
				switch(blend) {
					case BLEND_OPAQUE:
						colorBuffer.set(x, y, v.vec3[0]);
						break;
					case BLEND_ADD:
						colorBuffer.set(x, y, colorBuffer.get(x, y).add(v.vec3[0]));
						break;
				}
			}
		}
	}
	
	public static void setDepthBuffer(FrameBufferInterface<Float> depthBuffer) {
		Renderer.depthBuffer = depthBuffer;
	}
	
	public static void setColorBuffer(FrameBufferInterface<Vec3f> colorBuffer) {
		Renderer.colorBuffer = colorBuffer;
	}
	
	public static void toScreenSpace(List<Vertex> vertices) {
		for (Vertex v:vertices) {
			v.pos.x = (v.pos.x * 0.5f + 0.5f) * colorBuffer.getWidth();
			v.pos.y = (v.pos.y * -0.5f + 0.5f) * colorBuffer.getHeight();
		}
	}
	
	private static void toScreenSpace(Vertex v) {
		v.pos.x = (v.pos.x * 0.5f + 0.5f) * colorBuffer.getWidth();
		v.pos.y = (v.pos.y * -0.5f + 0.5f) * colorBuffer.getHeight();
	}
	
	public static void perspectiveDivide(List<Vertex> vertices) {
		for (Vertex v:vertices) {
			float z = 1 / v.pos.z;
			if (perspectiveCorrection) {
				v._mul(z);
				v.pos.z = z;
			} else {
				v.pos._mul(z);
				v.pos.z = z;
			}
		}
	}
	
	private static void perspectiveDivide(Vertex v) {
		float z = 1 / v.pos.z;
		if (perspectiveCorrection) {
			v._mul(z);
			v.pos.z = z;
		} else {
			v.pos._mul(z);
			v.pos.z = z;
		}
	}
	
	public static void bindShaderProgram(ShaderProgram shaderProgram) {
		Renderer.shaderProgram = shaderProgram;
	}
	
	public static void setNearClippingPlane(float near) {
		Renderer.nearPlane = near;
	}
	
	public static void setFarClippingPlane(float far) {
		Renderer.farPlane = far;
	}

}
