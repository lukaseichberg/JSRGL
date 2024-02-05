package com.lukaseichberg.particle;

import com.lukaseichberg.maths.Vec3f;

public class Particle {
	
	private Vec3f pos, vel;
	private float gravity;
	private long lifeLength;
	private float rotation;
	private float scale;
	private long startTime;
	
	public Particle(Vec3f pos, Vec3f vel, float gravity, long lifeLength, float rotation, float scale) {
		this.pos = pos;
		this.vel = vel;
		this.gravity = gravity;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		startTime = System.currentTimeMillis();
	}
	
	protected boolean update() {
		vel.y += gravity;
		pos._add(vel);
		rotation += 1f;
		long elapsedTime = System.currentTimeMillis() - startTime;
		return elapsedTime < lifeLength;
	}
	
	public float getLife() {
		long elapsedTime = System.currentTimeMillis() - startTime;
		return (float) Math.min(1, (float) elapsedTime / lifeLength);
	}
	
	public Vec3f getPos() {
		return pos;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public float getScale() {
		return scale;
	}

}
