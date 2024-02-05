package com.lukaseichberg.demo;

import com.lukaseichberg.buffer.ColorBuffer;
import com.lukaseichberg.buffer.DepthBuffer;
import com.lukaseichberg.display.Display;
import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.maths.Vec4f;
import com.lukaseichberg.mesh.Model;
import com.lukaseichberg.mesh.OBJLoader;
import com.lukaseichberg.renderer.Renderer;
import com.lukaseichberg.sampler.FilteringMode;
import com.lukaseichberg.sampler.Sampler;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.shader.ShaderProgram;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;
import com.lukaseichberg.texture.CubeMap;

public class Main {

	static float rot = 0;
	static float modelScale = 1;
	static CubeMap cubeMap;
	static boolean reflect = false;
	
	public static void main(String[] args) {
		int scale	= 2;
		int width	= 640 / scale;
		int height	= 640 / scale;
		
		Display display = new Display(width, height, scale, "CubeMap Renderer");

//		ColorBuffer colorBuffer = new ColorBuffer(256, 256);
//		DepthBuffer depthBuffer = new DepthBuffer(256, 256);
		ColorBuffer colorBuffer = display.getColorBuffer();
		DepthBuffer depthBuffer = display.getDepthBuffer();
		
		
		Renderer.setColorBuffer(colorBuffer);
		Renderer.setDepthBuffer(depthBuffer);

		Sampler<Vec3f> sampler = new Sampler<Vec3f>();
//		sampler.setFilteringMode(FilteringMode.BILINEAR);
		sampler.setFilteringMode(FilteringMode.NEAREST_POINT);
		
		CubeMap cubeMaptmp = new CubeMap("res/yokohama_lowres", "jpg");
		cubeMaptmp.setSampler(sampler);
		
		CubeMap lightMap = cubeMaptmp.getConv(1000, 0.5f, 64);
		lightMap.setSampler(sampler);
		
		Shader vertexShader = new Shader() {
			public Vertex main(Vertex in, Uniform uniform) {
				Vertex out = new Vertex(0, 0, 2);
				out.pos = in.pos.mul(modelScale).add(new Vec4f(0, 0, 1.5f, 0));
				if (reflect) {
					Vec3f dir = out.pos.vec3f().normal();
					Vec4f reflection = new Vec4f(reflect(dir, in.vec4[0].vec3f().normal()), 1);
					out.vec4[0] = reflection.mul(Mat4f.rotateY(rot));
					out.vec4[1] = in.vec4[0].mul(Mat4f.rotateY(rot));
				} else {
					out.vec4[0] = in.vec4[0].mul(Mat4f.rotateY(rot));
					out.vec4[1] = new Vec4f(0, 0, 0, 0);
				}
				return out;
			}
		};
		
		Shader fragmentShader = new Shader() {
			public Vertex main(Vertex in, Uniform uniform) {
				Vertex out = new Vertex(0, 1, 0);
				Vec3f color = cubeMap.get(in.vec4[0].vec3f());
				out.vec3[0] = color;
				return out;
			}
		};
		
//		Texture font = new Texture("res/font.png");
		
		Uniform uniform = new Uniform();
		
		ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader, uniform);
		Renderer.bindShaderProgram(shaderProgram);
		
		Model model = OBJLoader.load("res/Sphere.obj");
		
		long lastTime = System.currentTimeMillis();
		int fps = 0;
		int frames = 0;
		
		boolean running = true;
		while (running) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastTime >= 1000) {
				lastTime = currentTime;
				fps = frames;
				frames = 0;
				System.out.println("FPS: " + fps);
			}
			frames++;
			
			rot += 0.5f;
//			displayBuffer.fill(new Vec3f(0.25f, 0.25f, 0.25f));
			colorBuffer.fill(new Vec3f(0, 0, 0));
			depthBuffer.fill(0);
			
			Renderer.backfaceCulling = true;
			Renderer.frontfaceCulling = false;
			cubeMap = lightMap;
			modelScale = 0.5f;
			reflect = true;
			model.render();

			Renderer.backfaceCulling = false;
			Renderer.frontfaceCulling = true;
			cubeMap = cubeMaptmp;
			modelScale = 5;
			reflect = false;
			model.render();
			
//			display.drawImage(0, 0, colorBuffer);
//			display.drawString(258, 1, 8, 8, "FPS: "+fps, font);
			display.update();
		}
	}

}
