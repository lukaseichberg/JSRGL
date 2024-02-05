package com.lukaseichberg.physics;

import com.lukaseichberg.maths.Vec3f;

public class Ray {
	
	public Vec3f origin;
	public Vec3f dir;
	
	public Ray(Vec3f origin, Vec3f dir) {
		this.origin = origin;
		this.dir = dir;
	}
	
	public Vec3f[] closestPoint(Ray r) {
		if (Math.abs(dir.normal().dot(r.dir.normal())) == 1f) {
			return null;
		}

		float t = (r.origin.sub(origin).dot(dir) * r.dir.dot(r.dir) + origin.sub(r.origin).dot(r.dir) * dir.dot(r.dir)) / (dir.dot(dir) * r.dir.dot(r.dir) - (float) Math.pow(dir.dot(r.dir), 2));
		float s = (origin.sub(r.origin).dot(r.dir) * dir.dot(dir) + r.origin.sub(origin).dot(dir) * dir.dot(r.dir)) / (dir.dot(dir) * r.dir.dot(r.dir) - (float) Math.pow(dir.dot(r.dir), 2));
		
		return new Vec3f[] {
			origin.add(dir.mul(t)),
			r.origin.add(r.dir.mul(s)),
		};
	}

}
