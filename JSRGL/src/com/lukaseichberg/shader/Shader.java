package com.lukaseichberg.shader;

import com.lukaseichberg.buffer.FrameBufferInterface;
import com.lukaseichberg.maths.Vec2f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.sampler.Sampler;

public abstract class Shader implements ShaderInterface {
	
	protected boolean isBack = false;
	private Sampler<Vec3f> sampler;
	
	@SuppressWarnings("rawtypes")
	public Object sample(FrameBufferInterface texture, Vec2f uv) {
		int x = (int) (uv.x * texture.getWidth());
		int y = (int) (uv.y * texture.getHeight());
		x = Math.floorMod(x, texture.getWidth());
		y = Math.floorMod(y, texture.getHeight());
		return texture.get(x, y);
	}
	
	public Vec3f texture(FrameBufferInterface<Vec3f> texture, Vec2f uv) {
		return sampler.sample(texture, uv);
	}
	
	public void setSampler(Sampler<Vec3f> sampler) {
		this.sampler = sampler;
	}
	
	public Vec3f reflect(Vec3f dir, Vec3f normal) {
		return dir.sub(normal.mul(2 * dir.dot(normal)));
	}
	
	public Vec3f calcLighting() {
		
		return null;
	}
}
