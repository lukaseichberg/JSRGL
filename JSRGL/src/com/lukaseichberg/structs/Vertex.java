package com.lukaseichberg.structs;

import com.lukaseichberg.maths.Vec2f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.maths.Vec4f;

public class Vertex {
	
	public Vec4f pos;
	public Vec2f[] vec2;
	public Vec3f[] vec3;
	public Vec4f[] vec4;
	
	public Vertex(int vec2count, int vec3count, int vec4count) {
		vec2 = new Vec2f[vec2count];
		vec3 = new Vec3f[vec3count];
		vec4 = new Vec4f[vec4count];
	}

	public Vertex div(float scalar) {
		Vertex v = new Vertex(vec2.length, vec3.length, vec4.length);
		v.pos = pos.div(scalar);
		for (int i = 0; i < vec2.length; i++) {
			v.vec2[i] = vec2[i].div(scalar);
		}
		for (int i = 0; i < vec3.length; i++) {
			v.vec3[i] = vec3[i].div(scalar);
		}
		for (int i = 0; i < vec4.length; i++) {
			v.vec4[i] = vec4[i].div(scalar);
		}
		return v;
	}

	public Vertex mul(float scalar) {
		Vertex v = new Vertex(vec2.length, vec3.length, vec4.length);
		v.pos = pos.mul(scalar);
		for (int i = 0; i < vec2.length; i++) {
			v.vec2[i] = vec2[i].mul(scalar);
		}
		for (int i = 0; i < vec3.length; i++) {
			v.vec3[i] = vec3[i].mul(scalar);
		}
		for (int i = 0; i < vec4.length; i++) {
			v.vec4[i] = vec4[i].mul(scalar);
		}
		return v;
	}
	
	public void _div(float scalar) {
		pos._div(scalar);
		for (Vec2f v:vec2) v._div(scalar);
		for (Vec3f v:vec3) v._div(scalar);
		for (Vec4f v:vec4) v._div(scalar);
	}
	
	public void _mul(float scalar) {
		pos._mul(scalar);
		for (Vec2f v:vec2) v._mul(scalar);
		for (Vec3f v:vec3) v._mul(scalar);
		for (Vec4f v:vec4) v._mul(scalar);
	}
	
	public Vertex lerp(Vertex v, float value) {
		Vertex tmp = new Vertex(vec2.length, vec3.length, vec4.length);
		tmp.pos = pos.lerp(v.pos, value);
		for (int i = 0; i < vec2.length; i++) tmp.vec2[i] = vec2[i].lerp(v.vec2[i], value);
		for (int i = 0; i < vec3.length; i++) tmp.vec3[i] = vec3[i].lerp(v.vec3[i], value);
		for (int i = 0; i < vec4.length; i++) tmp.vec4[i] = vec4[i].lerp(v.vec4[i], value);
		return tmp;
	}
	
	public void swap(Vertex v) {
		pos.swap(v.pos);
		for (int i = 0; i < vec2.length; i++) vec2[i].swap(v.vec2[i]);
		for (int i = 0; i < vec3.length; i++) vec3[i].swap(v.vec3[i]);
		for (int i = 0; i < vec4.length; i++) vec4[i].swap(v.vec4[i]);
	}
	
	public Vertex clone() {
		Vertex tmp = new Vertex(vec2.length, vec3.length, vec4.length);
		tmp.pos = pos.clone();
		for (int i = 0; i < vec2.length; i++) tmp.vec2[i] = vec2[i].clone();
		for (int i = 0; i < vec3.length; i++) tmp.vec3[i] = vec3[i].clone();
		for (int i = 0; i < vec4.length; i++) tmp.vec4[i] = vec4[i].clone();
		return tmp;
	}

}
