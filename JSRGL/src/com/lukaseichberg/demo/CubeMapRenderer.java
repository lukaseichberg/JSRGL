package com.lukaseichberg.demo;

import com.lukaseichberg.buffer.ColorBuffer;
import com.lukaseichberg.buffer.DepthBuffer;
import com.lukaseichberg.renderer.Renderer;
import com.lukaseichberg.texture.CubeMap;

public class CubeMapRenderer {
	
//	private float near, far;
	
	public CubeMapRenderer() {
//		near = 0.25f;
//		far = 100;
	}
	
	public CubeMap render() {
		CubeMap cubeMap = new CubeMap();

		ColorBuffer colorBuffer = new ColorBuffer(32, 32);
		DepthBuffer depthBuffer = new DepthBuffer(32, 32);

		Renderer.setColorBuffer(colorBuffer);
		Renderer.setDepthBuffer(depthBuffer);
		
//		Mat4f viewMatrix = Mat4f.rotateX(90);
		cubeMap.setFaceTexture(colorBuffer, CubeMap.LEFT);
		
		return cubeMap;
	}

}
