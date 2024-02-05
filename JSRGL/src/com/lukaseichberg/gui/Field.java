package com.lukaseichberg.gui;

import com.lukaseichberg.display.Display;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.texture.Texture;

public class Field {
	
	private static int padding = 2;
	private static Vec3f bgColor = new Vec3f(0.25f, 0.25f, 0.25f);
	private static Texture texture = new Texture("res/font.png");
	
	private String text;
	
	public Field(String text) {
		this.text = text;
	}
	
	public int getWidth() {
		return text.length() * 8 + padding * 2;
	}
	
	public int getHeight() {
		return padding * 2 + 8;
	}
	
	public void draw(int x, int y, Display display) {
		display.fillRect(x, y, getWidth(), getHeight(), bgColor);
		display.drawString(x + padding, y + padding, 8, 8, text, texture);
	}

}
