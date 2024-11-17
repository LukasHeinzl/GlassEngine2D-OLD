package com.wolke7.ge2d.rendering;

public class Font{

	private static final String	chars	= "ABCDEFGHIJKLMNOPQRSTUVWXYZ���0123456789.,:;'\"!?$%()-=+/\\ []#*~<>|&abcdefghijklmnopqrstuvwxyz";
	private static Sprites	sps;

	public static void init(Sprites sps){
		Font.sps = sps;
	}

	public static void render(String msg, int x, int y, int scale){
		msg = msg.replace("\t", "   ");
		int xO = Renderer.getXOffset();
		int yO = Renderer.getYOffset();

		for(int i = 0; i < msg.length(); i++){
			int charIndex = chars.indexOf(msg.charAt(i));
			if(charIndex >= 0){
				Renderer.render(x + (i * 8) + xO, y + yO, charIndex + 7, 0x00, scale, sps);
			}
		}
	}

	public static void renderCentered(String msg, int scale){
		int x = (Renderer.getWidth() / 2) - 4 - 8 * (msg.length() / 2);
		int y = (Renderer.getHeight() / 2) - 4;

		render(msg, x, y, scale);
	}

	public static void renderCenteredX(String msg, int y, int scale){
		int x = (Renderer.getWidth() / 2) - 4 - 8 * (msg.length() / 2);

		render(msg, x, y, scale);
	}

	public static void renderCenteredY(String msg, int x, int scale){
		int y = (Renderer.getHeight() / 2) - 4;

		render(msg, x, y, scale);
	}

}
