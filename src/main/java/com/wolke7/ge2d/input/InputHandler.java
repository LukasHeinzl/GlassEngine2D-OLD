package com.wolke7.ge2d.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.wolke7.ge2d.Main;

public class InputHandler implements KeyListener{

	public static void init(Main main){
		main.addKeyListener(new InputHandler());
	}

	private static boolean[] keys = new boolean[65536];

	public static boolean isPressed(int code){
		return InputHandler.keys[code];
	}

	public static void setPressed(int code, boolean pressed){
		InputHandler.keys[code] = pressed;
	}

	public void keyPressed(KeyEvent e){
		InputHandler.keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e){
		InputHandler.keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e){

	}

}
