package com.lukaseichberg.maths;

public class Maths {
	
	public static float lerp(float f0, float f1, float value) {
		return f0 + (f1 - f0) * value;
	}
	
	public static float cosint(float f0, float f1, float value) {
		float v = (float) -Math.cos(Math.PI * value) * 0.5f + 0.5f;
		return lerp(f0, f1, v);
	}
	
	public static float cosintVal(float value) {
		return (float) -Math.cos(Math.PI * value) * 0.5f + 0.5f;
	}

}
