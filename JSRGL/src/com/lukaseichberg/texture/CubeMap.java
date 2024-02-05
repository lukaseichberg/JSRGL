package com.lukaseichberg.texture;

import java.util.ArrayList;
import java.util.List;

import com.lukaseichberg.buffer.ColorBuffer;
import com.lukaseichberg.buffer.FrameBufferInterface;
import com.lukaseichberg.maths.Vec2f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.sampler.Sampler;

public class CubeMap {

	public static final int TOP = 0;
	public static final int BOTTOM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int BACK = 4;
	public static final int FRONT = 5;
	
	private FrameBufferInterface<Vec3f>[] faces;
	private Sampler<Vec3f> sampler;
	
	@SuppressWarnings("unchecked")
	public CubeMap() {
		faces = new FrameBufferInterface[6];
	}
	
	public void setSampler(Sampler<Vec3f> sampler) {
		this.sampler = sampler;
	}
	
	public CubeMap(String directory, String format) {
		faces = new Texture[6];
		faces[TOP] = new Texture(directory+"/top."+format);
		faces[BOTTOM] = new Texture(directory+"/bottom."+format);
		faces[LEFT] = new Texture(directory+"/left."+format);
		faces[RIGHT] = new Texture(directory+"/right."+format);
		faces[BACK] = new Texture(directory+"/back."+format);
		faces[FRONT] = new Texture(directory+"/front."+format);
	}
	
	public void setFaceTexture(FrameBufferInterface<Vec3f> texture, int face) {
		faces[face] = texture;
	}
	
	public FrameBufferInterface<Vec3f> getFaceTexture(int face) {
		return faces[face];
	}
	
	public Vec3f get(Vec3f dir) {
		float max = Math.max(Math.max(Math.abs(dir.x), Math.abs(dir.y)), Math.abs(dir.z));
		Vec3f faceDir = dir.div(max);
		
		int face = 0;
		Vec2f uv = null;
		Vec3f color = new Vec3f(0, 0, 0);
		
		float pixelSize = (2f / faces[face].getWidth());

		List<Integer> samples = new ArrayList<>();
		List<Vec2f> uvs = new ArrayList<>();
		List<Float> weights = new ArrayList<>();
		
		if (faceDir.x <= -1f + pixelSize) {
			face = LEFT;
			uv = new Vec2f(faceDir.z, -faceDir.y);
			samples.add(face);
			uvs.add(uv);
			float weight = (-faceDir.x - (1f - pixelSize)) / pixelSize;
			weights.add(weight);
		}
		if (faceDir.x >= 1f - pixelSize) {
			face = RIGHT;
			uv = new Vec2f(-faceDir.z, -faceDir.y);
			samples.add(face);
			uvs.add(uv);
			float weight = (faceDir.x - (1f - pixelSize)) / pixelSize;
			weights.add(weight);
		}
		if (faceDir.y <= -1f + pixelSize) {
			face = BOTTOM;
			uv = new Vec2f(faceDir.x, -faceDir.z);
			samples.add(face);
			uvs.add(uv);
			float weight = (-faceDir.y - (1f - pixelSize)) / pixelSize;
			weights.add(weight);
		}
		if (faceDir.y >= 1f - pixelSize) {
			face = TOP;
			uv = new Vec2f(faceDir.x, faceDir.z);
			samples.add(face);
			uvs.add(uv);
			float weight = (faceDir.y - (1f - pixelSize)) / pixelSize;
			weights.add(weight);
		}
		if (faceDir.z <= -1f + pixelSize) {
			face = FRONT;
			uv = new Vec2f(-faceDir.x, -faceDir.y);
			samples.add(face);
			uvs.add(uv);
			float weight = (-faceDir.z - (1f - pixelSize)) / pixelSize;
			weights.add(weight);
		}
		if (faceDir.z >= 1f - pixelSize) {
			face = BACK;
			uv = new Vec2f(faceDir.x, -faceDir.y);
			samples.add(face);
			uvs.add(uv);
			float weight = (faceDir.z - (1f - pixelSize)) / pixelSize;
			weights.add(weight);
		}
		
		if (uv != null) {

			Vec3f sampledColor = new Vec3f(0, 0, 0);
			float totalWeight = 0;
			for (int i = 0; i < samples.size(); i++) {
			
				FrameBufferInterface<Vec3f> texture = faces[samples.get(i)];
				uv = uvs.get(i);
				uv.x = uv.x * 0.5f + 0.5f;
				uv.y = uv.y * 0.5f + 0.5f;
	
				if (sampler == null) {
					int x = (int) Math.min(texture.getWidth() - 1, (uv.x * texture.getWidth()));
					int y = (int) Math.min(texture.getHeight() - 1, (uv.y * texture.getHeight()));
					
					sampledColor._add(texture.get(x, y).mul(weights.get(i)));
				} else {
					sampledColor._add(sampler.sample(texture, uv).mul(weights.get(i)));
				}
				totalWeight += weights.get(i);
			}
			color = sampledColor.div(totalWeight);
		}
		
		return color;
	}
	
