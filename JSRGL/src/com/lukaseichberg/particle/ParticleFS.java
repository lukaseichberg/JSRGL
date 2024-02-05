package com.lukaseichberg.particle;

import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;
import com.lukaseichberg.texture.Texture;

public class ParticleFS extends Shader {

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Vertex out = new Vertex(0, 1, 0);
		Texture img = uniform.getTexture("img");
		float intensity = uniform.getFloat("intensity");
		Vec3f color = (Vec3f) sample(img, in.vec2[0]);
		float maxValue = color.max();
		Vec3f gray = new Vec3f(maxValue, maxValue, maxValue);
		out.vec3[0] = gray.lerp(color, intensity).mul(intensity);
		return out;
	}

}
