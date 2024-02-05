package com.lukaseichberg.demo;

import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec3f;

public class SimpleEntity {
	
	private Vec3f position, rotation;
	private float scale;
	
	public SimpleEntity(Vec3f position, Vec3f rotation, float scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Mat4f getTransformationMatrix() {
		Mat4f mat = Mat4f.scale(scale, scale, scale)
				.mul(Mat4f.rotateX(rotation.x))
				.mul(Mat4f.rotateY(rotation.y))
				.mul(Mat4f.rotateZ(rotation.z))
				.mul(Mat4f.translate(position.x, position.y, position.z));
		return mat;
	}

}
