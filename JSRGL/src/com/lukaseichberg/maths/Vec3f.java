package com.lukaseichberg.maths;

public class Vec3f implements LerpInterface<Vec3f> {
	
	public float x, y, z;
	
	public Vec3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3f lerp(LerpInterface<Vec3f> v, float value) {
		return new Vec3f(
			Maths.lerp(x, ((Vec3f) v).x, value),
			Maths.lerp(y, ((Vec3f) v).y, value),
			Maths.lerp(z, ((Vec3f) v).z, value)
		);
	}

	public Vec3f add(Vec3f v) {
		return new Vec3f(
			x + v.x,
			y + v.y,
			z + v.z
		);
	}
	
	public void _add(Vec3f v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	public Vec3f sub(Vec3f v) {
		return new Vec3f(
			x - v.x,
			y - v.y,
			z - v.z
		);
	}
	
	public void _sub(Vec3f v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}

	public Vec3f mul(float scalar) {
		return new Vec3f(
			x * scalar,
			y * scalar,
			z * scalar
		);
	}

	public void _mul(float scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}
	
	public Vec3f mul(Mat3f mat) {
		return new Vec3f(
			x * mat.m00 + y * mat.m01 + z * mat.m02,
			x * mat.m10 + y * mat.m11 + z * mat.m12,
			x * mat.m20 + y * mat.m21 + z * mat.m22
		);
	}

	public Vec3f div(float scalar) {
		return new Vec3f(
			x / scalar,
			y / scalar,
			z / scalar
		);
	}
	
	public void _div(float scalar) {
		x /= scalar;
		y /= scalar;
		z /= scalar;
	}
	
	public float mag() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public float sqLen() {
		return x * x + y * y + z * z;
	}
	
	public float dot(Vec3f v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	public Vec3f cross(Vec3f v) {
		return new Vec3f(
			y * v.z - z * v.y,
			z * v.x - x * v.z,
			x * v.y - y * v.x
		);
	}

	public Vec3f normal() {
		return div(mag());
	}

	public void normalize() {
		_div(mag());
	}
	
	public void swap(Vec3f v) {
		Vec3f tmpV = new Vec3f(x, y, z);
		x = v.x;
		y = v.y;
		z = v.z;
		v.x = tmpV.x;
		v.y = tmpV.y;
		v.z = tmpV.z;
	}
	
	public float max() {
		return Math.max(x, Math.max(y, z));
	}
	
	public Vec3f clone() {
		return new Vec3f(x, y, z);
	}
	
	public String toString() {
		return "Vec3("+x+", "+y+", "+z+")";
	}

}
