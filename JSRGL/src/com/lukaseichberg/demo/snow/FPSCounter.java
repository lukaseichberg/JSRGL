package com.lukaseichberg.demo.snow;

public class FPSCounter {

	private static int fps;
	private static int frames;
	private static long lastFrame;
	
	public static void init() {
		fps = 0;
		frames = 0;
		lastFrame = System.currentTimeMillis();
	}
	
	public static void tick() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastFrame >= 1000) {
			fps = frames;
			frames = 0;
			lastFrame = currentTime;
		} else {
			frames++;
		}
	}
	
	public static int getFPS() {
		return fps;
	}

}
