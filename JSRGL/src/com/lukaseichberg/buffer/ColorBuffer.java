package com.lukaseichberg.buffer;

import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.maths.Vec4f;

public class ColorBuffer implements FrameBufferInterface<Vec3f> {
	
	private int width, height;
	private Vec3f[] data;
	
	public ColorBuffer(int width, int height) {
		this.width = width;
		this.height = height;
		data = new Vec3f[width * height];
	}
	
	public void fill(Vec3f color) {
		for (int i = 0; i < data.length; i++) {
			data[i] = color;
		}
	}
	
	public void fill(Vec4f color) {
		for (int i = 0; i < data.length; i++) {
			data[i] = data[i].lerp(color.vec3f(), color.w);
		}
	}
	
	public void set(int x, int y, Vec3f color) {
		data[x + y * width] = color;
	}
	
	public Vec3f get(int x, int y) {
		return data[x + y * width];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

}
