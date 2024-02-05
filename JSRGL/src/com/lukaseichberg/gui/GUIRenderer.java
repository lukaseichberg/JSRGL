package com.lukaseichberg.gui;

import java.util.ArrayList;
import java.util.List;

import com.lukaseichberg.display.Display;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.texture.Texture;

public class GUIRenderer {
	
	private static Texture font = new Texture("res/font.png");
	
	private static List<GUIComponent> components = new ArrayList<>();
	
	private static int charWidth = 8;
	private static int charHeight = 8;
	
	private static Display display;
	
	public static void setDisplay(Display display) {
		GUIRenderer.display = display;
	}
	
	public static void fillRect(int x, int y, int w, int h, Vec3f color) {
		display.fillRect(x, y, w, h, color);
	}
	
	public static void drawRect(int x, int y, int w, int h, Vec3f color) {
		display.drawRect(x, y, w, h, color);
	}
	
	public static void drawString(int x, int y, String string) {
		display.drawString(x, y, charWidth, charHeight, string, font);
	}
	
	public static void drawPixel(int x, int y, Vec3f color) {
		display.drawPixel(x, y, color);
	}
	
	public static int getStringWidth(String string) {
		return string.length() * charWidth;
	}
	
	public static int getStringHeight(String string) {
		return charHeight;
	}
	
	public static void add(GUIComponent component) {
		components.add(component);
	}
	
	public static void draw() {
		for (GUIComponent component : components) {
			component.draw(component.x, component.y, component.getWidth(), component.getHeight());
		}
	}

}
