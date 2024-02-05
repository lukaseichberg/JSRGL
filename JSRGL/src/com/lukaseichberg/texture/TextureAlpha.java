package com.lukaseichberg.texture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.lukaseichberg.buffer.FrameBufferInterface;
import com.lukaseichberg.maths.Vec4f;

public class TextureAlpha implements FrameBufferInterface<Vec4f> {
	
	private Vec4f[] pixels;
	private int width, height;
	
	public TextureAlpha(String fileName) {
		File file = new File(fileName);
		try {
			BufferedImage img = ImageIO.read(file);
			byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();

			width = img.getWidth();
			height = img.getHeight();
			
			pixels = new Vec4f[width * height];
			
			if (img.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
				for (int y = 0; y < img.getHeight(); y++) {
					for (int x = 0; x < img.getWidth(); x++) {
						int index = (x + y * img.getWidth()) * 4;
						Vec4f color = new Vec4f(
							(float) (data[index + 3] & 0xFF) / 0xFF,
							(float) (data[index + 2] & 0xFF) / 0xFF,
							(float) (data[index + 1] & 0xFF) / 0xFF,
							(float) (data[index] & 0xFF) / 0xFF
						);
						pixels[x + y * width] = color;
					}
				}
			} else if (img.getType() == BufferedImage.TYPE_3BYTE_BGR) {
				for (int y = 0; y < img.getHeight(); y++) {
					for (int x = 0; x < img.getWidth(); x++) {
						int index = (x + y * img.getWidth()) * 3;
						Vec4f color = new Vec4f(
							(float) (data[index + 2] & 0xFF) / 0xFF,
							(float) (data[index + 1] & 0xFF) / 0xFF,
							(float) (data[index] & 0xFF) / 0xFF,
							1
						);
						pixels[x + y * width] = color;
					}
				}
			} else {
				System.err.println("Unknow image type: "+img.getType());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	private Vec4f colorToVec3(int color) {
//		float a = (float) ((color >> 24) & 0xFF) / 0xFF;
//		float r = (float) ((color >> 16) & 0xFF) / 0xFF;
//		float g = (float) ((color >> 8) & 0xFF) / 0xFF;
//		float b = (float) (color & 0xFF) / 0xFF;
//		return new Vec4f(r, g, b, a);
//	}

	@Override
	public void set(int x, int y, Vec4f color) {
		pixels[x + y * width] = color;
	}

	@Override
	public Vec4f get(int x, int y) {
		return pixels[x + y * width];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

}