	public CubeMap getConv(int samples, float radius, int res) {
		CubeMap map = new CubeMap();
		
		ColorBuffer top = new ColorBuffer(res, res);
		for (int y = 0; y < res; y++) {
			for (int x = 0; x < res; x++) {
				Vec3f dir = new Vec3f(
					((float) x / res - 0.5f) * 2,
					1,
					((float) y / res - 0.5f) * 2
				);
				dir.normalize();
				

				Vec3f color = new Vec3f(0, 0, 0);
				for (int i = 0; i < samples; i++) {
					Vec3f offset = new Vec3f(
						(float) ((Math.random() - 0.5f) * radius),
						(float) ((Math.random() - 0.5f) * radius),
						(float) ((Math.random() - 0.5f) * radius)
					);
					color._add(get(offset.add(dir)));
				}
				color._div(samples);
				
				top.set(x, y, color);
			}
		}
		map.setFaceTexture(top, CubeMap.TOP);

		ColorBuffer bottom = new ColorBuffer(res, res);
		for (int y = 0; y < res; y++) {
			for (int x = 0; x < res; x++) {
				Vec3f dir = new Vec3f(
					((float) x / res - 0.5f) * 2,
					-1,
					((float) y / res - 0.5f) * 2
				);
				dir.z *= -1;
				dir.normalize();

				Vec3f color = new Vec3f(0, 0, 0);
				for (int i = 0; i < samples; i++) {
					Vec3f offset = new Vec3f(
						(float) ((Math.random() - 0.5f) * radius),
						(float) ((Math.random() - 0.5f) * radius),
						(float) ((Math.random() - 0.5f) * radius)
					);
					color._add(get(offset.add(dir)));
				}
				color._div(samples);
				
				bottom.set(x, y, color);
			}
		}
		map.setFaceTexture(bottom, CubeMap.BOTTOM);


		ColorBuffer left = new ColorBuffer(res, res);
		for (int y = 0; y < res; y++) {
			for (int x = 0; x < res; x++) {
				Vec3f dir = new Vec3f(
					-1,
					((float) y / res - 0.5f) * 2,
					((float) x / res - 0.5f) * 2
				);
				dir.y *= -1;
				dir.normalize();

				Vec3f color = new Vec3f(0, 0, 0);
				for (int i = 0; i < samples; i++) {
					Vec3f offset = new Vec3f(
						(float) ((Math.random() - 0.5f) * radius),
						(float) ((Math.random() - 0.5f) * radius),
						(float) ((Math.random() - 0.5f) * radius)
					);
					color._add(get(offset.add(dir)));
				}
				color._div(samples);
				
				left.set(x, y, color);
			}
		}
		map.setFaceTexture(left, CubeMap.LEFT);

		ColorBuffer right = new ColorBuffer(res, res);
		for (int y = 0; y < res; y++) {
			for (int x = 0; x < res; x++) {
				Vec3f dir = new Vec3f(
					1,
					((float) y / res - 0.5f) * 2,
					((float) x / res - 0.5f) * 2
				);
				dir.z *= -1;
				dir.y *= -1;
				dir.normalize();
				
				Vec3f color = new Vec3f(0, 0, 0);
				for (int i = 0; i < samples; i++) {
					Vec3f offset = new Vec3f(
						(float) ((Math.random() - 0.5f) * radius),
						(float) ((Math.random() - 0.5f) * radius),
						(float) ((Math.random() - 0.5f) * radius)
					);
					color._add(get(offset.add(dir)));
				}
				color._div(samples);
				
				right.set(x, y, color);
			}
		}
		map.setFaceTexture(right, CubeMap.RIGHT);

		ColorBuffer back = new ColorBuffer(res, res);
		for (int y = 0; y < res; y++) {
			for (int x = 0; x < res; x++) {
				Vec3f dir = new Vec3f(
					((float) x / res - 0.5f) * 2,
					((float) y / res - 0.5f) * 2,
					1
				);
				dir.y *= -1;
				dir.normalize();

				Vec3f color = new Vec3f(0, 0, 0);
				for (int i = 0; i < samples; i++) {
					Vec3f offset = new Vec3f(
						(float) ((Math.random() - 0.5f) * radius),
						(float) ((Math.random() - 0.5f) * radius),
						(float) ((Math.random() - 0.5f) * radius)
					);
					color._add(get(offset.add(dir)));
				}
				color._div(samples);
				
				back.set(x, y, color);
			}
		}
		map.setFaceTexture(back, CubeMap.BACK);

		ColorBuffer front = new ColorBuffer(res, res);
		for (int y = 0; y < res; y++) {
			for (int x = 0; x < res; x++) {
				Vec3f dir = new Vec3f(
					((float) x / res - 0.5f) * 2,
					((float) y / res - 0.5f) * 2,
					-1
				);
				dir.x *= -1;
				dir.y *= -1;
				dir.normalize();

				Vec3f color = new Vec3f(0, 0, 0);
				for (int i = 0; i < samples; i++) {
					Vec3f offset = new Vec3f(
						(float) ((Math.random() - 0.5f) * radius),
						(float) ((Math.random() - 0.5f) * radius),
						(float) ((Math.random() - 0.5f) * radius)
					);
					color._add(get(offset.add(dir)));
				}
				color._div(samples);
				
				front.set(x, y, color);
			}
		}
		map.setFaceTexture(front, CubeMap.FRONT);
		return map;
		
		
//		Vec3f color = new Vec3f(0, 0, 0);
//		for (int i = 0; i < samples; i++) {
//			Vec3f offset = new Vec3f(
//				(float) ((Math.random() - 0.5f) * radius),
//				(float) ((Math.random() - 0.5f) * radius),
//				(float) ((Math.random() - 0.5f) * radius)
//			);
//			color._add(get(offset.add(dir)));
//		}
//		color._div(samples);
//		return color;
	}

}
