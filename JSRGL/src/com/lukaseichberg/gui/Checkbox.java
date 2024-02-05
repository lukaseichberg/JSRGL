package com.lukaseichberg.gui;

import com.lukaseichberg.maths.Vec3f;

public class Checkbox extends GUIComponent {
	
	private String text;
	private Boolean checked;
	
	public Checkbox(int x, int y, String text, Boolean checked) {
		super(x, y, GUIRenderer.getStringWidth(text), 8);
		this.text = text;
		this.checked = checked;
	}

	@Override
	void draw(int x, int y, int width, int height) {
		GUIRenderer.fillRect(x, y, 8, 8, new Vec3f(0, 0, 0.1f));
		if (checked) {
			GUIRenderer.fillRect(x + 2, y + 2, 4, 4, new Vec3f(0.25f, 0.25f, 0.5f));
		}
		GUIRenderer.drawString(x + 8, y, text);
	}

}
