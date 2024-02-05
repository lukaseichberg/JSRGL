package com.lukaseichberg.maths;

public class Quaternion {
	
	public float x, y, z, w;
	
	public Quaternion() {
		x=y=z = 0;
		w = 1;
	}
	
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Quaternion(Vec3f v, float w) {
		x = v.x;
		y = v.y;
		z = v.z;
		this.w = w;
	}
	
	public Quaternion(Vec4f v) {
		x = v.x;
		y = v.y;
		z = v.z;
		w = v.w;
	}
	
	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
	}
	
	public Quaternion inverse() {
		return conjugate().div(norm());
	}
	
	public float norm() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public Quaternion product(Quaternion q) {
		return new Quaternion(
			y * q.z - z * q.y + x * q.w + w * q.x,
			z * q.x - x * q.z + y * q.w + w * q.y,
			x * q.y - y * q.x + z * q.w + w * q.z,
			w * q.w - x * q.x - y * q.y - z * q.z
		);
	}
	
	public Quaternion mul(Quaternion q) {
		return product(q);
	}
	
	public Quaternion mul(float scalar) {
		return new Quaternion(x * scalar, y * scalar, z * scalar, w * scalar);
	}
	
	public Quaternion add(Quaternion q) {
		return new Quaternion(x + q.x, y + q.y, z + q.z, w + q.w);
	}
	
	public Quaternion sub(Quaternion q) {
		return new Quaternion(x - q.x, y - q.y, z - q.z, w - q.w);
	}
	
	public Quaternion neg() {
		return new Quaternion(-x, -y, -z, -w);
	}
	
	public Quaternion div(float scalar) {
		return new Quaternion(x / scalar, y / scalar, z / scalar, w / scalar);
	}
	
	public Mat4f leftMatrix() {
		return new Mat4f(
			w, -z, y, x,
			z, w, -x, y,
			-y, x, w, z,
			-x, -y, -z, w
		);
	}
	
	public Mat4f rightMatrix() {
		return new Mat4f(
			w, -z, y, -x,
			z, w, -x, -y,
			-y, x, w, -z,
			x, y, z, w
		);
	}
	
	public Vec4f vec4f() {
		return new Vec4f(x, y, z, w);
	}

    public Mat4f rotationMatrix() {
      return new Mat4f(
        1f-2f*y*y-2f*z*z, 2f*x*y - 2f*z*w, 2f*x*z + 2f*y*w, 0,
        2f*x*y + 2f*z*w, 1f-2f*x*x-2f*z*z, 2f*y*z - 2f*x*w, 0,
        2f*x*z - 2f*y*w, 2f*y*z + 2f*x*w, 1f-2f*x*x-2f*y*y, 0,
        0, 0, 0, 1
      );
    }
	
}
