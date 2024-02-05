package com.lukaseichberg.demo.snow;

import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec4f;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public class LampVS extends Shader {

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Vertex out = new Vertex(1, 2, 0);
		Mat4f modelMatrix = uniform.getMat4f("modelMatrix");
		Vec4f worldPos = in.pos.mul(modelMatrix);
		Vec4f worldNormal = in.vec4[0].mul(modelMatrix);
		out.pos = worldPos.mul(uniform.getMat4f("projViewMatrix"));
		out.vec2[0] = in.vec2[0].clone();
		out.vec3[0] = worldPos.vec3f();
		out.vec3[1] = worldNormal.vec3f();
		return out;
	}

}
