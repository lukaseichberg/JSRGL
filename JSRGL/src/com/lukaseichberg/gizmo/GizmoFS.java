package com.lukaseichberg.gizmo;

import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public class GizmoFS extends Shader {

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		return in.clone();
	}

}
