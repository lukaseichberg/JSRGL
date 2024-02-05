package com.lukaseichberg.structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lukaseichberg.buffer.FrameBufferInterface;
import com.lukaseichberg.light.SpotLight;
import com.lukaseichberg.maths.Mat3f;
import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.texture.CubeMap;
import com.lukaseichberg.texture.Texture;
import com.lukaseichberg.texture.TextureAlpha;

public class Uniform {

	private Map<String, Integer> _int;
	private Map<String, Float> _float;
	private Map<String, Vec3f> vec3;
	private Map<String, Mat3f> mat3;
	private Map<String, Mat4f> mat4;
	private Map<String, Texture> textures;
	private Map<String, TextureAlpha> texturesAlpha;
	private Map<String, CubeMap> cubeMaps;
	@SuppressWarnings("rawtypes")
	private Map<String, FrameBufferInterface> buffers;
	
	public List<SpotLight> spotLights;
	
	public Uniform() {
		_int = new HashMap<>();
		_float = new HashMap<>();
		vec3 = new HashMap<>();
		mat3 = new HashMap<>();
		mat4 = new HashMap<>();
		textures = new HashMap<>();
		texturesAlpha = new HashMap<>();
		cubeMaps = new HashMap<>();
		buffers = new HashMap<>();
		
		spotLights = new ArrayList<>();
	}
	
	public void setInt(String key, int value) {
		_int.put(key, value);
	}
	
	public void setFloat(String key, float value) {
		_float.put(key, value);
	}
	
	public void setVec3f(String key, Vec3f vec) {
		vec3.put(key, vec);
	}
	
	public void setMat3f(String key, Mat3f mat) {
		mat3.put(key, mat);
	}
	
	public void setMat4f(String key, Mat4f mat) {
		mat4.put(key, mat);
	}
	
	public void setTexture(String key, Texture texture) {
		textures.put(key, texture);
	}
	
	public void setTextureAlpha(String key, TextureAlpha texture) {
		texturesAlpha.put(key, texture);
	}
	
	public void setCubeMap(String key, CubeMap cubeMap) {
		cubeMaps.put(key, cubeMap);
	}
	
	@SuppressWarnings("rawtypes")
	public void setBuffer(String key, FrameBufferInterface buffer) {
		buffers.put(key, buffer);
	}
	
	public int getInt(String key) {
		return _int.get(key);
	}
	
	public float getFloat(String key) {
		return _float.get(key);
	}
	
	public Vec3f getVec3f(String key) {
		return vec3.get(key);
	}
	
	public Mat3f getMat3f(String key) {
		return mat3.get(key);
	}
	
	public Mat4f getMat4f(String key) {
		return mat4.get(key);
	}
	
	public Texture getTexture(String key) {
		return textures.get(key);
	}
	
	public TextureAlpha getTextureAlpha(String key) {
		return texturesAlpha.get(key);
	}
	
	public CubeMap getCubeMap(String key) {
		return cubeMaps.get(key);
	}
	
	@SuppressWarnings("rawtypes")
	public FrameBufferInterface getBuffer(String key) {
		return buffers.get(key);
	}

}
