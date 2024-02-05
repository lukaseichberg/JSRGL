package com.lukaseichberg.particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.mesh.Model;
import com.lukaseichberg.mesh.OBJLoader;
import com.lukaseichberg.renderer.Renderer;
import com.lukaseichberg.shader.ShaderProgram;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.texture.Texture;

public class ParticleSystem {
	
	private List<Particle> particles;
	private Model quad;
	private ShaderProgram sp;
	private Uniform uniform;
	
	public ParticleSystem(String texture) {
		particles = new ArrayList<>();
		quad = OBJLoader.load("res/quad.obj");
		uniform = new Uniform();
		sp = new ShaderProgram(new ParticleVS(), new ParticleFS(), uniform);
		uniform.setTexture("img", new Texture(texture));
	}
	
	public void add(Particle particle) {
		particles.add(particle);
	}
	
	public void update() {
		Iterator<Particle> iterator = particles.iterator();
		while (iterator.hasNext()) {
			Particle p = iterator.next();
			boolean alive = p.update();
			if (!alive) {
				iterator.remove();
			}
		}
	}
	
	public void clear() {
		particles.clear();
	}
	
	public void render(Mat4f view, Mat4f proj) {
		Renderer.bindShaderProgram(sp);
		for (Particle p:particles) {
			Vec3f pos = p.getPos();
			Mat4f modelMatrix = Mat4f.translate(pos.x, pos.y, pos.z);
			modelMatrix.m00 = view.m00;
			modelMatrix.m01 = view.m10;
			modelMatrix.m02 = view.m20;
			modelMatrix.m10 = view.m01;
			modelMatrix.m11 = view.m11;
			modelMatrix.m12 = view.m21;
			modelMatrix.m20 = view.m02;
			modelMatrix.m21 = view.m12;
			modelMatrix.m22 = view.m22;
			modelMatrix = modelMatrix.mul(Mat4f.rotateZ(p.getRotation()));
			modelMatrix = modelMatrix.mul(Mat4f.scale(p.getScale(), p.getScale(), p.getScale()));
			modelMatrix = view.mul(modelMatrix);
			modelMatrix = proj.mul(modelMatrix);
			uniform.setMat4f("mat", modelMatrix);
			uniform.setFloat("intensity", 1 - p.getLife());
			quad.render();
		}
	}
	
	public int getNumber() {
		return particles.size();
	}

}
