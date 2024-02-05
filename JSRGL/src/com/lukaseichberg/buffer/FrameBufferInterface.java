package com.lukaseichberg.buffer;

public interface FrameBufferInterface<T> {
	
	public void set(int x, int y, T value);
	public T get(int x, int y);
	public int getWidth();
	public int getHeight();

}
