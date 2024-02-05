package com.lukaseichberg.sampler;

import com.lukaseichberg.buffer.FrameBufferInterface;

class WrapAddressing<T> implements AddressingInterface<T> {

	@Override
	public T sample(FrameBufferInterface<T> frame, int x, int y) {
		int xx = (int) Math.floorMod(x, frame.getWidth());
		int yy = (int) Math.floorMod(y, frame.getHeight());
		return frame.get(xx, yy);
	}

}
