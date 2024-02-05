package com.lukaseichberg.maths;

public class Vec4f implements LerpInterface<Vec4f> {
	
	public float x, y, z, w;
	
	public Vec4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vec4f(Vec3f v, float w) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = w;
	}

	public Vec4f lerp(LerpInterface<Vec4f> v, float value) {
		return new Vec4f(
			Maths.lerp(x, ((Vec4f) v).x, value),
			Maths.lerp(y, ((Vec4f) v).y, value),
			Maths.lerp(z, ((Vec4f) v).z, value),
			Maths.lerp(w, ((Vec4f) v).w, value)
		);
	}

	public Vec4f add(Vec4f v) {
		return new Vec4f(
			x + v.x,
			y + v.y,
			z + v.z,
			w + v.w
		);
	}
	
	public void _add(Vec4f v) {
		x += v.x;
		y += v.y;
		z += v.z;
		w += v.w;
	}

	public Vec4f sub(Vec4f v) {
		return new Vec4f(
			x - v.x,
			y - v.y,
			z - v.z,
			w - v.w
		);
	}
	
	public void _sub(Vec4f v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		w -= v.w;
	}

	public Vec4f mul(float scalar) {
		return new Vec4f(
			x * scalar,
			y * scalar,
			z * scalar,
			w * scalar
		);
	}

	public void _mul(float scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		w *= scalar;
	}
	
	public Vec4f mul(Mat4f mat) {
		return new Vec4f(
			x * mat.m00 + y * mat.m01 + z * mat.m02 + w * mat.m03,
			x * mat.m10 + y * mat.m11 + z * mat.m12 + w * mat.m13,
			x * mat.m20 + y * mat.m21 + z * mat.m22 + w * mat.m23,
			x * mat.m30 + y * mat.m31 + z * mat.m32 + w * mat.m33
		);
	}
	
	public Vec4f div(float scalar) {
		return new Vec4f(
			x / scalar,
			y / scalar,
			z / scalar,
			w / scalar
		);
	}
	
	public void _div(float scalar) {
		x /= scalar;
		y /= scalar;
		z /= scalar;
		w /= scalar;
	}
	
	public float mag() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public float dot(Vec4f v) {
		return x * v.x + y * v.y + z * v.z + w * v.w;
	}

	public Vec4f normal() {
		return div(mag());
	}

	public void normalize() {
		_div(mag());
	}
	
	public void swap(Vec4f v) {
		Vec4f tmpV = new Vec4f(x, y, z, w);
		x = v.x;
		y = v.y;
		z = v.z;
		w = v.w;
		v.x = tmpV.x;
		v.y = tmpV.y;
		v.z = tmpV.z;
		v.w = tmpV.w;
	}
	
	public Vec2f vec2f() {
		return new Vec2f(x, y);
	}
	
	public Vec3f vec3f() {
		return new Vec3f(x, y, z);
	}

	public Vec4f clone() {
		return new Vec4f(x, y, z, w);
	}
	
	public String toString() {
		return "Vec4("+x+", "+y+", "+z+", "+w+")";
	}
	
}
