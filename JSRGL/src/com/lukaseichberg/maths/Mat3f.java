package com.lukaseichberg.maths;

public class Mat3f {
	
	public float m00;
	public float m01;
	public float m02;
	public float m10;
	public float m11;
	public float m12;
	public float m20;
	public float m21;
	public float m22;
	
	public Mat3f(
			float m00, float m01, float m02,
			float m10, float m11, float m12,
			float m20, float m21, float m22
		) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
	}
	
	public Mat3f(Vec3f v0, Vec3f v1, Vec3f v2) {
		this.m00 = v0.x;
		this.m01 = v0.y;
		this.m02 = v0.z;
		this.m10 = v1.x;
		this.m11 = v1.y;
		this.m12 = v1.z;
		this.m20 = v2.x;
		this.m21 = v2.y;
		this.m22 = v2.z;
	}
	
	public Mat3f mul(Mat3f mat) {
		return new Mat3f(
			m00 * mat.m00 + m01 * mat.m10 + m02 * mat.m20,
			m00 * mat.m01 + m01 * mat.m11 + m02 * mat.m21,
			m00 * mat.m02 + m01 * mat.m12 + m02 * mat.m22,
			m10 * mat.m00 + m11 * mat.m10 + m12 * mat.m20,
			m10 * mat.m01 + m11 * mat.m11 + m12 * mat.m21,
			m10 * mat.m02 + m11 * mat.m12 + m12 * mat.m22,
			m20 * mat.m00 + m21 * mat.m10 + m22 * mat.m20,
			m20 * mat.m01 + m21 * mat.m11 + m22 * mat.m21,
			m20 * mat.m02 + m21 * mat.m12 + m22 * mat.m22
		);
	}
	
//	public Mat3f inverse() {
//		return new Mat3f(
//			m00, m10, m20,
//			m01, m11, m21,
//			m02, m12, m22
//		);
//	}
	
}
