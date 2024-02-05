package com.lukaseichberg.engine;

import java.util.ArrayList;
import java.util.List;

import com.lukaseichberg.demo.snow.EntityType;

public class Scene {
	
	private List<EntityType> entityTypes;
	
	public Scene() {
		entityTypes = new ArrayList<>();
	}
	
	public void add(EntityType entityType) {
		entityTypes.add(entityType);
	}
	
	public void render() {
//		for (EntityType entityType:entityTypes) {
////			entityType.render();
//		}
	}

}
