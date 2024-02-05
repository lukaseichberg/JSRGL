package com.lukaseichberg.demo;

import java.util.ArrayList;
import java.util.List;

import com.lukaseichberg.mesh.Model;
import com.lukaseichberg.renderer.Renderer;
import com.lukaseichberg.shader.ShaderProgram;
import com.lukaseichberg.structs.Uniform;

public class SimpleEntityType {
	
	private Model model;
	private ShaderProgram shaderProgram;
	private List<SimpleEntity> entities;
	private Uniform uniform;
	
	public SimpleEntityType(Model model, ShaderProgram shaderProgram) {
		this.model = model;
		this.shaderProgram = shaderProgram;
		entities = new ArrayList<>();
		uniform = shaderProgram.getUniform();
	}
	
	public void render() {
		Renderer.bindShaderProgram(shaderProgram);
		for (SimpleEntity entity:entities) {
			uniform.setMat4f("model", entity.getTransformationMatrix());
			model.render();
		}
	}

}
