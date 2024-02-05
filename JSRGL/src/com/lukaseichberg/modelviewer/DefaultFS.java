package com.lukaseichberg.modelviewer;

import com.lukaseichberg.maths.Vec2f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.maths.Vec4f;
import com.lukaseichberg.sampler.Sampler;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;
import com.lukaseichberg.texture.TextureAlpha;

public class DefaultFS extends Shader {
	
	Vec3f lightDir = new Vec3f(-1, -0.5f, -0.75f).normal();

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Sampler<Vec4f> sampler = ModelViewer.sampler;
		Vertex out = new Vertex(0, 1, 0);
		TextureAlpha texture = uniform.getTextureAlpha("texture");
//		int tilesX = uniform.getInt("tilesX");
//		int tilesY = uniform.getInt("tilesY");
//		int index = uniform.getInt("texIndex");
//		float xOffset = (1.0f / tilesX) * (index % tilesX);
//		float yOffset = (float) ((1.0f / tilesY) * Math.floor(index / tilesX));
		
//		Vec2f uv = new Vec2f(
//			xOffset + in.vec2[0].x / tilesX,
//			yOffset + in.vec2[0].y / tilesY
//		);
		Vec2f uv = in.vec2[0].clone();
		if (texture == null) {
			out.vec3[0] = new Vec3f(1, 1, 1);
		} else {
			Vec4f color = sampler.sample(texture, uv);
			if (color.w < 0.5f) return null;
			out.vec3[0] = color.vec3f();
		}
		float brightness = 0;
		if (isBack) {
			brightness = lightDir.dot(in.vec3[0]) * 0.5f + 0.5f;
		} else {
			brightness = -lightDir.dot(in.vec3[0]) * 0.5f + 0.5f;
		}
		out.vec3[0] = out.vec3[0].mul(brightness);
		return out;
	}

}
