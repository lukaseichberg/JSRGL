package com.lukaseichberg.demo;

import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public class FragmentShader extends Shader {

	private Vec3f lightDir;
	private Vec3f lightDir2;
	public Vec3f color;
	public Vec3f color2;
	
	public FragmentShader() {
		lightDir = new Vec3f(1, 1, 1).normal();
		lightDir2 = new Vec3f(0, 0, 0).sub(lightDir);
		color = new Vec3f(1, 0.5f, 0);
		color2 = new Vec3f(0, 0.5f, 1);
	}

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Vertex out = new Vertex(0, 1, 0);
		out.pos = in.pos.clone();
		
		Vec3f normal = in.vec4[0].vec3f().normal();
		float brightness = Math.min(1, Math.max(0.1f, -lightDir.dot(normal)));
		float brightness2 = Math.min(1, Math.max(0.1f, -lightDir2.dot(normal)));
		
		out.vec3[0] = color.mul(brightness).add(color2.mul(brightness2));
		return out;
	}

}
