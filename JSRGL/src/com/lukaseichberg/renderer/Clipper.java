package com.lukaseichberg.renderer;

import java.util.ArrayList;
import java.util.List;

import com.lukaseichberg.structs.Vertex;

public class Clipper {
	
	public static List<Vertex> clipScreenSpace(List<Vertex> vertices) {
		List<Vertex> newVertices;
		newVertices = clipLeft(vertices);
		newVertices = clipBottom(newVertices);
		newVertices = clipRight(newVertices);
		newVertices = clipTop(newVertices);
		return newVertices;
	}

	private static List<Vertex> clipLeft(List<Vertex> vertices) {
		List<Vertex> tmp = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			int nextI = (i + 1) % vertices.size();
			
			Vertex v = vertices.get(i);
			Vertex nextV = vertices.get(nextI);
			
			if (v.pos.x < -1) {
				if (nextV.pos.x >= -1) {
					float value = (-1f - v.pos.x) / (nextV.pos.x - v.pos.x);
					tmp.add(v.lerp(nextV, value));
					tmp.add(nextV);
				}
			} else {
				if (nextV.pos.x >= -1) {
					tmp.add(nextV);
				} else {
					float value = (-1 - v.pos.x) / (nextV.pos.x - v.pos.x);
					tmp.add(v.lerp(nextV, value));
				}
			}
		}
		return tmp;
	}
	
	private static List<Vertex> clipRight(List<Vertex> vertices) {
		List<Vertex> tmp = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			int nextI = (i + 1) % vertices.size();
			
			Vertex v = vertices.get(i);
			Vertex nextV = vertices.get(nextI);
			
			if (v.pos.x > 1) {
				if (nextV.pos.x <= 1) {
					float value = (1f - v.pos.x) / (nextV.pos.x - v.pos.x);
					tmp.add(v.lerp(nextV, value));
					tmp.add(nextV);
				}
			} else {
				if (nextV.pos.x <= 1) {
					tmp.add(nextV);
				} else {
					float value = (1 - v.pos.x) / (nextV.pos.x - v.pos.x);
					tmp.add(v.lerp(nextV, value));
				}
			}
		}
		return tmp;
	}
	
	private static List<Vertex> clipBottom(List<Vertex> vertices) {
		List<Vertex> tmp = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			int nextI = (i + 1) % vertices.size();
			
			Vertex v = vertices.get(i);
			Vertex nextV = vertices.get(nextI);
			
			if (v.pos.y < -1) {
				if (nextV.pos.y >= -1) {
					float value = (-1f - v.pos.y) / (nextV.pos.y - v.pos.y);
					tmp.add(v.lerp(nextV, value));
					tmp.add(nextV);
				}
			} else {
				if (nextV.pos.y >= -1) {
					tmp.add(nextV);
				} else {
					float value = (-1 - v.pos.y) / (nextV.pos.y - v.pos.y);
					tmp.add(v.lerp(nextV, value));
				}
			}
		}
		return tmp;
	}
	
	private static List<Vertex> clipTop(List<Vertex> vertices) {
		List<Vertex> tmp = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			int nextI = (i + 1) % vertices.size();
			
			Vertex v = vertices.get(i);
			Vertex nextV = vertices.get(nextI);
			
			if (v.pos.y > 1) {
				if (nextV.pos.y <= 1) {
					float value = (1f - v.pos.y) / (nextV.pos.y - v.pos.y);
					tmp.add(v.lerp(nextV, value));
					tmp.add(nextV);
				}
			} else {
				if (nextV.pos.y <= 1) {
					tmp.add(nextV);
				} else {
					float value = (1 - v.pos.y) / (nextV.pos.y - v.pos.y);
					tmp.add(v.lerp(nextV, value));
				}
			}
		}
		return tmp;
	}
	
	public static List<Vertex> clipZ(List<Vertex> vertices, float near, float far) {
		List<Vertex> newVertices = new ArrayList<>();
		newVertices = clipZNear(vertices, near);
		newVertices = clipZFar(newVertices, far);
		return newVertices;
	}

	
	private static List<Vertex> clipZNear(List<Vertex> vertices, float near) {
		List<Vertex> tmp = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			int nextI = (i + 1) % vertices.size();
			
			Vertex v = vertices.get(i);
			Vertex nextV = vertices.get(nextI);
			
			if (v.pos.z < near) {
				if (nextV.pos.z >= near) {
					float value = (near - v.pos.z) / (nextV.pos.z - v.pos.z);
					tmp.add(v.lerp(nextV, value));
					tmp.add(nextV);
				}
			} else {
				if (nextV.pos.z >= near) {
					tmp.add(nextV);
				} else {
					float value = (near - v.pos.z) / (nextV.pos.z - v.pos.z);
					tmp.add(v.lerp(nextV, value));
				}
			}
		}
		return tmp;
	}

	private static List<Vertex> clipZFar(List<Vertex> vertices, float far) {
		List<Vertex> tmp = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			int nextI = (i + 1) % vertices.size();
			
			Vertex v = vertices.get(i);
			Vertex nextV = vertices.get(nextI);
			
			if (v.pos.z > far) {
				if (nextV.pos.z <= far) {
					float value = (far - v.pos.z) / (nextV.pos.z - v.pos.z);
					tmp.add(v.lerp(nextV, value));
					tmp.add(nextV);
				}
			} else {
				if (nextV.pos.z <= far) {
					tmp.add(nextV);
				} else {
					float value = (far - v.pos.z) / (nextV.pos.z - v.pos.z);
					tmp.add(v.lerp(nextV, value));
				}
			}
		}
		return tmp;
	}

}
