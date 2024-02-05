package com.lukaseichberg.demo.snow;

import com.lukaseichberg.light.SpotLight;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public class SnowFS extends Shader {

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Vertex out = new Vertex(0, 1, 0);
		
		float brightness = 0.15f;
		for (SpotLight l:uniform.spotLights) {
			brightness += l.getBrightness(in.vec3[0]);
		}
		
		out.vec3[0] = new Vec3f(brightness, brightness, brightness);
		return out;
	}

}
