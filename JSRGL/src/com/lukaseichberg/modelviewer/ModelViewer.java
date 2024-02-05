package com.lukaseichberg.modelviewer;

import com.lukaseichberg.maths.Vec4f;
import com.lukaseichberg.mesh.FBXModelLoader;
import com.lukaseichberg.mesh.Model;
import com.lukaseichberg.mesh.OBJLoader;
import com.lukaseichberg.sampler.Sampler;
import com.lukaseichberg.texture.TextureAlpha;

public class ModelViewer {
	
	private static Model model;
	public static TextureAlpha texture;
	public static Sampler<Vec4f> sampler = new Sampler<>();
	public static Display display;

	public static void loadOBJModel(String file) {
		model = OBJLoader.load(file);
		display.setVertices(model.vertices.length);
	}
	
	public static void loadFBXModel(String file) {
		model = FBXModelLoader.load(file);
		display.setVertices(model.vertices.length);
	}
	
	public static void loadTexture(String file) {
		texture = new TextureAlpha(file);
	}
	
	public static void render() {
		if (model != null) {
			model.render();
		}
	}

}
