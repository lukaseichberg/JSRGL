package com.lukaseichberg.sampler;

import com.lukaseichberg.buffer.FrameBufferInterface;

interface AddressingInterface<T> {
	
	public T sample(FrameBufferInterface<T> frame, int x, int y);
}
