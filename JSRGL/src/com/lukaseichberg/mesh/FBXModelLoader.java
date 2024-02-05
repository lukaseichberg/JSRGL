package com.lukaseichberg.mesh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lukaseichberg.fbxloader.FBXFile;
import com.lukaseichberg.fbxloader.FBXLoader;
import com.lukaseichberg.maths.Vec2f;
import com.lukaseichberg.maths.Vec4f;
import com.lukaseichberg.structs.Vertex;

public class FBXModelLoader {
	
	public static Model load(String filePath) {
//		List<Vec3f> vertices = new ArrayList<>();
//		List<Vec4f> colors = new ArrayList<>();
//		List<Vec2f> textures = new ArrayList<>();
//		List<Vec3f> normals = new ArrayList<>();
//		List<Integer> vertexIndices = new ArrayList<>();
//		List<Integer> textureIndices = new ArrayList<>();
//		List<Integer> normalIndices = new ArrayList<>();
		
		FBXFile file = null;
		try {
			file = FBXLoader.loadFBXFile(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int[] polyIndex = (int[]) file.getRootNode().getNodeFromPath("Objects/Geometry/PolygonVertexIndex").getProperty(0).getData();
		double[] vertices = (double[]) file.getRootNode().getNodeFromPath("Objects/Geometry/Vertices").getProperty(0).getData();
		double[] normals = (double[]) file.getRootNode().getNodeFromPath("Objects/Geometry/LayerElementNormal/Normals").getProperty(0).getData();
		double[] uvs = (double[]) file.getRootNode().getNodeFromPath("Objects/Geometry/LayerElementUV/UV").getProperty(0).getData();
		int[] uvIndex = (int[]) file.getRootNode().getNodeFromPath("Objects/Geometry/LayerElementUV/UVIndex").getProperty(0).getData();

		List<Vertex> face = new ArrayList<>();
		List<Vertex> modelVertices = new ArrayList<>();
		boolean lastVertex = false;
		for (int i = 0; i < polyIndex.length; i++) {
			int index = polyIndex[i];
			if (index < 0) {
				index = ~index;
				lastVertex = true;
			}
			
			Vertex v = new Vertex(1, 0, 1);
			v.pos = getVertex(vertices, index);
			v.vec2[0] = getUV(uvs, uvIndex[i]);
			v.vec2[0].y = 1 - v.vec2[0].y;
			v.vec4[0] = getNormal(normals, index);
			face.add(v);
			
			if (lastVertex) {
				lastVertex = false;
				int tris = face.size() - 2;
				for (int j = 0; j < tris; j++) {
					modelVertices.add(face.get(0).clone());
					modelVertices.add(face.get(j + 1).clone());
					modelVertices.add(face.get(j + 2).clone());
				}
				face.clear();
			}
		}

		Model model = new Model(modelVertices.size());
		modelVertices.toArray(model.vertices);
		return model;
		
	}
	
	private static Vec4f getVertex(double[] data, int index) {
		return new Vec4f(
			(float) data[index * 3],
			(float) data[index * 3 + 1],
			(float) data[index * 3 + 2],
			1
		);
	}
	
	private static Vec4f getNormal(double[] data, int index) {
		return new Vec4f(
			(float) data[index * 3],
			(float) data[index * 3 + 1],
			(float) data[index * 3 + 2],
			0
		);
	}
	
	private static Vec2f getUV(double[] data, int index) {
		return new Vec2f(
			(float) data[index * 2],
			(float) data[index * 2 + 1]
		);
	}

}
