package com.wolke7.ge2d.sound;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound{

	private static Map<String, Sound>	sounds	= new HashMap<>();

	private Clip						clip;

	public static Sound loadSound(String path, boolean internal){
		Sound s = sounds.get(path);
		if(s != null){
			return s;
		}
		s = new Sound();
		try{
			AudioInputStream in = null;
			if(internal){
				in = AudioSystem.getAudioInputStream(Sound.class.getResource(path));
			} else{
				in = AudioSystem.getAudioInputStream(new File(path));
			}
			Clip clip = AudioSystem.getClip();
			clip.open(in);
			s.clip = clip;
		} catch(Exception e){
			throw new RuntimeException(e);
		}
		sounds.put(path, s);
		return s;
	}

	public void play(){
		if(clip != null){
			new Thread(){
				public void run(){
					synchronized(clip){
						clip.stop();
						clip.setFramePosition(0);
						clip.start();
					}
				}
			}.start();
		}
	}
}
