package com.lukaseichberg.engine;

public class Engine {
	
	public Scene currentScene;
	
//	public void update() {
//		currentScene.update();
//	}
	
	public void render() {
		currentScene.render();
	}

}
