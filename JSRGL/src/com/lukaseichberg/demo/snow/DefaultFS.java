package com.lukaseichberg.demo.snow;

import com.lukaseichberg.light.SpotLight;
import com.lukaseichberg.maths.Vec2f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.maths.Vec4f;
import com.lukaseichberg.shader.Shader;
import com.lukaseichberg.structs.Uniform;
import com.lukaseichberg.structs.Vertex;
import com.lukaseichberg.texture.Texture;
import com.lukaseichberg.texture.TextureAlpha;

public class DefaultFS extends Shader {
	
	Vec3f white = new Vec3f(0.02f, 0.02f, 0.02f);

	@Override
	public Vertex main(Vertex in, Uniform uniform) {
		Vertex out = new Vertex(0, 1, 0);
		Texture texture = uniform.getTexture("modelTexture");
//		float brightness = Math.max(-in.vec4[0].vec3f().normal().dot(uniform.getVec3f("lightDir").normal()), 0);
		float brightness = 0.15f;
		Vec4f worldPos = in.vec4[0];
		for (SpotLight l:uniform.spotLights) {
			brightness += l.getBrightness(worldPos.vec3f(), in.vec3[0].normal());
		}
		brightness = Math.min(brightness, 1);
		out.vec3[0] = ((Vec3f) sample(texture, in.vec2[0])).clone();
		
		for (int i = 0; i < uniform.getInt("decalCount"); i++) {
			Vec4f decal = worldPos.mul(uniform.getMat4f("decalMat["+i+"]"));
			if (decal.x > -1 && decal.x < 1 && decal.y > -1 && decal.y < 1 && decal.z > -1f && decal.z < 1f) {
				TextureAlpha decalTexture = uniform.getTextureAlpha("decalTexture");
				Vec2f decalUV = new Vec2f(
					decal.x * 0.5f + 0.5f,
					-decal.y * 0.5f + 0.5f
				);
				Vec4f decalColor = (Vec4f) sample(decalTexture, decalUV);
				out.vec3[0] = out.vec3[0].lerp(decalColor.vec3f(), decalColor.w);
			}
		}
		float fogStart = 5;
		float fogEnd = 15;
		float fog = (in.vec3[1].mag() - fogStart) / (fogEnd - fogStart);
		if (fog < 0) fog = 0;
		if (fog > 1) fog = 1;
		out.vec3[0]._mul(brightness);
		out.vec3[0] = out.vec3[0].lerp(white, fog);
		return out;
	}

}
