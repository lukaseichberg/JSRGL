package com.lukaseichberg.sampler;

import com.lukaseichberg.buffer.FrameBufferInterface;
import com.lukaseichberg.maths.Vec2f;

class NearestFiltering<T> implements FilteringInterface<T> {

	@Override
	public T sample(FrameBufferInterface<T> frame, AddressingInterface<T> addressing, Vec2f uv) {
		int x = (int) (frame.getWidth() * uv.x);
		int y = (int) (frame.getHeight() * uv.y);
		return addressing.sample(frame, x, y);
	}

}
