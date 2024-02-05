package com.lukaseichberg.demo.snow;

import java.util.ArrayList;
import java.util.List;

import com.lukaseichberg.maths.Vec4f;

public class DecalHandler {
	
	private static List<Decal> decalList = new ArrayList<>();
	
	public static void add(Decal decal) {
		decalList.add(decal);
	}
	
	public static Vec4f sample(Vec4f worldPos) {
		Vec4f color = new Vec4f(0, 0, 0, 0);
//		for (Decal d:decalList) {
//			Vec4f decal = worldPos.mul(d.getMatrix());
//			if (decal.x > -1 && decal.x < 1 && decal.y > -1 && decal.y < 1 && decal.z > -1f && decal.z < 1f) {
//				TextureAlpha decalTexture = uniform.getTextureAlpha("decalTexture");
//				Vec2f decalUV = new Vec2f(
//					decal.x * 0.5f + 0.5f,
//					-decal.y * 0.5f + 0.5f
//				);
//				Vec4f decalColor = (Vec4f) sample(decalTexture, decalUV);
//				out.vec3[0] = out.vec3[0].lerp(decalColor.vec3f(), decalColor.w);
//			}
//		}
		return color;
	}

}
