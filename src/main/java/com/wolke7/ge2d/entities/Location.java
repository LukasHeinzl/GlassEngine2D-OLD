package com.wolke7.ge2d.entities;

public class Location{

	private int	x;
	private int	y;
	private int	xOffset;
	private int	yOffset;
	private Mob	mob;

	public Location(){
		this(0, 0, null);
	}

	public Location(int x, int y, Mob mob){
		this.x = x;
		this.y = y;
		this.mob = mob;
	}

	public Location(int x, int y){
		this.x = x;
		this.y = y;
	}

	public Location(int x, int y, int xOffset, int yOffset){
		this.x = x;
		this.y = y;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public int getX(){
		return x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getY(){
		return y;
	}

	public void setY(int y){
		this.y = y;
	}

	public Mob getMob(){
		return mob;
	}

	public void setMob(Mob mob){
		this.mob = mob;
	}

	public int getxOffset(){
		return xOffset;
	}

	public void setxOffset(int xOffset){
		this.xOffset = xOffset;
	}

	public int getyOffset(){
		return yOffset;
	}

	public void setyOffset(int yOffset){
		this.yOffset = yOffset;
	}

}
