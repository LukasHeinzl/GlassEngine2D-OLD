package com.wolke7.ge2d.entities;

import com.wolke7.ge2d.level.Level;
import com.wolke7.ge2d.level.Tile;

public abstract class Mob extends Entity{

	protected float		speed;
	protected int		stepCount	= 0;
	protected boolean	isMoving;
	protected int		health, maxHealth;
	// 0 Up, 1, Down, 2 Left, 3 Right
	protected int		movingDir	= 1;
	protected boolean	isGhost		= false;
	protected boolean	active		= true;
	protected int		width;
	protected int		height;
	protected int		textureXOffset;
	protected int		textureYOffset;

	public Mob(String name, int x, int y, float speed, int width, int height, int health, int maxHealth, int textureXOffset, int textureYOffset){
		super(name);
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
		this.textureXOffset = textureXOffset;
		this.textureYOffset = textureYOffset;
		this.health = health;
		this.maxHealth = maxHealth;
	}

	protected boolean isSolidTile(int xa, int ya, int x, int y){
		if(isGhost){
			return false;
		}
		Tile lastTile = Level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
		Tile newTile = Level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
		if(!lastTile.equals(newTile) && newTile.isSolid()){
			return true;
		}
		return false;
	}

	public void setLocation(Location loc){
		this.x = loc.getX();
		this.y = loc.getY();
	}

	public Location getLocation(){
		return new Location(x, y, xOffset, yOffset);
	}

	public void move(int xa, int ya){
		if(xa != 0 && ya != 0){
			move(xa, 0);
			move(0, ya);
			stepCount--;
			return;
		}

		stepCount++;
		if(!hasCollided(xa, ya)){
			if(ya < 0){
				movingDir = 0;
			}
			if(ya > 0){
				movingDir = 1;
			}
			if(xa < 0){
				movingDir = 2;
			}
			if(xa > 0){
				movingDir = 3;
			}
			x += xa * speed;
			y += ya * speed;
		}
	}

	public abstract boolean hasCollided(int xa, int ya);

	public String getName(){
		return name;
	}

	public int getMovingDir(){
		return movingDir;
	}

	public void setMovingDir(int movingDir){
		this.movingDir = movingDir;
	}

	public void setStepCount(int stepCount){
		this.stepCount = stepCount;
	}

	public void setMoving(boolean moving){
		this.isMoving = moving;
	}

	public String getMovingDirName(){
		if(movingDir == 0){
			return "up";
		}
		if(movingDir == 1){
			return "down";
		}
		if(movingDir == 2){
			return "left";
		}
		if(movingDir == 3){
			return "right";
		}
		return "ERROR UNKNOWN DIR!";
	}

	public boolean collidedWithMob(Mob colliededMob){
		int cmX = colliededMob.x;
		int cmY = colliededMob.y;
		int mWidth = this.width;
		int mHeight = this.height;
		int mX = this.x;
		int mY = this.y;
		if(cmX >= mX && cmX <= mX + mWidth && cmY >= mY && cmY <= mY + mHeight){
			return true;
		}
		return false;
	}

	public int getHealth(){
		return health;
	}

	public void damage(int amount){
		if(getHealth() - amount >= 0){
			health -= amount;
		} else{
			health = 0;
		}
	}
}
