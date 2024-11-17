package com.wolke7.ge2d.entities;

import com.wolke7.ge2d.level.Level;
import com.wolke7.ge2d.rendering.Renderer;

public class Projectile extends Mob{

	private static int	projectileCount	= 0;
	private int			damage, id;

	public Projectile(int x, int y, int movingDir, int damage){
		super("projectile:" + projectileCount, x, y, 2, 16, 16, 0, 0, 0, 0);
		this.movingDir = movingDir;
		this.id = projectileCount++;
		this.damage = damage;
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
		int xa = 0, ya = 0;
		if(movingDir == 0){
			ya--;
		} else if(movingDir == 1){
			ya++;
		} else if(movingDir == 2){
			xa--;
		} else{
			xa++;
		}
		move(xa, ya);

		for(Entity e: Level.getEntities()){
			if(e instanceof Mob && !(e instanceof Player)){
				if(collidedWithMob((Mob) e)){
					((Mob) e).damage(damage);
				}
			}
		}
	}

	@Override
	public void render(){
		int xTile = id;
		int yTile = 7;

		int xOffset = x - 4;
		int yOffset = y;

		Renderer.render(xOffset, yOffset, xTile + yTile * 32, 0x00, 1);
	}

}
