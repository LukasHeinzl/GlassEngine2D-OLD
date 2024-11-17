package com.wolke7.ge2d.level;

import com.wolke7.ge2d.rendering.Renderer;

public class BasicTile extends Tile{

	protected int tileId;

	public BasicTile(int id, int x, int y, int levelColor, String idName){
		super(id, false, false, levelColor, idName, false);
		this.tileId = x + y * 64;
	}

	@Override
	public void render(int x, int y){
		Renderer.render(x, y, tileId, 0x00, 1);
	}

	@Override
	public void tick(){

	}

}
