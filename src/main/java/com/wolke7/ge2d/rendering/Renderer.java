package com.wolke7.ge2d.rendering;

import com.wolke7.ge2d.ConfErr;

public class Renderer{

	private static int[]		pixels;

	private static int			xOffset			= 0;
	private static int			yOffset			= 0;
	private static int			width;
	private static int			height;
	private static Sprites		sprites;

	public static final byte	BIT_MIRROR_X	= 0x01;
	public static final byte	BIT_MIRROR_Y	= 0x02;

	public static void init(int width, int height, Sprites sprites){
		Renderer.width = width;
		Renderer.height = height;
		Renderer.pixels = new int[width * height];

		if(sprites == null){
			throw new ConfErr("Sprites could not be loaded!");
		}

		Renderer.sprites = sprites;
	}

	public static void clearScreen(){
		Renderer.pixels = new int[Renderer.pixels.length];
	}

	public static void render(int xPos, int yPos, int tile, int mirrorDir, int scale, Sprites... sps){
		Sprites sprites = sps.length != 1 ? Renderer.sprites : sps[0];
		xPos -= xOffset;
		yPos -= yOffset;

		boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
		boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;
		int scaleMap = (scale - 1);

		int xTile = tile % 64;
		int yTile = tile / 64;
		int tileOffset = (xTile << 3) + (yTile << 3) * sprites.width;
		for(int y = 0; y < 8; y++){
			int ySheet = y;
			if(mirrorY){
				ySheet = 7 - y;
			}
			int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << 3) / 2);
			for(int x = 0; x < 8; x++){
				int xSheet = x;
				if(mirrorX){
					xSheet = 7 - x;
				}
				int xPixel = x + xPos + (x * scaleMap) - ((scaleMap << 3) / 2);
				int col = sprites.pixels[xSheet + ySheet * sprites.width + tileOffset];
				if(col != 0){
					for(int yScale = 0; yScale < scale; yScale++){
						if(yPixel + yScale < 0 || yPixel + yScale >= height){
							continue;
						}

						for(int xScale = 0; xScale < scale; xScale++){
							if(xPixel + xScale < 0 || xPixel + xScale >= width){
								continue;
							}
							pixels[(xPixel + xScale) + (yPixel + yScale) * width] = col;
						}
					}
				}
			}
		}
	}

	public static void setOffset(int xOffset, int yOffset){
		Renderer.xOffset = xOffset;
		Renderer.yOffset = yOffset;
	}

	public static void renderPixels(int[] toCopy){
		System.arraycopy(Renderer.pixels, 0, toCopy, 0, Renderer.pixels.length);
	}

	public static int getXOffset(){
		return Renderer.xOffset;
	}

	public static int getYOffset(){
		return Renderer.yOffset;
	}

	public static int getHeight(){
		return Renderer.height;
	}

	public static int getWidth(){
		return Renderer.width;
	}

}
