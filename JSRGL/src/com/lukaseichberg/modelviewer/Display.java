package com.lukaseichberg.modelviewer;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.lukaseichberg.buffer.ColorBuffer;
import com.lukaseichberg.buffer.DepthBuffer;
import com.lukaseichberg.buffer.FrameBufferInterface;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.renderer.Renderer;
import com.lukaseichberg.sampler.AddressingMode;
import com.lukaseichberg.sampler.FilteringMode;
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
	private JLabel fps;
	private JLabel vertices;
	
	public boolean up, down, left, right, mb_left, space, shift, k;
	public int mouse_x, mouse_y;
	public int mouseScroll;
	
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
		canvas.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				mouseScroll += e.getWheelRotation();
			}
		});

//		canvas.setCursor(canvas.getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), null));
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem model = new JMenuItem("OBJ Model");
		model.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem source = (JMenuItem) (e.getSource());
				
				if (source == model) {
					JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Wavefront", "obj");
					fileChooser.setFileFilter(filter);
					int returnVal = fileChooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						String filePath = fileChooser.getSelectedFile().getAbsolutePath();
						ModelViewer.loadOBJModel(filePath);
					}
				}
			}
		});
		menu.add(model);

		JMenuItem model1 = new JMenuItem("FBX Model");
		model1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem source = (JMenuItem) (e.getSource());
				
				if (source == model1) {
					JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("FBX File", "fbx");
					fileChooser.setFileFilter(filter);
					int returnVal = fileChooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						String filePath = fileChooser.getSelectedFile().getAbsolutePath();
						ModelViewer.loadFBXModel(filePath);
					}
				}
			}
		});
		menu.add(model1);
		
		JMenuItem texture = new JMenuItem("Texture");
		texture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem source = (JMenuItem) (e.getSource());
				
				if (source == texture) {
					JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("png", "png");
					fileChooser.setFileFilter(filter);
					int returnVal = fileChooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						String filePath = fileChooser.getSelectedFile().getAbsolutePath();
						ModelViewer.loadTexture(filePath);
					}
				}
			}
		});
		menu.add(texture);
		
		menuBar.add(menu);
		
		JPanel panel = new JPanel();
		JLabel res = new JLabel("Resolution: " + width + " x " + height);
		res.setMaximumSize(new Dimension(300, 30));
		res.setPreferredSize(new Dimension(300, 30));
		res.setMinimumSize(new Dimension(300, 30));
		panel.add(res);
		fps = new JLabel("FPS: ");
		fps.setMaximumSize(new Dimension(300, 30));
		fps.setPreferredSize(new Dimension(300, 30));
		fps.setMinimumSize(new Dimension(300, 30));
		panel.add(fps);
		vertices = new JLabel("Vertices: ");
		vertices.setMaximumSize(new Dimension(300, 30));
		vertices.setPreferredSize(new Dimension(300, 30));
		vertices.setMinimumSize(new Dimension(300, 30));
		panel.add(vertices);
//		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setMaximumSize(new Dimension(500, 1000));
		panel.setPreferredSize(new Dimension(400, 10));
		panel.setMinimumSize(new Dimension(100, 10));
		JCheckBox checkbox = new JCheckBox("Backface culling", true);
