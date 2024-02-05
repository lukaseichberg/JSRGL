package com.lukaseichberg.modelviewer;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;

import com.lukaseichberg.buffer.ColorBuffer;
import com.lukaseichberg.buffer.DepthBuffer;
import com.lukaseichberg.camera.Camera;
import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.maths.Vec4f;
import com.lukaseichberg.renderer.Renderer;
import com.lukaseichberg.shader.ShaderProgram;
import com.lukaseichberg.structs.Uniform;

public class Main {
	
	public static int tilesX = 1;
	public static int tilesY = 1;
	public static int texIndex = 1;

	public static void main(String[] args) {
//	    try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
//				| UnsupportedLookAndFeelException e) {
//			e.printStackTrace();
//		}
	    int scale = 1;
		Display display = new Display(1280 / scale, 960 / scale, scale, "Model Viewer");
		
		ColorBuffer colorBuffer = display.getColorBuffer();
		DepthBuffer depthBuffer = display.getDepthBuffer();
		
		DefaultVS vs = new DefaultVS();
		DefaultFS fs = new DefaultFS();
		Uniform uniform = new Uniform();
		
		ShaderProgram sp = new ShaderProgram(vs, fs, uniform);
		
		Renderer.setColorBuffer(colorBuffer);
		Renderer.setDepthBuffer(depthBuffer);
		Renderer.bindShaderProgram(sp);
		
		ModelViewer.display = display;
		
		float aspect = (float) display.getWidth() / display.getHeight();
		
		float dist = 1;
		Vec4f pos = new Vec4f(0, 0, -1, 0);
		
		Vec3f camPos = new Vec3f(0, 0, -5);
		Vec3f camRot = new Vec3f(0, 0, 0);
		Camera cam = new Camera(camPos, camRot);
		cam.setProjectionMatrix(90, aspect, 0.1f, 1000);
		Mat4f mat = cam.getProjectionViewMatrix();//.mul(Mat4f.scale(0.1f, 0.1f, 0.1f));
		uniform.setMat4f("projView", mat);
		
		Vec3f black = new Vec3f(0, 0, 0);
		
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		long lastTime = System.currentTimeMillis();
		int frames = 0;
		int fps = 0;
		
		boolean running = true;
		while (running) {
			if (System.currentTimeMillis() - lastTime >= 1000) {
				fps = frames;
				frames = 0;
				lastTime = System.currentTimeMillis();
				display.setFPS(fps);
			} else {
				frames++;
			}
			
			if (display.mouseScroll != 0) {
				dist += display.mouseScroll;
				display.mouseScroll = 0;
				Mat4f projView = cam.getProjectionViewMatrix();
				cam.position = pos.mul(projView.invert()).vec3f().mul(dist);
				mat = cam.getProjectionViewMatrix().mul(Mat4f.scale(0.1f, 0.1f, 0.1f));
				uniform.setMat4f("projView", mat);
			}
			
			if (display.mb_left && robot != null) {
				int halfWidth = display.getCanvasWidth() / 2;
				int halfHeight = display.getCanvasHeight() / 2;
				int dx = display.mouse_x - halfWidth;
				int dy = display.mouse_y - halfHeight;
				
				Point point = display.getCanvasPos();
				robot.mouseMove(point.x + halfWidth, point.y + halfHeight);
				
				camRot.y += dx * 0.1f;
				camRot.x += dy * 0.1f;
				
				Mat4f projView = cam.getProjectionViewMatrix();
				cam.position = pos.mul(projView.invert()).vec3f().mul(dist);
				mat = cam.getProjectionViewMatrix().mul(Mat4f.scale(0.1f, 0.1f, 0.1f));
				uniform.setMat4f("projView", mat);
			}
			
			uniform.setTextureAlpha("texture", ModelViewer.texture);

			uniform.setInt("tilesX", tilesX);
			uniform.setInt("tilesY", tilesY);
			uniform.setInt("texIndex", texIndex);
			
			
			colorBuffer.fill(black);
			depthBuffer.fill(0);
			ModelViewer.render();
			display.update();
		}
	}

}
