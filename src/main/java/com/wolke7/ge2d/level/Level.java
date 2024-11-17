package com.wolke7.ge2d.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.wolke7.ge2d.ConfErr;
import com.wolke7.ge2d.entities.Entity;
import com.wolke7.ge2d.rendering.Renderer;

public class Level{

	private static int[]			tiles;
	private static int				width;
	private static int				height;
	private static List<Entity>		entities	= new ArrayList<Entity>();
	private static BufferedImage	img;

	public static void loadLevelFromFile(String imgPath, boolean internal){
		if(imgPath.isEmpty()){
			throw new ConfErr("Empty level name!");
		}

		if(imgPath != null){
			try{
				if(internal){
					Level.img = ImageIO.read(Level.class.getResource(imgPath));
				} else{
					Level.img = ImageIO.read(new File(imgPath));
				}

				Level.width = img.getWidth();
				Level.height = img.getHeight();
				Level.tiles = new int[width * height];
				Level.loadTiles();
			} catch(IOException e){
				throw new RuntimeException(e);
			}
		}
	}

	public static List<Entity> getEntities(){
		return Level.entities;
	}

	public static void alterTile(int x, int y, Tile newTile){
		Level.tiles[x + y * width] = newTile.getId();
	}

	private static void loadTiles(){
		int[] tileColors = Level.img.getRGB(0, 0, Level.width, Level.height, null, 0, Level.width);
		for(int y = 0; y < Level.height; y++){
			for(int x = 0; x < Level.width; x++){
				tileCheck: for(Tile t: Tile.tiles){
					if(t != null && t.getlevelColor() == tileColors[x + y * width]){
						Level.tiles[x + y * width] = t.getId();
						break tileCheck;
					}
				}
			}
		}
	}

	public static void tick(){
		for(Entity e: entities){
			if(e != null && e.isVisible()){
				e.tick();
			}
		}

		for(Tile t: Tile.tiles){
			if(t == null){
				break;
			} else{
				t.tick();
			}
		}
	}

	public static void renderEntities(){
		for(Entity e: entities){
			if(e != null && e.isVisible()){
				e.render();
			}
		}
	}

	public static void renderTiles(int xOffset, int yOffset){
		if(xOffset < 0){
			xOffset = 0;
		}
		if(xOffset > (width << 3) - Renderer.getWidth()){
			xOffset = (width << 3) - Renderer.getWidth();
		}
		if(yOffset < 0){
			yOffset = 0;
		}
		if(yOffset > (height << 3) - Renderer.getHeight()){
			yOffset = (height << 3) - Renderer.getHeight();
		}

		Renderer.setOffset(xOffset, yOffset);

		for(int y = (yOffset >> 3); y < (yOffset + Renderer.getHeight() >> 3) + 1; y++){
			for(int x = (xOffset >> 3); x < (xOffset + Renderer.getWidth() >> 3) + 1; x++){
				getTile(x, y).render(x << 3, y << 3);
			}
		}
	}

	public static Tile getTile(int x, int y){
		if(x < 0 || x >= width || y < 0 || y >= height){
			return Tile.DUMMY;
		}
		return Tile.tiles[tiles[x + y * width]];
	}

	public static void addEntity(Entity entity){
		Level.entities.add(entity);
	}

	public static int getWidth(){
		return Level.width;
	}

	public static int getHeight(){
		return Level.height;
	}
}
