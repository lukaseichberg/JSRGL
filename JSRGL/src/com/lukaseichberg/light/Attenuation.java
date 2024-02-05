package com.lukaseichberg.light;

import com.lukaseichberg.maths.Vec3f;

public class Attenuation {
	
	public float constant;
	public float linear;
	public float quadratic;
	
	public Attenuation(float constant, float linear, float quadratic) {
		this.constant = constant;
		this.linear = linear;
		this.quadratic = quadratic;
	}
	
	public float calcAttenuation(Vec3f lightPos, Vec3f fragPos) {
		float distance = lightPos.sub(fragPos).mag();
		return 1.0f / (constant + linear * distance + quadratic * (distance * distance));
	}
	
	public static Attenuation distance7() {
		return new Attenuation(1.0f, 0.7f, 1.8f);
	}

}
