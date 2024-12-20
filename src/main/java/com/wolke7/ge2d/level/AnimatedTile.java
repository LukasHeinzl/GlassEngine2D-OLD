package com.wolke7.ge2d.level;

public class AnimatedTile extends BasicTile{

	private int[][]	animationTileCoords;
	private int		currentAnimationIndex;
	private long	lastIterationTime;
	private int		animationDelay;

	public AnimatedTile(int id, int[][] animationCoords, int levelColor, int animationDelay, String idName){
		super(id, animationCoords[0][0], animationCoords[0][1], levelColor, idName);
		this.animationTileCoords = animationCoords;
		this.currentAnimationIndex = 0;
		this.lastIterationTime = System.currentTimeMillis();
		this.animationDelay = animationDelay;
	}

	public void tick(){
		if((System.currentTimeMillis() - lastIterationTime) >= (animationDelay)){
			lastIterationTime = System.currentTimeMillis();
			currentAnimationIndex = (currentAnimationIndex + 1) % animationTileCoords.length;
			tileId = ((animationTileCoords[currentAnimationIndex][0]) + (animationTileCoords[currentAnimationIndex][1] * 64));
		}
	}

}
