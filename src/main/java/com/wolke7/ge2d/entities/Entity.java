package com.wolke7.ge2d.entities;

import com.wolke7.ge2d.rendering.Renderer;

public abstract class Entity{

	protected int		x, y, xOffset, yOffset;
	protected String	name;

	public Entity(String name){
		this.name = name;
	}

	public abstract void tick();

	public abstract void render();

	public boolean isVisible(){
		if(x < Renderer.getXOffset() || x > Renderer.getXOffset() + Renderer.getWidth()){
			return false;
		}
		if(y < Renderer.getYOffset() || y > Renderer.getYOffset() + Renderer.getHeight()){
			return false;
		}
		return true;
	}

}
