package com.lukaseichberg.shader;

import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.sampler.Sampler;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;

public class ShaderProgram {

	private Shader vertexShader;
	private Shader fragmentShader;
	private Uniform uniform;
	
	public ShaderProgram(Shader vs, Shader fs, Uniform uniform) {
		vertexShader = vs;
		fragmentShader = fs;
		this.uniform = uniform;
	}
	
	public void setSampler(Sampler<Vec3f> sampler) {
		fragmentShader.setSampler(sampler);
	}
	
	public void setIsBack(boolean isBack) {
		fragmentShader.isBack = isBack;
	}
	
	public Uniform getUniform() {
		return uniform;
	}
	
	public Vertex processVertex(Vertex v) {
		return vertexShader.main(v, uniform);
	}
	
	public Vertex processFragment(Vertex v) {
		return fragmentShader.main(v, uniform);
	}
}
