package com.lukaseichberg.demo.pcss;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import com.lukaseichberg.buffer.ColorBuffer;
import com.lukaseichberg.buffer.DepthBuffer;
import com.lukaseichberg.camera.Camera;
import com.lukaseichberg.demo.snow.Entity;
import com.lukaseichberg.demo.snow.EntityType;
import com.lukaseichberg.demo.snow.FPSCounter;
import com.lukaseichberg.display.Display;
import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.maths.Vec4f;
import com.lukaseichberg.mesh.Model;
import com.lukaseichberg.mesh.OBJLoader;
import com.lukaseichberg.renderer.Renderer;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.shader.ShaderProgram;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;
import com.lukaseichberg.texture.Texture;

public class Main {

	public static void main(String[] args) {
		int width = 640 / 2;
		int height = 480 / 2;
		Display display = new Display(width, height, 4, "PCSS");
		ColorBuffer colorBuffer = display.getColorBuffer();
		DepthBuffer depthBuffer = display.getDepthBuffer();
		
		Renderer.setColorBuffer(colorBuffer);
		Renderer.setDepthBuffer(depthBuffer);
		
		Vec3f black = new Vec3f(0, 0, 0);
		
		Model cubeModel = OBJLoader.load("res/Cube.obj");
		Model planeModel = OBJLoader.load("res/plane.obj");
		
		// Default Shader Program -----------------------------------------------------
		Shader vertexShader = new Shader() {
			public Vertex main(Vertex in, Uniform uniform) {
				Mat4f projView = uniform.getMat4f("projView");
				Mat4f modelMatrix = uniform.getMat4f("modelMatrix");
				Mat4f shadowProj = uniform.getMat4f("shadowProj");
				
				Vertex out = new Vertex(0, 1, 1);
				Mat4f mat = projView.mul(modelMatrix);
				out.pos = in.pos.mul(mat);
				out.vec3[0] = in.vec4[0].mul(modelMatrix).vec3f();
				out.vec4[0] = in.pos.mul(shadowProj.mul(modelMatrix));
				return out;
			}
		};
		
		Shader fragmentShader = new Shader() {
			public Vertex main(Vertex in, Uniform uniform) {
				Vec3f lightDir = uniform.getVec3f("lightDir");
				Vec3f normal = in.vec3[0].normal();

				float brightness = 0;
				
				Vec4f sPos = in.vec4[0];
				DepthBuffer shadowMap = (DepthBuffer) uniform.getBuffer("shadowMap");

				sPos.x = sPos.x * 0.5f + 0.5f;
				sPos.y = -sPos.y * 0.5f + 0.5f;
				float tx = sPos.x * shadowMap.getWidth();
				float ty = sPos.y * shadowMap.getHeight();
				int sx = (int) Math.min(Math.max(tx, 0), shadowMap.getWidth() - 1);
				int sy = (int) Math.min(Math.max(ty, 0), shadowMap.getHeight() - 1);

				int step = 3;
				int radius = step * 5;
				float min = radius - step;
				for (int y = -radius; y < radius; y += step) {
					for (int x = -radius; x < radius; x += step) {
						if (sx + x >= 0 && sx + x < shadowMap.getWidth() - 1 && sy + y >= 0 && sy + y < shadowMap.getHeight() - 1) {
							float depth = shadowMap.get(sx + x, sy + y);
							float diff = (sPos.w - (1 / depth)) * 4;
							if (diff > 0 && diff < min) {
								min = diff;
							}
						}
					}
				}
				radius = (int) min;

				int samples = 0;
				float shadow = 0;
				float z = 1 / sPos.w;
				for (int y = -radius; y <= radius; y += step) {
					for (int x = -radius; x <= radius; x += step) {
						if (sx + x >= 0 && sx + x < shadowMap.getWidth() - 1 && sy + y >= 0 && sy + y < shadowMap.getHeight() - 1) {
							float depth = shadowMap.get(sx + x, sy + y);
							if (z > depth) shadow++;
							samples++;
						} else {
							shadow++;
							samples++;
						}
					}
				}
				shadow /= samples;

				brightness = -lightDir.dot(normal) * shadow;
				brightness = brightness > 0.1f ? brightness : 0.1f;
				
				Vec3f ambient = new Vec3f(0.01f, 0, 0.1f);
				
				
				Vertex out = new Vertex(0, 1, 0);
				out.pos = in.pos.clone();
				out.vec3[0] = new Vec3f(1, 0.95f, 0.6f).mul(brightness).add(ambient);
				return out;
			}
		};
		
		Uniform uniform = new Uniform();
		
		ShaderProgram sp = new ShaderProgram(vertexShader, fragmentShader, uniform);
		
		// ----------------------------------------------------------------------------

		// Shadow Shader Program -----------------------------------------------------
		Shader shadowVS = new Shader() {
			public Vertex main(Vertex in, Uniform uniform) {
				Mat4f projView = uniform.getMat4f("projView");
				Mat4f modelMatrix = uniform.getMat4f("modelMatrix");
				
				Vertex out = new Vertex(0, 0, 0);
				Mat4f mat = projView.mul(modelMatrix);
				out.pos = in.pos.mul(mat);
				return out;
			}
		};
		
		Shader shadowFS = new Shader() {
			public Vertex main(Vertex in, Uniform uniform) {
				Vertex out = new Vertex(0, 1, 0);
				out.pos = in.pos.clone();
				out.vec3[0] = new Vec3f(0, 0, (1 / in.pos.w) / 10);
				return out;
			}
		};
		
//		Uniform shadowUniform = new Uniform();
		
		ShaderProgram shadowSP = new ShaderProgram(shadowVS, shadowFS, uniform);
		
		// ----------------------------------------------------------------------------
		
		int shadowRes = 256;
		ColorBuffer shadowColorBuffer = new ColorBuffer(shadowRes, shadowRes);
		DepthBuffer shadowDepthBuffer = new DepthBuffer(shadowRes, shadowRes);
		
		Camera shadowCam = new Camera(new Vec3f(-5, 1f, 0), new Vec3f(20, 90, 0));
		shadowCam.setOrthographicMatrix(-1.5f, 1.5f, 1.5f, -1, 0, 10);

		EntityType sphereType = new EntityType(cubeModel, null, sp);
		EntityType planeType = new EntityType(planeModel, null, sp);
		
		Camera cam = new Camera(new Vec3f(0, 0, -3), new Vec3f(0, 0, 0));
//		Camera cam = new Camera(shadowCam.position, shadowCam.rotation);
		cam.setProjectionMatrix(90, (float) width / height, 0.3f, 100);

		uniform.setVec3f("lightDir", shadowCam.getViewDir());
		
		Entity test = sphereType.create(-2, -0.25f, 0, 0, 0, 0, 1f);
		planeType.create(0, -1, 0, 0, 0, 0, 4);

		Texture font = new Texture("res/small_font.png");
		
		FPSCounter.init();
		
		boolean isLeft = false;
		
		boolean running = true;
		while (running) {
			FPSCounter.tick();
			

			if (display.mb_left) {
				int halfWidth = display.getCanvasWidth() / 2;
				int halfHeight = display.getCanvasHeight() / 2;
				int dx = display.mouse_x - halfWidth;
				int dy = display.mouse_y - halfHeight;
				
				if (!isLeft) {
					display.canvas.setCursor(display.canvas.getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), null));
					isLeft = true;
				} else {
					cam.rotation.y += (float) dx / 10;
					cam.rotation.x += (float) dy / 10;
				}

				Robot robot;
				try {
					robot = new Robot();
					Point point = display.getCanvasPos();
					robot.mouseMove(point.x + halfWidth, point.y + halfHeight);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			} else {
				if (isLeft) {
					display.canvas.setCursor(Cursor.getDefaultCursor());
					isLeft = false;
				}
			}
			
			if (display.up) {
				cam.position.x += (float) Math.sin(Math.toRadians(cam.rotation.y)) * 0.1f;
				cam.position.z += (float) Math.cos(Math.toRadians(cam.rotation.y)) * 0.1f;
			}
			
			if (display.down) {
				cam.position.x -= (float) Math.sin(Math.toRadians(cam.rotation.y)) * 0.1f;
				cam.position.z -= (float) Math.cos(Math.toRadians(cam.rotation.y)) * 0.1f;
			}
			
			if (display.right) {
				cam.position.x += (float) Math.cos(Math.toRadians(cam.rotation.y)) * 0.1f;
				cam.position.z -= (float) Math.sin(Math.toRadians(cam.rotation.y)) * 0.1f;
			}
			
			if (display.left) {
				cam.position.x -= (float) Math.cos(Math.toRadians(cam.rotation.y)) * 0.1f;
				cam.position.z += (float) Math.sin(Math.toRadians(cam.rotation.y)) * 0.1f;
			}

			if (display.space) cam.position.y += 0.05;
			if (display.shift) cam.position.y -= 0.05;

			test.rot.y += 1f;
			test.rot.x += 2f;
			
			// Render Shadows To ShadowMap
			Renderer.bindShaderProgram(shadowSP);
			Renderer.setColorBuffer(shadowColorBuffer);
			Renderer.setDepthBuffer(shadowDepthBuffer);
			Renderer.backfaceCulling = false;
			Renderer.frontfaceCulling = true;
			Renderer.setNearClippingPlane(-2);
			Renderer.setFarClippingPlane(2);

			uniform.setMat4f("projView", shadowCam.getProjectionViewMatrix());

			shadowColorBuffer.fill(black);
			shadowDepthBuffer.fill(0);

			sphereType.render(uniform);
			planeType.render(uniform);
			// ---------------------------
			
			
			// Render To Screen ----------
			Renderer.bindShaderProgram(sp);
			Renderer.setColorBuffer(colorBuffer);
			Renderer.setDepthBuffer(depthBuffer);
			Renderer.backfaceCulling = true;
			Renderer.frontfaceCulling = false;
			Renderer.setNearClippingPlane(0.3f);
			Renderer.setFarClippingPlane(100);
			
			uniform.setBuffer("shadowMap", shadowDepthBuffer);
			uniform.setMat4f("projView", cam.getProjectionViewMatrix());
			uniform.setMat4f("shadowProj", shadowCam.getProjectionViewMatrix());

			colorBuffer.fill(black);
			depthBuffer.fill(0);
			
			planeType.render(uniform);
			sphereType.render(uniform);
			// ---------------------------

			display.drawString(0, 0, 4, 6, "FPS: " + FPSCounter.getFPS(), font);
			display.drawString(0, 7, 4, 6, "SCREEN RES: " + width + "x" + height, font);
			display.drawString(0, 14, 4, 6, "SHADOW TECHNIQUE: PCSS", font);
			display.drawString(0, 21, 4, 6, "SHADOW MAP: " + shadowRes + "x" + shadowRes, font);
			display.drawString(0, 28, 4, 6, "SHADOW SAMPLES: 5", font);
//			display.drawRect(9, 9, shadowRes + 2, shadowRes + 2, new Vec3f(1, 1, 1));
//			for (int y = 0; y < shadowRes; y++) {
//				for (int x = 0; x < shadowRes; x++) {
//					float depth = shadowDepthBuffer.get(x, y);
//					Vec3f color = new Vec3f(depth, depth, depth);
//					display.drawPixel(x + 10, y + 10, color);
//				}
//			}
			display.update();
		}
	}

}
