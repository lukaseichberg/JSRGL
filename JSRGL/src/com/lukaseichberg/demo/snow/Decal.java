package com.lukaseichberg.demo.snow;

import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec3f;

public class Decal {
	
	private Mat4f view;
	private Mat4f orth;
	private Mat4f projViewMat;
	
	public Decal(Vec3f pos, Vec3f rot, float width, float height, float depth) {
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		float halfDepth = depth / 2;
		view = Mat4f.viewMatrix(pos, rot);
		orth = Mat4f.orthographic(-halfWidth, halfWidth, halfHeight, -halfHeight, -halfDepth, halfDepth);
		projViewMat = orth.mul(view);
	}
	
	public void setPosRot(Vec3f pos, Vec3f rot) {
		view = Mat4f.viewMatrix(pos, rot);
		projViewMat = orth.mul(view);
	}
	
	public Mat4f getMatrix() {
		return projViewMat;
	}

}
