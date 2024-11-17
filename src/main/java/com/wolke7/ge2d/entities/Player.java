package com.wolke7.ge2d.entities;

import java.awt.event.KeyEvent;

import com.wolke7.ge2d.Main;
import com.wolke7.ge2d.input.InputHandler;
import com.wolke7.ge2d.level.Level;
import com.wolke7.ge2d.level.Tile;
import com.wolke7.ge2d.rendering.Renderer;

public class Player extends Mob{

	private int			scale		= 1;
	protected boolean	isSwimming	= false;
	private int			tickCount	= 0;
	private int			waterIdx, heartIdx, invMax;
	// private boolean cpui;

	public Player(int x, int y, int width, int height, int health, int maxHealth, int texX, int texY, int waterIdx, int heartIdx, boolean cpui,
			int invMax){
		super("Player", x, y, 2, width, height, health, maxHealth, texX, texY);
		this.waterIdx = waterIdx;
		this.heartIdx = heartIdx;
		// this.cpui = cpui;
		this.invMax = invMax;
	}

	public Tile getTileAtPlayer(){
		return Level.getTile(x >> 3, y >> 3);
	}

	@Override
	public boolean hasCollided(int xa, int ya){
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;
		for(int x = xMin; x < xMax; x++){
			if(isSolidTile(xa, ya, x, yMin)){
				return true;
			}
		}
		for(int x = xMin; x < xMax; x++){
			if(isSolidTile(xa, ya, x, yMax)){
				return true;
			}
		}
		for(int y = yMin; y < yMax; y++){
			if(isSolidTile(xa, ya, xMax, y)){
				return true;
			}
		}
		for(int y = xMin; y < yMax; y++){
			if(isSolidTile(xa, ya, xMin, y)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void tick(){
		speed = 2;
		int xa = 0;
		int ya = 0;
		xOffset = x - (Renderer.getWidth() / 2);
		yOffset = y - (Renderer.getHeight() / 2);

		if(health > 0){
			if(InputHandler.isPressed(KeyEvent.VK_W)){
				ya--;
			}
			if(InputHandler.isPressed(KeyEvent.VK_S)){
				ya++;
			}
			if(InputHandler.isPressed(KeyEvent.VK_D)){
				xa++;
			}
			if(InputHandler.isPressed(KeyEvent.VK_A)){
				xa--;
			}
		} else{
			xa -= x - 10;
			ya -= y - 50;
			health = maxHealth;
		}

		if(xa != 0 || ya != 0){
			move(xa, ya);
			isMoving = true;
		} else{
			isMoving = false;
		}

		if(Level.getTile(this.x >> 3, this.y >> 3).getId() == Tile.WATER.getId()){
			isSwimming = true;
		}
		if(isSwimming && Level.getTile(this.x >> 3, this.y >> 3).getId() != Tile.WATER.getId()){
			isSwimming = false;
		}
		tickCount++;
	}

	@Override
	public void render(){
		int xTile = super.textureXOffset;
		int yTile = super.textureYOffset;
		int walkingSpeed = 4;
		int flipTop = (stepCount >> walkingSpeed) & 1;
		int flipBottom = (stepCount >> walkingSpeed) & 1;

		if(movingDir == 1){
			xTile += 2;
		} else if(movingDir > 1){
			xTile += 4 + ((stepCount >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
		}

		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;

		if(isSwimming){
			int waterTileOffset = 0;
			yOffset += 4;
			if(tickCount % 60 < 15){
				waterTileOffset = 0;
			} else if(15 <= tickCount % 60 && tickCount % 60 < 30){
				waterTileOffset = 1;
				yOffset -= 1;
			} else if(30 <= tickCount % 60 && tickCount % 60 < 45){
				waterTileOffset = 2;
			} else{
				waterTileOffset = 1;
				yOffset -= 1;
			}
			Renderer.render(xOffset, yOffset + 3, waterTileOffset + waterIdx, 0x00, 1);
			Renderer.render(xOffset + 8, yOffset + 3, waterTileOffset + waterIdx, 0x01, 1);
		}

		Renderer.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 64, flipTop, scale);
		Renderer.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 64, flipTop, scale);

		if(!isSwimming){
			Renderer.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 64, flipBottom, scale);
			Renderer.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1) * 64, flipBottom, scale);
		}

		int rxo = Renderer.getXOffset();
		int ryo = Renderer.getYOffset();
		int rh = Renderer.getHeight();

		for(int i = 0; i < maxHealth; i++){
			if(i < health){
				Renderer.render(rxo + 8 * i, ryo, heartIdx + 1, 0x00, 1);
			} else{
				Renderer.render(rxo + 8 * i, ryo, heartIdx, 0x00, 1);
			}
		}

		for(int i = 0; i < invMax; i++){
			Renderer.render(rxo + 5 + 16 * i, ryo + rh - 13, 102, 0x00, 2, Main.getDefaultSprites());
		}
	}

	public void setGhost(boolean ghost){
		if(ghost){
			isGhost = true;
			speed = 4;
		} else{
			isGhost = false;
			speed = 2;
		}
	}
}
