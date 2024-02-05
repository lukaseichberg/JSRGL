package com.lukaseichberg.demo.snow;

import com.lukaseichberg.maths.Maths;
import com.lukaseichberg.maths.Vec2f;
import com.lukaseichberg.maths.Vec4f;
import com.lukaseichberg.renderer.Renderer;
import com.lukaseichberg.structs.Vertex;

public class Terrain {
	
	private Vertex[] vertices;
	private int tilesX, tilesZ;
	private PerlinNoise noise;
	private int scale;
	
	public Terrain(int tilesX, int tilesZ, int scale) {
		this.tilesX = tilesX;
		this.tilesZ = tilesZ;
		vertices = new Vertex[tilesX * tilesZ * 6];
		noise = new PerlinNoise((long) (Math.random() * Long.MAX_VALUE));
		this.scale = scale;
	}
	
	public void generate() {
		for (int z = 0; z < tilesZ; z++) {
			for (int x = 0; x < tilesX; x++) {
				Vertex v0 = new Vertex(1, 0, 1);
				Vertex v1 = new Vertex(1, 0, 1);
				Vertex v2 = new Vertex(1, 0, 1);
				Vertex v3 = new Vertex(1, 0, 1);
				Vertex v4 = new Vertex(1, 0, 1);
				Vertex v5 = new Vertex(1, 0, 1);
				
				float xx = (float) x / tilesX;
				float zz = (float) z / tilesZ;

				float h00 = noise.getHeight(x, z, scale);
				float h01 = noise.getHeight(x + 1, z, scale);
				float h10 = noise.getHeight(x, z + 1, scale);
				float h11 = noise.getHeight(x + 1, z + 1, scale);

				Vec2f uv00 = new Vec2f(0, 0);
				Vec2f uv01 = new Vec2f(2, 0);
				Vec2f uv10 = new Vec2f(0, 2);
				Vec2f uv11 = new Vec2f(2, 2);

				float xOffset = 1f / tilesX;
				float zOffset = 1f / tilesZ;

				v0.pos = new Vec4f(xx, h00, zz, 1);
				v1.pos = new Vec4f(xx + xOffset, h01, zz, 1);
				v2.pos = new Vec4f(xx, h10, zz + zOffset, 1);
				v3.pos = new Vec4f(xx, h10, zz + zOffset, 1);
				v4.pos = new Vec4f(xx + xOffset, h01, zz, 1);
				v5.pos = new Vec4f(xx + xOffset, h11, zz + zOffset, 1);

				v0.vec2[0] = uv10;
				v1.vec2[0] = uv11;
				v2.vec2[0] = uv00;
				v3.vec2[0] = uv00;
				v4.vec2[0] = uv11;
				v5.vec2[0] = uv01;

				Vec4f n0 = new Vec4f(v2.pos.vec3f().sub(v0.pos.vec3f()).cross(v1.pos.vec3f().sub(v0.pos.vec3f())), 0);
				Vec4f n1 = new Vec4f(v5.pos.vec3f().sub(v3.pos.vec3f()).cross(v4.pos.vec3f().sub(v3.pos.vec3f())), 0);

				v0.vec4[0] = n0;
				v1.vec4[0] = n0;
				v2.vec4[0] = n0;
				v3.vec4[0] = n1;
				v4.vec4[0] = n1;
				v5.vec4[0] = n1;
				
				int offset = (x + z * tilesX) * 6;

				vertices[offset] = v0;
				vertices[offset + 1] = v2;
				vertices[offset + 2] = v1;
				vertices[offset + 3] = v3;
				vertices[offset + 4] = v5;
				vertices[offset + 5] = v4;
			}
		}
		System.out.println("Vertices: "+vertices.length);
	}
	
	public float getHeight(float x, float z) {
		float h00 = noise.getHeight((int) x, (int) z, scale);
		float h01 = noise.getHeight((int) x + 1, (int) z, scale);
		float h10 = noise.getHeight((int) x, (int) z + 1, scale);
		float h11 = noise.getHeight((int) x + 1, (int) z + 1, scale);
		float cOffX = (float) (x - Math.floor(x));
		float cOffZ = (float) (z - Math.floor(z));
		float h0 = Maths.lerp(h00, h10, cOffZ);
		float h1 = Maths.lerp(h01, h11, cOffZ);
		return Maths.lerp(h0, h1, cOffX);
	}
	
	public void render() {
		for (int i = 0; i < vertices.length; i += 3) {
			Renderer.renderTriangle(vertices[i], vertices[i + 1], vertices[i + 2]);
		}
	}

}
