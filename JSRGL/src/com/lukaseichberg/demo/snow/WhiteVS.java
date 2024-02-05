package com.lukaseichberg.demo.snow;

import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.maths.Vec4f;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public class WhiteVS extends Shader {

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Vertex out = new Vertex(0, 1, 0);
		Mat4f modelMatrix = uniform.getMat4f("modelMatrix");
		Vec4f worldPos = in.pos.mul(modelMatrix);
		Vec3f worldNormal = in.vec4[0].mul(modelMatrix).vec3f();
		Vec3f toCam = worldPos.vec3f().sub(uniform.getVec3f("camPos")).normal();
		Vec3f reflection = reflect(toCam, worldNormal);
		out.pos = worldPos.mul(uniform.getMat4f("projViewMatrix"));
		out.vec3[0] = reflection;
		return out;
	}

}
