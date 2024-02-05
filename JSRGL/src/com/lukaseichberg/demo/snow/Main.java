package com.lukaseichberg.demo.snow;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;

import com.lukaseichberg.buffer.ColorBuffer;
import com.lukaseichberg.buffer.DepthBuffer;
import com.lukaseichberg.camera.Camera;
import com.lukaseichberg.display.Display;
import com.lukaseichberg.engine.Scene;
import com.lukaseichberg.light.SpotLight;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.mesh.Model;
import com.lukaseichberg.mesh.OBJLoader;
import com.lukaseichberg.particle.ParticleSystem;
import com.lukaseichberg.renderer.Renderer;
import com.lukaseichberg.sampler.FilteringMode;
import com.lukaseichberg.sampler.Sampler;
import com.lukaseichberg.shader.ShaderProgram;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.texture.CubeMap;
import com.lukaseichberg.texture.Texture;
import com.lukaseichberg.texture.TextureAlpha;

public class Main {

	public static void main(String[] args) {
		int width = 320;
		int height = 240;
		float aspect = (float) width / height;
		float near = 0.3f;
		float far = 15f;
		
		Display display = new Display(width, height, 4, "Dynamic Reflections");

		ColorBuffer colorBuffer = display.getColorBuffer();
		DepthBuffer depthBuffer = display.getDepthBuffer();
		
		Renderer.setColorBuffer(colorBuffer);
		Renderer.setDepthBuffer(depthBuffer);

		Uniform uniform = new Uniform();
		
		DefaultVS defaultVS = new DefaultVS();
		DefaultFS defaultFS = new DefaultFS();
		ShaderProgram defaultShader = new ShaderProgram(defaultVS, defaultFS, uniform);

		LampVS lampVS = new LampVS();
		LampFS lampFS = new LampFS();
		ShaderProgram lampShader = new ShaderProgram(lampVS, lampFS, uniform);
		
//		SnowVS snowVS = new SnowVS();
//		SnowFS snowFS = new SnowFS();
//		ShaderProgram snowShader = new ShaderProgram(snowVS, snowFS, uniform);
		
		ParticleSystem ps = new ParticleSystem("res/fire.png");
		
		Texture font = new Texture("res/small_font.png");
		
		Vec3f camPos = new Vec3f(25, 2, 25);
		Vec3f camRot = new Vec3f(0, 0, 0);
		Camera camera = new Camera(camPos, camRot);
		camera.setProjectionMatrix(90, aspect, near, far);
		
		Renderer.setNearClippingPlane(near);
		Renderer.setFarClippingPlane(far);
		
//		Texture groundTexture = new Texture("res/grass.png");
//		Terrain terrain = new Terrain(10, 10, 32);
//		terrain.generate();

		WhiteVS whiteVS = new WhiteVS();
		WhiteFS whiteFS = new WhiteFS();
		ShaderProgram whiteShader = new ShaderProgram(whiteVS, whiteFS, uniform);
		
		Scene scene = new Scene();
		
		Model sphereModel = OBJLoader.load("res/Sphere.obj");
		EntityType sphereType = new EntityType(sphereModel, null, whiteShader);
//		scene.add(sphereType);
		
		Model barrel = OBJLoader.load("res/rusty_barrel.obj");
		Texture barrelTexture = new Texture("res/rusty_barrel.png");
		EntityType barrelType = new EntityType(barrel, barrelTexture, defaultShader);
		barrelType.backfaceCulling = false;
		scene.add(barrelType);

		Model street = OBJLoader.load("res/plane.obj");
		Texture streetTexture = new Texture("res/street_texture.png");
		EntityType streetType = new EntityType(street, streetTexture, defaultShader);
		scene.add(streetType);

		Model lampModel = OBJLoader.load("res/streetlamp.obj");
		Texture lampTexture = new Texture("res/streetlamp.png");
		Texture lampGlowMap = new Texture("res/glowmap.png");
		EntityType lampType = new EntityType(lampModel, lampTexture, lampShader);
		scene.add(lampType);
		for (int i = 0; i < 5; i++) {
//			float h = terrain.getHeight(((float) (i * 10) / 5), 25f / 5) * 50;
			float h = 0;
			lampType.create((i * 9), h, 24, 0, 90, 0, 0.2f);
//			sphereType.create((i * 10), h + 5.25f, 51.5f, 0, 0, 0, 0.25f);
			uniform.spotLights.add(new SpotLight(new Vec3f((i * 9), h + 5.25f, 25.5f), new Vec3f(0, -1, 0.15f), 20, 40));
//			barrelType.create((i * 10), h+2f, 31, 180, 0, 0, 0.75f);
			streetType.create((i * 10), h, 29, 0, 90, 0, 5f);
		}
		barrelType.create(10, 1.33f, 26, 180, 0, 0, 0.5f);
		barrelType.create(11.5f, 1.33f, 26, 180, 0, 0, 0.5f);
		barrelType.create(15, 0.1f, 29, 0, 0, 0, 0.5f);
		uniform.setTexture("glowMap", lampGlowMap);
//		uniform.spotLights.add(new SpotLight(new Vec3f(50, 100, 50), new Vec3f(0, -1, 0), (float) Math.cos(Math.toRadians(45f))));
		
//		Renderer.backfaceCulling = false;
		
		float decalSize = 0.25f;
		
		List<Decal> decals = new ArrayList<>();
		uniform.setInt("decalCount", decals.size());
		
		TextureAlpha decalTexture = new TextureAlpha("res/test.png");
		uniform.setTextureAlpha("decalTexture", decalTexture);
		
		Vec3f black = new Vec3f(0.02f, 0.02f, 0.02f);
		
		uniform.setVec3f("lightDir", new Vec3f(-0.5f, -1, -0.25f));
		
		int refRes = 32;
		Vec3f c360p = new Vec3f(12, 1.5f, 29);
		Camera c360 = new Camera(c360p, new Vec3f(0, 0, 0));
		sphereType.create(c360p.x, c360p.y, c360p.z, 0, 0, 0, 1);
		c360.setProjectionMatrix(90, 1, 0.1f, 50);
		ColorBuffer cbZP = new ColorBuffer(refRes, refRes);
		ColorBuffer cbZN = new ColorBuffer(refRes, refRes);
		ColorBuffer cbYP = new ColorBuffer(refRes, refRes);
		ColorBuffer cbYN = new ColorBuffer(refRes, refRes);
		ColorBuffer cbXP = new ColorBuffer(refRes, refRes);
		ColorBuffer cbXN = new ColorBuffer(refRes, refRes);
		DepthBuffer db = new DepthBuffer(refRes, refRes);
		
		Sampler<Vec3f> sampler = new Sampler<>();
		sampler.setFilteringMode(FilteringMode.BILINEAR);
		
		FPSCounter.init();
		
//		long lastTime = System.currentTimeMillis();
//		
//		Vec3f origin = new Vec3f(15, 1f, 29);

		boolean running = true;
		while (running) {
			FPSCounter.tick();
			colorBuffer.fill(black);
			depthBuffer.fill(0);
			
			if (display.mb_left) {
				int halfWidth = display.getCanvasWidth() / 2;
				int halfHeight = display.getCanvasHeight() / 2;
				int dx = display.mouse_x - halfWidth;
				int dy = display.mouse_y - halfHeight;
				
				camera.rotation.y += (float) dx / 10;
				camera.rotation.x += (float) dy / 10;

				Robot robot;
				try {
					robot = new Robot();
					Point point = display.getCanvasPos();
					robot.mouseMove(point.x + halfWidth, point.y + halfHeight);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
			
			if (display.k) {
				Decal decal = new Decal(camera.position, camera.rotation, decalSize, decalSize, 1f);
				uniform.setMat4f("decalMat["+decals.size()+"]", decal.getMatrix());
				decals.add(decal);
				uniform.setInt("decalCount", decals.size());
				display.k = false;
			}
			
			if (display.up) {
				camera.position.x += (float) Math.sin(Math.toRadians(camera.rotation.y)) * 0.1f;
				camera.position.z += (float) Math.cos(Math.toRadians(camera.rotation.y)) * 0.1f;
			}
			
			if (display.down) {
				camera.position.x -= (float) Math.sin(Math.toRadians(camera.rotation.y)) * 0.1f;
				camera.position.z -= (float) Math.cos(Math.toRadians(camera.rotation.y)) * 0.1f;
			}
			
			if (display.right) {
				camera.position.x += (float) Math.cos(Math.toRadians(camera.rotation.y)) * 0.1f;
				camera.position.z -= (float) Math.sin(Math.toRadians(camera.rotation.y)) * 0.1f;
			}
			
			if (display.left) {
				camera.position.x -= (float) Math.cos(Math.toRadians(camera.rotation.y)) * 0.1f;
				camera.position.z += (float) Math.sin(Math.toRadians(camera.rotation.y)) * 0.1f;
			}

			if (display.space) camera.position.y += 0.05;
			if (display.shift) camera.position.y -= 0.05;

			ps.update();

			Renderer.setDepthBuffer(db);
			Renderer.setColorBuffer(cbZP);
			cbZP.fill(black);
			db.fill(0);
			c360.rotation = new Vec3f(0, 0, 0);
			uniform.setMat4f("projViewMatrix", c360.getProjectionViewMatrix());
			scene.render();
			Renderer.blend = Renderer.BLEND_ADD;
			Renderer.depthBuffering = false;
			ps.render(c360.getViewMatrix(), c360.projection);
			Renderer.blend = Renderer.BLEND_OPAQUE;
			Renderer.depthBuffering = true;

			Renderer.setColorBuffer(cbZN);
			cbZN.fill(black);
			db.fill(0);
			c360.rotation = new Vec3f(0, 180, 0);
			uniform.setMat4f("projViewMatrix", c360.getProjectionViewMatrix());
			scene.render();
			Renderer.blend = Renderer.BLEND_ADD;
			Renderer.depthBuffering = false;
			ps.render(c360.getViewMatrix(), c360.projection);
			Renderer.blend = Renderer.BLEND_OPAQUE;
			Renderer.depthBuffering = true;
			
			Renderer.setColorBuffer(cbXP);
			cbXP.fill(black);
			db.fill(0);
			c360.rotation = new Vec3f(0, 90, 0);
			uniform.setMat4f("projViewMatrix", c360.getProjectionViewMatrix());
			scene.render();
			Renderer.blend = Renderer.BLEND_ADD;
			Renderer.depthBuffering = false;
			ps.render(c360.getViewMatrix(), c360.projection);
			Renderer.blend = Renderer.BLEND_OPAQUE;
			Renderer.depthBuffering = true;
			
			Renderer.setColorBuffer(cbXN);
			cbXN.fill(black);
			db.fill(0);
			c360.rotation = new Vec3f(0, 270, 0);
			uniform.setMat4f("projViewMatrix", c360.getProjectionViewMatrix());
			scene.render();
			Renderer.blend = Renderer.BLEND_ADD;
			Renderer.depthBuffering = false;
			ps.render(c360.getViewMatrix(), c360.projection);
			Renderer.blend = Renderer.BLEND_OPAQUE;
			Renderer.depthBuffering = true;
			
			Renderer.setColorBuffer(cbYP);
			cbYP.fill(black);
			db.fill(0);
			c360.rotation = new Vec3f(-90, 0, 0);
			uniform.setMat4f("projViewMatrix", c360.getProjectionViewMatrix());
			scene.render();
			Renderer.blend = Renderer.BLEND_ADD;
			Renderer.depthBuffering = false;
			ps.render(c360.getViewMatrix(), c360.projection);
			Renderer.blend = Renderer.BLEND_OPAQUE;
			Renderer.depthBuffering = true;
			
			Renderer.setColorBuffer(cbYN);
			cbYN.fill(black);
			db.fill(0);
			c360.rotation = new Vec3f(90, 0, 0);
			uniform.setMat4f("projViewMatrix", c360.getProjectionViewMatrix());
			scene.render();
			Renderer.blend = Renderer.BLEND_ADD;
			Renderer.depthBuffering = false;
			ps.render(c360.getViewMatrix(), c360.projection);
			Renderer.blend = Renderer.BLEND_OPAQUE;
			Renderer.depthBuffering = true;
			
			CubeMap cm = new CubeMap();
			cm.setSampler(sampler);
			cm.setFaceTexture(cbYP, CubeMap.TOP);
			cm.setFaceTexture(cbYN, CubeMap.BOTTOM);
			cm.setFaceTexture(cbXP, CubeMap.RIGHT);
			cm.setFaceTexture(cbXN, CubeMap.LEFT);
			cm.setFaceTexture(cbZP, CubeMap.BACK);
			cm.setFaceTexture(cbZN, CubeMap.FRONT);
			uniform.setCubeMap("cubeMap", cm);
			
			Renderer.setColorBuffer(colorBuffer);
			Renderer.setDepthBuffer(depthBuffer);
			
			uniform.setMat4f("projViewMatrix", camera.getProjectionViewMatrix());
			uniform.setVec3f("camPos", camera.position);
			
			scene.render();
			
			sphereType.render(uniform);
			/*
			if (Math.random() < 0.2f) {
				ps.add(new Particle(
					origin.clone(),
					new Vec3f(
						(float) (Math.random() * 2 - 1) * 0.005f, 
						(float) Math.random() * 0.02f + 0.01f, 
						(float) (Math.random() * 2 - 1) * 0.005f
					),
					0,
					1500,
					(float) (Math.random() * 360),
					(float) (Math.random() * 0.5f + 0.1f)
				));
			}

			Renderer.blend = Renderer.BLEND_ADD;
			Renderer.depthBuffering = false;
			ps.render(camera.getViewMatrix(), camera.projection);
			Renderer.blend = Renderer.BLEND_OPAQUE;
			Renderer.depthBuffering = true;
			*/
			display.drawString(0, 0, 4, 6, "FPS: " + FPSCounter.getFPS(), font);
			display.drawString(0, 7, 4, 6, "RESOLUTION: " + width + "x" + height, font);
			display.drawString(0, 14, 4, 6, "CUBEMAP RES: " + refRes, font);
			display.drawString(0, 21, 4, 6, "PARTICLES: " + ps.getNumber(), font);
			display.drawString(0, 28, 4, 6, "DECALS: " + decals.size(), font);
			
			display.update();
		}
	}
	
//	private static Mat4f createTransform(float x, float y, float z, float rotX, float rotY, float rotZ, float scale) {
//		return Mat4f.identity()
//				.mul(Mat4f.rotateX(rotX))
//				.mul(Mat4f.rotateY(rotY))
//				.mul(Mat4f.rotateZ(rotZ))
//				.mul(Mat4f.scale(scale, scale, scale))
//				.mul(Mat4f.translate(x, y, z));
//	}

}
