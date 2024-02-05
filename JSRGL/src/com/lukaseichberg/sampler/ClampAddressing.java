package com.lukaseichberg.sampler;

import com.lukaseichberg.buffer.FrameBufferInterface;

class ClampAddressing<T> implements AddressingInterface<T> {

	@Override
	public T sample(FrameBufferInterface<T> frame, int x, int y) {
		int width = frame.getWidth() - 1;
		int height = frame.getHeight() - 1;
		int xx = (x < 0) ? 0 : (x > width) ? width : x;
		int yy = (y < 0) ? 0 : (y > height) ? height : y;
		return frame.get(xx, yy);
	}

}
