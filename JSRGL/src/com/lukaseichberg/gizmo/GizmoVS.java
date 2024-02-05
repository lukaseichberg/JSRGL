package com.lukaseichberg.gizmo;

import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public class GizmoVS extends Shader {
	
	public Mat4f transform;
	
	public GizmoVS(Mat4f transform) {
		this.transform = transform;
	}

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Vertex out = in.clone();
		out.pos = in.pos.mul(transform);
		return out;
	}

}
