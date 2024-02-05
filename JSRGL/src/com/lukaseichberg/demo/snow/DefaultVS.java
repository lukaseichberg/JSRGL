package com.lukaseichberg.demo.snow;

import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec4f;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public class DefaultVS extends Shader {

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Vertex out = new Vertex(1, 2, 1);
		Mat4f modelMatrix = uniform.getMat4f("modelMatrix");
		Vec4f worldPos = in.pos.mul(modelMatrix);
		Vec4f worldNormal = in.vec4[0].mul(modelMatrix);
		out.pos = worldPos.mul(uniform.getMat4f("projViewMatrix"));
		float jitter = 50.0f;
		out.pos.x = (float) ((int) (out.pos.x * jitter)) / jitter;
		out.pos.y = (float) ((int) (out.pos.y * jitter)) / jitter;
		out.pos.z = (float) ((int) (out.pos.z * jitter)) / jitter;
		out.vec2[0] = in.vec2[0].clone();
		out.vec4[0] = worldPos;
		out.vec3[0] = worldNormal.vec3f();
		out.vec3[1] = out.pos.vec3f();
		
//		out.vec3[2] = worldPos.mul(uniform.getMat4f("decalMat")).vec3f();
		return out;
	}

}
