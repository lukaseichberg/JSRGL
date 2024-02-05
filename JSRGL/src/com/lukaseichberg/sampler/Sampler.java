package com.lukaseichberg.sampler;

import com.lukaseichberg.buffer.FrameBufferInterface;
import com.lukaseichberg.maths.Vec2f;

public class Sampler<T> {
	
	private AddressingInterface<T> addressing;
	private FilteringInterface<T> filtering;
	
	public Sampler() {
		setAddressingMode(AddressingMode.WRAP);
		setFilteringMode(FilteringMode.NEAREST_POINT);
	}
	
	public void setAddressingMode(AddressingMode mode) {
		switch (mode) {
			case WRAP:
				addressing = new WrapAddressing<T>();
			case CLAMP:
				addressing = new ClampAddressing<T>();
		}
	}
	
	public void setFilteringMode(FilteringMode mode) {
		switch (mode) {
			case NEAREST_POINT:
				filtering = new NearestFiltering<>();
				break;
				
			case BILINEAR:
				filtering = new BilinearFiltering<>();
				break;
		}
	}
	
	public T sample(FrameBufferInterface<T> frame, Vec2f uv) {
		return filtering.sample(frame, addressing, uv);
	}
	
}
