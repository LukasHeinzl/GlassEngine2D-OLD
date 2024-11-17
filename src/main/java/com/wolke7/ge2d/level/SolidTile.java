package com.wolke7.ge2d.level;

public class SolidTile extends BasicTile{

	public SolidTile(int id, int x, int y, int levelColor, String idName){
		super(id, x, y, levelColor, idName);
		this.solid = true;
	}

}
