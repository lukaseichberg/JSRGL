package com.lukaseichberg.demo.snow;

import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec3f;

public class Entity {
	
	public Vec3f pos, rot;
	private float scale;
	
	public Entity(float x, float y, float z, float rotX, float rotY, float rotZ, float scale) {
		pos = new Vec3f(x, y, z);
		rot = new Vec3f(rotX, rotY, rotZ);
		this.scale = scale;
	}
	
	public Mat4f getTransform() {
		return Mat4f.identity()
				.mul(Mat4f.translate(pos.x, pos.y, pos.z))
				.mul(Mat4f.scale(scale, scale, scale))
				.mul(Mat4f.rotateX(rot.x))
				.mul(Mat4f.rotateY(rot.y))
				.mul(Mat4f.rotateZ(rot.z));
	}

}
