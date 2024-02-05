package com.lukaseichberg.camera;

import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.maths.Vec4f;

public class Camera {
	
	public Vec3f position;
	public Vec3f rotation;
	public Mat4f projection;
	
	public Camera(Vec3f pos, Vec3f rot) {
		this.position = pos;
		this.rotation = rot;
	}
	
	public Mat4f getViewMatrix() {
		return Mat4f.viewMatrix(position, rotation);
	}
	
	public Vec3f getViewDir() {
		Vec4f dir = new Vec4f(0, 0, 1, 0);
		return dir.mul(Mat4f.rotateZ(rotation.z).mul(Mat4f.rotateY(rotation.y).mul(Mat4f.rotateX(rotation.x)))).vec3f();
	}

	public void setProjectionMatrix(float fov, float aspect, float near, float far) {
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2))));
		float x_scale = y_scale / aspect;
//		float frustum_length = far - near;
		
		Mat4f projectionMatrix = Mat4f.identity();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		
		projection = projectionMatrix;
	}

	public void setOrthographicMatrix(float left, float right, float top, float bottom, float near, float far) {
		float y_scale = (float) 2 / (right - left);
		float x_scale = (float) 2 / (top - bottom);
		
		Mat4f matrix = Mat4f.identity();
		matrix.m00 = x_scale;
		matrix.m11 = y_scale;
		matrix.m03 = 0;
		matrix.m13 = 0;
		matrix.m22 = 0;
		matrix.m32 = 1;
		matrix.m23 = 1;
		matrix.m33 = (far - near) / 2;
		
		projection = matrix;
	}
	
	public Mat4f getProjectionViewMatrix() {
		return projection.mul(getViewMatrix());
	}

}
