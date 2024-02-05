package com.lukaseichberg.gui;

import com.lukaseichberg.maths.Vec2f;
import com.lukaseichberg.maths.Vec3f;

public class ColorPicker {

	private static Vec3f[] colorPalette = {
			new Vec3f(1, 0, 0),
			new Vec3f(1, 1, 0),
			new Vec3f(0, 1, 0),
			new Vec3f(0, 1, 1),
			new Vec3f(0, 0, 1),
			new Vec3f(1, 0, 1),
	};
	
	public static Vec2f selection = new Vec2f(0, 0);
	public static float value = 0;
	public static Vec3f c10 = getColor(value);
	public static Vec3f color = getColor(selection);
	
	public static int x = 10;
	public static int y = 10;
	
	public static Vec3f white = new Vec3f(1, 1, 1);
	
	public static void setMouseInput() {
//		GUIRenderer.display.canvas.addMouseListener(new MouseListener() {
//
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void mouseExited(MouseEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void mousePressed(MouseEvent arg0) {
//				int mouseX = arg0.getX() / GUIRenderer.display.getScale();
//				int mouseY = arg0.getY() / GUIRenderer.display.getScale();
//
//				if (mouseX >= x + 1 && mouseY >= y + 1 && mouseX < x + 102 && mouseY < y + 102) {
//					selection = new Vec2f(
//						(float) (mouseX - (x + 1)) / 100,
//						(float) (mouseY - (y + 1)) / 100
//					);
//					color = getColor(selection);
//				}
//				
//				if (mouseX >= x + 104 && mouseY >= y + 1 && mouseX < x + 130 && mouseY < y + 102) {
//					value = (float) (mouseY - (y + 1)) / 101;
//					c10 = getColor(value);
//					color = getColor(selection);
//				}
//			}
//
//			@Override
//			public void mouseReleased(MouseEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
//		
//		GUIRenderer.display.canvas.addMouseMotionListener(new MouseMotionListener() {
//			
//			@Override
//			public void mouseMoved(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void mouseDragged(MouseEvent e) {
//				int mouseX = e.getX() / GUIRenderer.display.getScale();
//				int mouseY = e.getY() / GUIRenderer.display.getScale();
//				
//				if (mouseX >= x + 1 && mouseY >= y + 1 && mouseX < x + 102 && mouseY < y + 102) {
//					selection = new Vec2f(
//						(float) (mouseX - (x + 1)) / 100,
//						(float) (mouseY - (y + 1)) / 100
//					);
//					color = getColor(selection);
//				}
//				
//				if (mouseX >= x + 104 && mouseY >= y + 1 && mouseX < x + 130 && mouseY < y + 102) {
//					value = (float) (mouseY - (y + 1)) / 101;
//					c10 = getColor(value);
//					color = getColor(selection);
//				}
//			}
//		});
	}
	
	public static void draw() {
		Vec3f c00 = new Vec3f(1, 1, 1);
		Vec3f c01 = new Vec3f(0, 0, 0);
		Vec3f c11 = new Vec3f(0, 0, 0);
		GUIRenderer.fillRect(x, y, 140, 104, new Vec3f(0.25f, 0.25f, 0.25f));
		for (int yy = 0; yy < 100; yy++) {
			float valueY = (float) yy / 100;
			Vec3f c0 = c00.lerp(c01, valueY);
			Vec3f c1 = c10.lerp(c11, valueY);
			for (int xx = 0; xx < 100; xx++) {
				float valueX = (float) xx / 100;
				Vec3f c = c0.lerp(c1, valueX);
				GUIRenderer.drawPixel(xx + x + 2, yy + y + 2, c);
			}
		}
		for (int yy = 0; yy < 100; yy++) {
			float steps = (float) 100 / colorPalette.length;
			int index = (int) (yy / steps) % colorPalette.length;
			float value = (float) (yy - steps * index) / steps;
			int nextIndex = (index + 1) % colorPalette.length;
			Vec3f c0 = colorPalette[index];
			Vec3f c1 = colorPalette[nextIndex];
			Vec3f c = c0.lerp(c1, value);
			GUIRenderer.fillRect(x + 104, yy + y + 2, 16, 1, c);
		}
		int xPos = (int) (100f * selection.x) + 1;
		int yPos = (int) (100f * selection.y) + 1;
		int yValue = (int) (100f * value) + 1;
		GUIRenderer.fillRect(x + 122, y + 2, 16, 16, color);
		GUIRenderer.fillRect(x + xPos, y + yPos, 2, 2, white);
		GUIRenderer.fillRect(x + 103, y + yValue, 3, 2, white);
		GUIRenderer.fillRect(x + 118, y + yValue, 3, 2, white);
	}
	
	private static Vec3f getColor(float value) {
		int yy = (int) (value * 100f);
		float steps = (float) 100 / colorPalette.length;
		int index = (int) (yy / steps) % colorPalette.length;
		float tvalue = (float) (yy - steps * index) / steps;
		int nextIndex = (index + 1) % colorPalette.length;
		Vec3f c0 = colorPalette[index];
		Vec3f c1 = colorPalette[nextIndex];
		Vec3f c = c0.lerp(c1, tvalue);
		return c;
	}
	
	private static Vec3f getColor(Vec2f selection) {
		Vec3f c00 = new Vec3f(1, 1, 1);
		Vec3f c01 = new Vec3f(0, 0, 0);
		Vec3f c11 = new Vec3f(0, 0, 0);
		
		Vec3f c0 = c00.lerp(c01, selection.y);
		Vec3f c1 = c10.lerp(c11, selection.y);
		
		Vec3f c = c0.lerp(c1, selection.x);
		
		return c;
	}

}
