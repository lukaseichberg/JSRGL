package com.lukaseichberg.engine;

import java.util.ArrayList;
import java.util.List;

import com.lukaseichberg.mesh.Model;
import com.lukaseichberg.renderer.Renderer;
import com.lukaseichberg.shader.ShaderProgram;

public class Entity {
	
	private List<Component> components;
	private ShaderProgram shaderProgram;
	private Model model;
//	private Vec3f position;
//	private Vec3f rotation;
//	private float scale;
	
	public Entity() {
		components = new ArrayList<>();
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public void setShaderProgram(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
	}
	
	public void update() {
		for (Component component:components) {
			component.update(this);
		}
	}
	
	public void render() {
		if (model != null) {
//			Mat4f modelMatrix = 
//					Mat4f.scale(scale, scale, scale)
//					.mul(Mat4f.rotateX(rotation.x))
//					.mul(Mat4f.rotateY(rotation.y))
//					.mul(Mat4f.rotateZ(rotation.z))
//					.translate(position.x, position.y, position.z);
			
			Renderer.bindShaderProgram(shaderProgram);
			model.render();
		}
	}

}
