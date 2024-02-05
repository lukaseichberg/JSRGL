package com.lukaseichberg.buffer;

public class DepthBuffer implements FrameBufferInterface<Float> {
	
	private int width, height;
	private float[] data;
	
	public DepthBuffer(int width, int height) {
		this.width = width;
		this.height = height;
		data = new float[width * height];
	}
	
	public void fill(float value) {
		for (int i = 0; i < data.length; i++) {
			data[i] = value;
		}
	}
	
	public void set(int x, int y, Float value) {
		data[x + y * width] = value;
	}
	
	public Float get(int x, int y) {
		return data[x + y * width];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

}
