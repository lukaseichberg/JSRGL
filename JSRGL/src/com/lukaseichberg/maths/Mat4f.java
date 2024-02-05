package com.lukaseichberg.maths;

public class Mat4f {
	
	public float m00;
	public float m01;
	public float m02;
	public float m03;
	public float m10;
	public float m11;
	public float m12;
	public float m13;
	public float m20;
	public float m21;
	public float m22;
	public float m23;
	public float m30;
	public float m31;
	public float m32;
	public float m33;
	
	public Mat4f() {
		this(
			0f, 0f, 0f, 0f,
			0f, 0f, 0f, 0f,
			0f, 0f, 0f, 0f,
			0f, 0f, 0f, 0f
		);
	}
	
	public Mat4f(
			float m00, float m01, float m02, float m03,
			float m10, float m11, float m12, float m13,
			float m20, float m21, float m22, float m23,
			float m30, float m31, float m32, float m33
		) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}
	
	public Mat4f mul(Mat4f mat) {
		return new Mat4f(
			m00 * mat.m00 + m01 * mat.m10 + m02 * mat.m20 + m03 * mat.m30,
			m00 * mat.m01 + m01 * mat.m11 + m02 * mat.m21 + m03 * mat.m31,
			m00 * mat.m02 + m01 * mat.m12 + m02 * mat.m22 + m03 * mat.m32,
			m00 * mat.m03 + m01 * mat.m13 + m02 * mat.m23 + m03 * mat.m33,
			m10 * mat.m00 + m11 * mat.m10 + m12 * mat.m20 + m13 * mat.m30,
			m10 * mat.m01 + m11 * mat.m11 + m12 * mat.m21 + m13 * mat.m31,
			m10 * mat.m02 + m11 * mat.m12 + m12 * mat.m22 + m13 * mat.m32,
			m10 * mat.m03 + m11 * mat.m13 + m12 * mat.m23 + m13 * mat.m33,
			m20 * mat.m00 + m21 * mat.m10 + m22 * mat.m20 + m23 * mat.m30,
			m20 * mat.m01 + m21 * mat.m11 + m22 * mat.m21 + m23 * mat.m31,
			m20 * mat.m02 + m21 * mat.m12 + m22 * mat.m22 + m23 * mat.m32,
			m20 * mat.m03 + m21 * mat.m13 + m22 * mat.m23 + m23 * mat.m33,
			m30 * mat.m00 + m31 * mat.m10 + m32 * mat.m20 + m33 * mat.m30,
			m30 * mat.m01 + m31 * mat.m11 + m32 * mat.m21 + m33 * mat.m31,
			m30 * mat.m02 + m31 * mat.m12 + m32 * mat.m22 + m33 * mat.m32,
			m30 * mat.m03 + m31 * mat.m13 + m32 * mat.m23 + m33 * mat.m33
		);
	}
	
	public Mat4f invert() {
		return new Mat4f(
			m12*m23*m31 - m13*m22*m31 + m13*m21*m32 - m11*m23*m32 - m12*m21*m33 + m11*m22*m33,
			m03*m22*m31 - m02*m23*m31 - m03*m21*m32 + m01*m23*m32 + m02*m21*m33 - m01*m22*m33,
			m02*m13*m31 - m03*m12*m31 + m03*m11*m32 - m01*m13*m32 - m02*m11*m33 + m01*m12*m33,
			m03*m12*m21 - m02*m13*m21 - m03*m11*m22 + m01*m13*m22 + m02*m11*m23 - m01*m12*m23,
			m13*m22*m30 - m12*m23*m30 - m13*m20*m32 + m10*m23*m32 + m12*m20*m33 - m10*m22*m33,
			m02*m23*m30 - m03*m22*m30 + m03*m20*m32 - m00*m23*m32 - m02*m20*m33 + m00*m22*m33,
			m03*m12*m30 - m02*m13*m30 - m03*m10*m32 + m00*m13*m32 + m02*m10*m33 - m00*m12*m33,
			m02*m13*m20 - m03*m12*m20 + m03*m10*m22 - m00*m13*m22 - m02*m10*m23 + m00*m12*m23,
			m11*m23*m30 - m13*m21*m30 + m13*m20*m31 - m10*m23*m31 - m11*m20*m33 + m10*m21*m33,
			m03*m21*m30 - m01*m23*m30 - m03*m20*m31 + m00*m23*m31 + m01*m20*m33 - m00*m21*m33,
			m01*m13*m30 - m03*m11*m30 + m03*m10*m31 - m00*m13*m31 - m01*m10*m33 + m00*m11*m33,
			m03*m11*m20 - m01*m13*m20 - m03*m10*m21 + m00*m13*m21 + m01*m10*m23 - m00*m11*m23,
			m12*m21*m30 - m11*m22*m30 - m12*m20*m31 + m10*m22*m31 + m11*m20*m32 - m10*m21*m32,
			m01*m22*m30 - m02*m21*m30 + m02*m20*m31 - m00*m22*m31 - m01*m20*m32 + m00*m21*m32,
			m02*m11*m30 - m01*m12*m30 - m02*m10*m31 + m00*m12*m31 + m01*m10*m32 - m00*m11*m32,
			m01*m12*m20 - m02*m11*m20 + m02*m10*m21 - m00*m12*m21 - m01*m10*m22 + m00*m11*m22
		);
	}

	public double determinant() {
		double value;
		value =
		   m03*m12*m21*m30 - m02*m13*m21*m30 - m03*m11*m22*m30 + m01*m13*m22*m30+
		   m02*m11*m23*m30 - m01*m12*m23*m30 - m03*m12*m20*m31 + m02*m13*m20*m31+
		   m03*m10*m22*m31 - m00*m13*m22*m31 - m02*m10*m23*m31 + m00*m12*m23*m31+
		   m03*m11*m20*m32 - m01*m13*m20*m32 - m03*m10*m21*m32 + m00*m13*m21*m32+
		   m01*m10*m23*m32 - m00*m11*m23*m32 - m02*m11*m20*m33 + m01*m12*m20*m33+
		   m02*m10*m21*m33 - m00*m12*m21*m33 - m01*m10*m22*m33 + m00*m11*m22*m33;
		return value;
	}
	
	public Mat4f oneOverMatrix() {
		return new Mat4f(
			1 / m00, 1 / m01, 1 / m02, 1 / m03,
			1 / m10, 1 / m11, 1 / m12, 1 / m13,
			1 / m20, 1 / m21, 1 / m22, 1 / m23,
			1 / m30, 1 / m31, 1 / m32, 1 / m33
		);
	}
	
	public static Mat4f rotateX(float deg) {
		float rad = (float) Math.toRadians(deg);
		return new Mat4f(
			1f, 0f, 0f, 0f,
			0f, (float) Math.cos(rad), (float) -Math.sin(rad), 0f,
			0f, (float) Math.sin(rad), (float) Math.cos(rad), 0f,
			0f, 0f, 0f, 1f
		);
	}
	
	public static Mat4f rotateY(float deg) {
		float rad = (float) Math.toRadians(deg);
		return new Mat4f(
			(float) Math.cos(rad), 0f, (float) Math.sin(rad), 0f,
			0f, 1f, 0f, 0f,
			(float) -Math.sin(rad), 0f, (float) Math.cos(rad), 0f,
			0f, 0f, 0f, 1f
		);
	}
	
	public static Mat4f rotateZ(float deg) {
		float rad = (float) Math.toRadians(deg);
		return new Mat4f(
			(float) Math.cos(rad), (float) -Math.sin(rad), 0f, 0f,
			(float) Math.sin(rad), (float) Math.cos(rad), 0f, 0f,
			0f, 0f, 1f, 0f,
			0f, 0f, 0f, 1f
		);
	}
	
	public static Mat4f rotate(Vec3f axis, float deg) {
		float rad = (float) Math.toRadians(deg);
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);
		float x = axis.x;
		float y = axis.y;
		float z = axis.z;
		float x2 = x * x;
		float y2 = y * y;
		float z2 = z * z;
		return new Mat4f(
			cos+x2*(1-cos),		x*y*(1-cos)-z*sin,	x*z*(1-cos)+y*sin,	0,
			y*x*(1-cos)+z*sin,	cos+y2*(1-cos),		y*z*(1-cos)-x*sin,	0,
			z*x*(1-cos)-y*sin,	z*y*(1-cos)+x*sin,	cos+z2*(1-cos),		0,
			0,					0,					0,					1
		);
	}
	
	public static Mat4f translate(float x, float y, float z) {
		return new Mat4f(
			1f, 0f, 0f, x,
			0f, 1f, 0f, y,
			0f, 0f, 1f, z,
			0f, 0f, 0f, 1f
		);
	}
	
	public static Mat4f scale(float x, float y, float z) {
		return new Mat4f(
			x, 0f, 0f, 0f,
			0f, y, 0f, 0f,
			0f, 0f, z, 0f,
			0f, 0f, 0f, 1f
		);
	}
	
	public static Mat4f flip(Mat4f mat) {
		Mat4f m = new Mat4f();
		m.m00 = mat.m00;
		m.m01 = mat.m10;
		m.m02 = mat.m20;
		m.m03 = mat.m30;
		m.m10 = mat.m01;
		m.m11 = mat.m11;
		m.m12 = mat.m21;
		m.m13 = mat.m31;
		m.m20 = mat.m02;
		m.m21 = mat.m12;
		m.m22 = mat.m22;
		m.m23 = mat.m32;
		m.m30 = mat.m03;
		m.m31 = mat.m13;
		m.m32 = mat.m23;
		m.m33 = mat.m33;
		return m;
	}
	
	public static Mat4f identity() {
		return new Mat4f(
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1
		);
	}
	
	public static Mat4f viewMatrix(Vec3f pos, Vec3f rot) {
		return Mat4f.rotateZ(-rot.z)
				.mul(Mat4f.rotateX(-rot.x))
				.mul(Mat4f.rotateY(-rot.y))
				.mul(Mat4f.translate(-pos.x, -pos.y, -pos.z));
	}
	
	public static Mat4f orthographic(float left, float right, float top, float bottom, float near, float far) {
		float yScale = (float) 1 / (right - left);
		float xScale = (float) 1 / (top - bottom);
		float zScale = (float) 1 / (far - near);
		float wScale = (far - near) / 2;
		
		return new Mat4f(
			xScale, 0,		0, 		0,
			0,		yScale, 0,		0,
			0,		0,		zScale, 0,
			0,		0,		0,		wScale
		);
	}
	
}
