package com.lukaseichberg.demo.snow;

import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public class SnowVS extends Shader {

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Vertex out = new Vertex(0, 1, 0);
		out.pos = in.pos.mul(uniform.getMat4f("projViewMatrix"));
		out.vec3[0] = in.pos.vec3f();
		return out;
	}

}
