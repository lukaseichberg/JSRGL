package com.lukaseichberg.sampler;

import com.lukaseichberg.buffer.FrameBufferInterface;
import com.lukaseichberg.maths.LerpInterface;
import com.lukaseichberg.maths.Vec2f;

class BilinearFiltering<T> implements FilteringInterface<T> {

	@SuppressWarnings("unchecked")
	@Override
	public T sample(FrameBufferInterface<T> frame, AddressingInterface<T> addressing, Vec2f uv) {
		int width = frame.getWidth();
		int height = frame.getHeight();
		
		uv._sub(new Vec2f(
			(float) 0.5f / width,
			(float) 0.5f / height
		));

		float x = width * uv.x;
		float y = height * uv.y;
		int xx = (int) x;
		int yy = (int) y;
		
		LerpInterface<T> v00 = (LerpInterface<T>) addressing.sample(frame, xx, yy);
		LerpInterface<T> v10 = (LerpInterface<T>) addressing.sample(frame, xx + 1, yy);
		LerpInterface<T> v01 = (LerpInterface<T>) addressing.sample(frame, xx, yy + 1);
		LerpInterface<T> v11 = (LerpInterface<T>) addressing.sample(frame, xx + 1, yy + 1);

		float xOffset = x - xx;
		float yOffset = y - yy;

		LerpInterface<T> v0 = (LerpInterface<T>) v00.lerp(v10, xOffset);
		LerpInterface<T> v1 = (LerpInterface<T>) v01.lerp(v11, xOffset);
		
		return v0.lerp(v1, yOffset);
	}

}
