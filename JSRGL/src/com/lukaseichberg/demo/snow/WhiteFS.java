package com.lukaseichberg.demo.snow;

import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public class WhiteFS extends Shader {

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Vertex out = new Vertex(0, 1, 0);
		out.vec3[0] = uniform.getCubeMap("cubeMap").get(in.vec3[0].normal());
		out.vec3[0]._add(new Vec3f(0.02f, 0.02f, 0.02f));
		return out;
	}

}
