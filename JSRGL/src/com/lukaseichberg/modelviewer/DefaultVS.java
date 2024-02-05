package com.lukaseichberg.modelviewer;

import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public class DefaultVS extends Shader {

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Mat4f projView = uniform.getMat4f("projView");
		Vertex out = new Vertex(1, 1, 0);
		out.pos = in.pos.mul(projView);
		out.vec2[0] = in.vec2[0].clone();
		out.vec3[0] = in.vec4[0].vec3f();
		return out;
	}

}
