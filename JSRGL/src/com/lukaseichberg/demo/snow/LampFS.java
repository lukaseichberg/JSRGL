package com.lukaseichberg.demo.snow;

import com.lukaseichberg.light.SpotLight;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;
import com.lukaseichberg.texture.Texture;

public class LampFS extends Shader {

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Vertex out = new Vertex(0, 1, 0);
		Texture texture = uniform.getTexture("modelTexture");
		Texture glowMap = uniform.getTexture("glowMap");
		float brightness = ((Vec3f) sample(glowMap, in.vec2[0])).x + 0.2f;
		for (SpotLight l:uniform.spotLights) {
			brightness += l.getBrightness(in.vec3[0], in.vec3[1].normal());
		}
		out.vec3[0] = ((Vec3f) sample(texture, in.vec2[0])).mul(brightness);
		return out;
	}

}
