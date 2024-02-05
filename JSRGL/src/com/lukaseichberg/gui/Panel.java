package com.lukaseichberg.gui;

import com.lukaseichberg.maths.Vec3f;

public class Panel extends GUIComponent {

//	private static int padding = 2;
	private static Vec3f bgColor = new Vec3f(0.5f, 0.5f, 0.5f);
	
	public Panel(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	void draw(int x, int y, int width, int height) {
		GUIRenderer.fillRect(x, y, width, height, bgColor);
	}

}
