package com.lukaseichberg.demo.snow;

import java.util.ArrayList;
import java.util.List;

import com.lukaseichberg.mesh.Model;
import com.lukaseichberg.shader.ShaderProgram;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.texture.Texture;

public class EntityType {
	
	private Model model;
//	private ShaderProgram shaderProgram;
	private Texture texture;
	private List<Entity> entities;
	public boolean backfaceCulling = true;
	public boolean frontfaceCulling = false;
	
	public EntityType(Model model, Texture texture, ShaderProgram shaderProgram) {
		entities = new ArrayList<>();
		this.model = model;
//		this.shaderProgram = shaderProgram;
		this.texture = texture;
	}
	
	public void render(Uniform uniform) {
//		Renderer.bindShaderProgram(shaderProgram);
//		Renderer.backfaceCulling = backfaceCulling;
//		Renderer.frontfaceCulling = frontfaceCulling;
//		Uniform uniform = shaderProgram.getUniform();
		
		for (Entity e:entities) {
			uniform.setMat4f("modelMatrix", e.getTransform());
			uniform.setTexture("modelTexture", texture);
			model.render();
		}
	}
	
	public Entity create(float x, float y, float z, float rotX, float rotY, float rotZ, float scale) {
		Entity e = new Entity(x, y, z, rotX, rotY, rotZ, scale);
		entities.add(e);
		return e;
	}
	
	public void clear() {
		entities.clear();
	}

}
