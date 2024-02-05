package com.lukaseichberg.gui;

import com.lukaseichberg.maths.Vec3f;

public class Titlebar extends GUIComponent {
	
	private static Vec3f bgColor = new Vec3f(0.25f, 0.25f, 0.25f);
	public static int padding = 2;
	
	private String title;

	public Titlebar(int x, int y, int width, int height, String title) {
		super(x, y, width, height);
		System.out.println(getHeight());
		this.title = title;
	}

	@Override
	void draw(int x, int y, int width, int height) {
		GUIRenderer.fillRect(x, y, width, height, bgColor);
		GUIRenderer.drawString(x + padding, y + padding, title);
	}

}
