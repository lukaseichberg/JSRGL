package com.lukaseichberg.display;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.lukaseichberg.buffer.ColorBuffer;
import com.lukaseichberg.buffer.DepthBuffer;
import com.lukaseichberg.buffer.FrameBufferInterface;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.texture.Texture;

public class Display {
	
	public JFrame frame;
	private BufferedImage image;
	private BufferStrategy bufferStrategy;
	private Graphics graphics;
	private int[] pixels;
	
	private int width, height, scale;
	private ColorBuffer colorBuffer;
	private DepthBuffer depthBuffer;
	
	public Canvas canvas;
	
	public boolean up, down, left, right, mb_left, space, shift, k;
	public int mouse_x, mouse_y;
	
	public Display(int width, int height, int scale, String title) {
		this.width 	= width;
		this.height = height;
		this.scale 	= scale;

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		colorBuffer = new ColorBuffer(width, height);
		depthBuffer = new DepthBuffer(width, height);
		
		pixels = (((DataBufferInt) image.getRaster().getDataBuffer()).getData());
		
		Dimension size = new Dimension(width * scale, height * scale);
		
		canvas = new Canvas();
		canvas.setPreferredSize(size);
		canvas.setMinimumSize(size);
		canvas.setMaximumSize(size);
		canvas.createBufferStrategy(1);
		
		canvas.requestFocus();
		canvas.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
					up = true;
					break;
				case KeyEvent.VK_S:
					down = true;
					break;
				case KeyEvent.VK_A:
					left = true;
					break;
				case KeyEvent.VK_D:
					right = true;
					break;
				case KeyEvent.VK_SPACE:
					space = true;
					break;
				case KeyEvent.VK_SHIFT:
					shift = true;
					break;
				case KeyEvent.VK_K:
					k = true;
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
					up = false;
					break;
				case KeyEvent.VK_S:
					down = false;
					break;
				case KeyEvent.VK_A:
					left = false;
					break;
				case KeyEvent.VK_D:
					right = false;
					break;
				case KeyEvent.VK_SPACE:
					space = false;
					break;
				case KeyEvent.VK_SHIFT:
					shift = false;
					break;
				case KeyEvent.VK_K:
					k = false;
					break;
				}
			}
			
		});
		canvas.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mb_left = false;
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mb_left = true;
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				mb_left = false;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		canvas.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mouse_x = e.getX();
				mouse_y = e.getY();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				mouse_x = e.getX();
				mouse_y = e.getY();
			}
		});

//		canvas.setCursor(canvas.getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), null));
		
		
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		bufferStrategy = canvas.getBufferStrategy();
		graphics = bufferStrategy.getDrawGraphics();
	}
	
	public void update() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Vec3f color = colorBuffer.get(x, y).clone();
				
				//	Clip values 0 - 1 
				color.x = color.x > 1 ? 1 : color.x < 0 ? 0 : color.x;
				color.y = color.y > 1 ? 1 : color.y < 0 ? 0 : color.y;
				color.z = color.z > 1 ? 1 : color.z < 0 ? 0 : color.z;
				
				//	Color Vec to Int
				int r = (int) (color.x * 0xFF);
				int g = (int) (color.y * 0xFF);
				int b = (int) (color.z * 0xFF);
				int c = r << 16 | g << 8 | b;
				pixels[y * width + x] = c;
			}
		}
		graphics.drawImage(image, 0, 0, width * scale, height * scale, null);
		bufferStrategy.show();
	}
	
	public Point getCanvasPos() {
		return canvas.getLocationOnScreen();
	}
	
	public int getCanvasWidth() {
		return canvas.getWidth();
	}
	
	public int getCanvasHeight() {
		return canvas.getHeight();
	}
	
	public void drawImage(int x, int y, FrameBufferInterface<Vec3f> texture) {
//		int startX = Math.max(-x, 0);
//		int startY = Math.max(-y, 0);
//		int width = Math.min(this.width - x, texture.getWidth());
//		int height = Math.min(this.height - y, texture.getHeight());
//		
//		for (int yy = startY; yy < height; yy++) {
//			for (int xx = startX; xx < width; xx++) {
//				colorBuffer.set(xx + x, yy + y, texture.get(xx, yy));
//			}
//		}

		for (int yy = 0; yy < texture.getHeight(); yy++) {
			for (int xx = 0; xx < texture.getWidth(); xx++) {
				colorBuffer.set(xx + x, yy + y, texture.get(xx, yy));
			}
		}
	}
	
	public void drawPixel(int x, int y, Vec3f c) {
		colorBuffer.set(x, y, c);
	}
	
	public void drawImage(int x, int y, int startX, int startY, int width, int height, Texture texture) {
		for (int yy = startY; yy < startY + height; yy++) {
			for (int xx = startX; xx < startX + width; xx++) {
				Vec3f color = texture.get(xx, yy);
				if (color.x != 1f || color.y != 0f || color.z != 1f) {
					colorBuffer.set(x + xx - startX, y + yy - startY, color);
				}
			}
		}
	}
	
	public void drawChar(int x, int y, int tileWidth, int tileHeight, char c, Texture texture) {
		int tileX = c % 16;
		int tileY = c / 16;
		drawImage(x, y, tileX * tileWidth, tileY * tileHeight, tileWidth, tileHeight, texture);
	}
	
	public void drawString(int x, int y, int tileWidth, int tileHeight, String text, Texture texture) {
		for (int i = 0; i < text.length(); i++) {
			drawChar(x + i * tileWidth, y, tileWidth, tileHeight, text.charAt(i), texture);
		}
	}
	
	public void fillRect(int x, int y, int w, int h, Vec3f c) {
		for (int yy = 0; yy < h; yy++) {
			for (int xx = 0; xx < w; xx++) {
				colorBuffer.set(xx + x, yy + y, c);
			}
		}
	}
	
	public void drawRect(int x, int y, int w, int h, Vec3f c) {
		fillRect(x, y, w, 1, c);
		fillRect(x, y + 1, 1, h - 2, c);
		fillRect(x + w - 1, y + 1, 1, h - 2, c);
		fillRect(x, y + h - 1, w, 1, c);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getScale() {
		return scale;
	}
	
	public ColorBuffer getColorBuffer() {
		return colorBuffer;
	}
	
	public DepthBuffer getDepthBuffer() {
		return depthBuffer;
	}

}
