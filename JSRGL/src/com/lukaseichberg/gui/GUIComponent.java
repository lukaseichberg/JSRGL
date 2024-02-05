package com.lukaseichberg.gui;

import java.util.ArrayList;
import java.util.List;

public abstract class GUIComponent {
	
	private List<GUIComponent> children;
	public int x, y;
	public int width, height;
	public int padding;
	
	public GUIComponent(int x, int y, int width, int height) {
		children = new ArrayList<>();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void add(GUIComponent component) {
		children.add(component);
	}
	
	public void draw(int x, int y) {
		draw(x + this.x, y + this.y, width, height);
		drawChildren(x + this.x, y + this.y);
	}
	
	public void drawChildren(int x, int y) {
		for (GUIComponent child : children) {
			child.draw(x, y);
		}
	}
	
//	public void drawChildren(int xOffset, int yOffset) {
//		for (GUIComponent child : children) {
//			child.draw(xOffset + child.x, yOffset + child.y);
//		}
//	}
	
	public int getWidth() {
		return width + padding * 2;
	}
	
	public int getHeight() {
		return height + padding * 2;
	}
	
	abstract void draw(int x, int y, int width, int height);

}
