package com.lukaseichberg.particle;

import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public class ParticleVS extends Shader {

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Vertex out = new Vertex(1, 0, 0);
		out.pos = in.pos.mul(uniform.getMat4f("mat"));
		out.vec2[0] = in.vec2[0].clone();
		return out;
	}

}
