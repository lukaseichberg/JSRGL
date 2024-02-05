package com.lukaseichberg.sampler;

import com.lukaseichberg.buffer.FrameBufferInterface;
import com.lukaseichberg.maths.Vec2f;

interface FilteringInterface<T> {
	
	public T sample(FrameBufferInterface<T> frame, AddressingInterface<T> addressing, Vec2f uv);

}
