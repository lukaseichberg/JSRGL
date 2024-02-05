package com.lukaseichberg.light;

import com.lukaseichberg.maths.Vec3f;

public class SpotLight {
	
	public Vec3f position;
	public Vec3f direction;
	public float innerCutOff, outerCutOff;
	
	public SpotLight(Vec3f position, Vec3f direction, float innerCutOff, float outerCutOff) {
		this.position = position;
		this.direction = direction.normal();
		this.innerCutOff = (float) Math.cos(Math.toRadians(innerCutOff));
		this.outerCutOff = (float) Math.cos(Math.toRadians(outerCutOff));
	}
	
	public float getBrightness(Vec3f pos, Vec3f normal) {
		Vec3f toLight = pos.sub(position);
		Vec3f lightDir = toLight.normal();
		float theta = lightDir.dot(direction);
		float epsilon = innerCutOff - outerCutOff;
		float intensity = Math.min(1.0f, Math.max((theta - outerCutOff) / epsilon, 0.0f));
		float brightness = Math.max(0, -lightDir.dot(normal));
		return intensity * brightness;
	}
	
	public float getBrightness(Vec3f pos) {
		Vec3f lightDir = pos.sub(position).normal();
		float theta = lightDir.dot(direction);
		float epsilon = innerCutOff - outerCutOff;
		float intensity = Math.min(1.0f, Math.max((theta - outerCutOff) / epsilon, 0.0f));
		return intensity;
	}

}
