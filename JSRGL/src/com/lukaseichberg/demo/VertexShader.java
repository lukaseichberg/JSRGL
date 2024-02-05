package com.lukaseichberg.demo;

import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public class VertexShader extends Shader {
	
	public Mat4f transform;
	
	public VertexShader(Mat4f transform) {
		this.transform = transform;
	}

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Vertex out = in.clone();
		out.pos = in.pos.mul(transform);
		out.vec4[0] = in.vec4[0].mul(transform);
		return out;
	}

}
