package com.lukaseichberg.gui;

import com.lukaseichberg.maths.Vec3f;

public class Button extends GUIComponent {
	
	private static Vec3f bgColor = new Vec3f(0.7f, 0.7f, 0.7f);
	private static int padding = 2;
	
	private String text;

	public Button(int x, int y, String text) {
		super(x, y, GUIRenderer.getStringWidth(text) + padding * 2, GUIRenderer.getStringHeight(text) + padding * 2);
		this.text = text;
	}

	@Override
	void draw(int x, int y, int width, int height) {
		GUIRenderer.fillRect(x, y, getWidth(), getHeight(), bgColor);
		GUIRenderer.drawString(x + padding, y + padding, text);
	}

}
