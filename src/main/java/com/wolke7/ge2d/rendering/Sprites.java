package com.wolke7.ge2d.rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprites{

	public String			path;
	public int				width;
	public int				height;
	public int[]			pixels;

	public static Sprites	DUMMYSPRITES	= new Sprites();

	private Sprites(){

	}

	public Sprites(String path, boolean internal){
		BufferedImage img = null;
		try{
			if(internal){
				img = ImageIO.read(Sprites.class.getResourceAsStream(path));
			} else{
				img = ImageIO.read(new File(path));
			}
		} catch(IOException e){
			throw new RuntimeException(e);
		}

		if(img == null){
			return;
		}

		this.path = path;
		this.width = img.getWidth();
		this.height = img.getHeight();

		pixels = img.getRGB(0, 0, width, height, null, 0, width);
	}

}
