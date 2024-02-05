package com.lukaseichberg.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.lukaseichberg.buffer.FrameBufferInterface;
import com.lukaseichberg.maths.Vec3f;

public class Texture implements FrameBufferInterface<Vec3f> {
	
	private Vec3f[] pixels;
	private int width, height;
	
	public Texture(String fileName) {
		File file = new File(fileName);
		try {
			BufferedImage img = ImageIO.read(file);
			width = img.getWidth();
			height = img.getHeight();
			pixels = new Vec3f[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					pixels[x + y * width] = colorToVec3(img.getRGB(x, y));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Vec3f colorToVec3(int color) {
		float r = (float) ((color >> 16) & 0xFF) / 0xFF;
		float g = (float) ((color >> 8) & 0xFF) / 0xFF;
		float b = (float) (color & 0xFF) / 0xFF;
		return new Vec3f(r, g, b);
	}

	@Override
	public void set(int x, int y, Vec3f color) {
		pixels[x + y * width] = color;
	}

	@Override
	public Vec3f get(int x, int y) {
		return pixels[x + y * width];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

}
