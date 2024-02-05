package com.lukaseichberg.mesh;

import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.physics.Ray;
import com.lukaseichberg.renderer.Renderer;
import com.lukaseichberg.structs.Vertex;

public class Model {
	
	public Vertex[] vertices;
	
	public Model(int vertexCount) {
		vertices = new Vertex[vertexCount];
	}
	
	public void render() {
		int faces = vertices.length / 3;
		for (int i = 0; i < faces; i++) {
			Renderer.renderTriangle(vertices[i * 3], vertices[i * 3 + 1], vertices[i * 3 + 2]);
		}
	}
	
	public boolean isIntersecting(Ray ray, Mat4f transform) {
		for (int i = 0; i < vertices.length; i += 3) {
			Vec3f v0 = vertices[i].pos.mul(transform).vec3f();
			Vec3f v1 = vertices[i+1].pos.mul(transform).vec3f();
			Vec3f v2 = vertices[i+2].pos.mul(transform).vec3f();
			
			Vec3f n = v1.sub(v0).cross(v2.sub(v0)).normal();
			float d = v0.dot(n);
			
			Vec3f center = n.mul(d);

			float denom = n.dot(ray.dir.normal());
			if (denom > 0) {
				Vec3f p0l0 = center.sub(ray.origin);
				float t = p0l0.dot(n) / denom;
				
				if (t >= 0) {
					Vec3f p = ray.origin.add(ray.dir.normal().mul(t));
					Vec3f p0 = p.sub(v0).normal();
					Vec3f n0 = v1.sub(v0).cross(n);

					Vec3f p1 = p.sub(v1).normal();
					Vec3f n1 = v2.sub(v1).cross(n);

					Vec3f p2 = p.sub(v2).normal();
					Vec3f n2 = v0.sub(v2).cross(n);
					if (p0.dot(n0) < 0 && p1.dot(n1) < 0 && p2.dot(n2) < 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