//		checkbox.setMaximumSize(new Dimension(300, 30));
//		checkbox.setPreferredSize(new Dimension(300, 30));
//		checkbox.setMinimumSize(new Dimension(300, 30));
		checkbox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Renderer.backfaceCulling = checkbox.isSelected();
			}
		});
		panel.add(checkbox);

		JCheckBox checkbox1 = new JCheckBox("Frontface culling", false);
		checkbox1.setMaximumSize(new Dimension(300, 30));
		checkbox1.setPreferredSize(new Dimension(300, 30));
		checkbox1.setMinimumSize(new Dimension(300, 30));
		checkbox1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Renderer.frontfaceCulling = checkbox1.isSelected();
			}
		});
		panel.add(checkbox1);
		

		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(2, 1));
		panel2.setMinimumSize(new Dimension(300, 50));
		panel2.setPreferredSize(new Dimension(300, 75));
		panel2.setMaximumSize(new Dimension(300, 500));
		Border border1 = BorderFactory.createTitledBorder("texture filtering");
		panel2.setBorder(border1);
		
		ButtonGroup filtering = new ButtonGroup();
		JRadioButton nearest = new JRadioButton("closest");
		nearest.setSelected(true);
		nearest.setMaximumSize(new Dimension(300, 30));
		nearest.setPreferredSize(new Dimension(300, 30));
		nearest.setMinimumSize(new Dimension(300, 30));
		filtering.add(nearest);
		panel2.add(nearest);
		
		nearest.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ModelViewer.sampler.setFilteringMode(FilteringMode.NEAREST_POINT);
			}
		});
		
		JRadioButton bilinear = new JRadioButton("bilinear");
		bilinear.setMaximumSize(new Dimension(300, 30));
		bilinear.setPreferredSize(new Dimension(300, 30));
		bilinear.setMinimumSize(new Dimension(300, 30));
		filtering.add(bilinear);
		panel2.add(bilinear);
		
		bilinear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ModelViewer.sampler.setFilteringMode(FilteringMode.BILINEAR);
			}
		});
		
		panel.add(panel2);
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(2, 1));
		panel1.setMinimumSize(new Dimension(300, 50));
		panel1.setPreferredSize(new Dimension(300, 75));
		panel1.setMaximumSize(new Dimension(300, 500));
		Border border = BorderFactory.createTitledBorder("texture wrapping");
		panel1.setBorder(border);
		
		ButtonGroup wrapping = new ButtonGroup();
		JRadioButton repeat = new JRadioButton("repeat");
		repeat.setSelected(true);
		wrapping.add(repeat);
		panel1.add(repeat);
		
		repeat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ModelViewer.sampler.setAddressingMode(AddressingMode.WRAP);
			}
		});
		
		JRadioButton clamp = new JRadioButton("clamp");
		wrapping.add(clamp);
		panel1.add(clamp);
		
		clamp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ModelViewer.sampler.setAddressingMode(AddressingMode.CLAMP);
			}
		});
		panel.add(panel1);
		
		
		

		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayout(2, 1));
		panel3.setMinimumSize(new Dimension(300, 50));
		panel3.setPreferredSize(new Dimension(300, 75));
		panel3.setMaximumSize(new Dimension(300, 500));
		Border border2 = BorderFactory.createTitledBorder("texture wrapping");
		panel3.setBorder(border2);
		
		ButtonGroup tiling = new ButtonGroup();
		JRadioButton tile = new JRadioButton("tile");
		tiling.add(tile);
		panel3.add(tile);

		JTextField tilesX = new JTextField();
		panel3.add(tilesX);
		JTextField tilesY = new JTextField();
		panel3.add(tilesY);
		JTextField texIndex = new JTextField();
		panel3.add(texIndex);
		
		tile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.tilesX = Integer.parseInt(tilesX.getText());
				Main.tilesY = Integer.parseInt(tilesY.getText());
				Main.texIndex = Integer.parseInt(texIndex.getText());
			}
		});
		
		JRadioButton full = new JRadioButton("full");
		full.setSelected(true);
		tiling.add(full);
		panel3.add(full);
		
		full.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.tilesX = 1;
				Main.tilesY = 1;
				Main.texIndex = 0;
			}
		});
		panel.add(panel3);
		/*
		JPanel position = new JPanel();
		position.setLayout(new GridLayout(2, 4));
		
		JLabel label = new JLabel("Position");
		label.setMaximumSize(new Dimension(70, 30));
		label.setPreferredSize(new Dimension(70, 30));
		label.setMinimumSize(new Dimension(70, 30));
		position.add(label);

		JTextField xField = new JTextField();
		xField.setMaximumSize(new Dimension(90, 30));
		xField.setMinimumSize(new Dimension(90, 30));
		xField.setPreferredSize(new Dimension(90, 30));
		position.add(xField);
		
		JTextField yField = new JTextField();
		yField.setText("Test");
		yField.setEnabled(false);
		yField.setMaximumSize(new Dimension(90, 30));
		yField.setMinimumSize(new Dimension(90, 30));
		yField.setPreferredSize(new Dimension(90, 30));
		position.add(yField);
		
		JTextField zField = new JTextField();
		zField.setMaximumSize(new Dimension(90, 30));
		zField.setMinimumSize(new Dimension(90, 30));
		zField.setPreferredSize(new Dimension(90, 30));
		position.add(zField);
		
		JLabel label1 = new JLabel("Rotation");
		label.setMaximumSize(new Dimension(70, 30));
		label.setPreferredSize(new Dimension(70, 30));
		label.setMinimumSize(new Dimension(70, 30));
		position.add(label1);

		JTextField xField1 = new JTextField();
		xField1.setMaximumSize(new Dimension(90, 30));
		xField1.setMinimumSize(new Dimension(90, 30));
		xField1.setPreferredSize(new Dimension(90, 30));
		position.add(xField1);
		
		JTextField yField1 = new JTextField();
		yField1.setMaximumSize(new Dimension(90, 30));
		yField1.setMinimumSize(new Dimension(90, 30));
		yField1.setPreferredSize(new Dimension(90, 30));
		position.add(yField1);
		
		JTextField zField1 = new JTextField();
		zField1.setMaximumSize(new Dimension(90, 30));
		zField1.setMinimumSize(new Dimension(90, 30));
		zField1.setPreferredSize(new Dimension(90, 30));
		position.add(zField1);
		
		panel.add(position);*/
		
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		frame.add(menuBar, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.WEST);
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		bufferStrategy = canvas.getBufferStrategy();
		graphics = bufferStrategy.getDrawGraphics();
	}
	
	public void setFPS(int fps) {
		this.fps.setText("FPS: " + fps);
	}
	
	public void setVertices(int vertices) {
		this.vertices.setText("Vertices: " + vertices);
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
