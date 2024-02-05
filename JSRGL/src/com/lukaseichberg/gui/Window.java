package com.lukaseichberg.gui;

public class Window extends GUIComponent {
	
	private Titlebar titlebar;
	private Panel panel;
	
//	private int x, y;

//	private static Vec3f bgColor = new Vec3f(0.25f, 0.25f, 0.25f);
//	private static Vec3f borderColor = new Vec3f(0.75f, 0.75f, 0.75f);
	
	public Window(int x, int y, int width, int height, String title) {
		super(x, y, width, height);
		titlebar = new Titlebar(x, y, width, GUIRenderer.getStringHeight(title) + Titlebar.padding * 2, title);
		panel = new Panel(x, y + titlebar.getHeight(), width, height - titlebar.getHeight());
	}

	@Override
	void draw(int x, int y, int width, int height) {
//		int width = Math.max(titlebar.getWidth(), panel.getWidth());
		titlebar.draw(x, y);
		panel.draw(x, y);
//		GUIRenderer.fillRect(xOffset + x, yOffset + y, 8, 8, bgColor);
	}


	@Override
	public void add(GUIComponent component) {
		panel.add(component);
	}

	@Override
	public int getWidth() {
		int width = Math.max(titlebar.getWidth(), panel.getWidth());
		return width;
	}

	@Override
	public int getHeight() {
		int height = titlebar.getHeight() + panel.getHeight();
		return height;
	}

}
