package com.wolke7.ge2d;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Configuration{

	private static Map<String, String>			baseEntries	= new HashMap<>();
	private static Map<String, Configuration>	configs		= new HashMap<>();

	private Map<String, String>					subEntries	= new HashMap<>();

	private Configuration(String path){
		File f = Paths.get(path).toFile();

		if(!f.exists()){
			throw new ConfErr("Config file does not exist: " + f.getName());
		}

		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)))){
			String line;
			while((line = br.readLine()) != null){
				if(line.isEmpty()){
					continue;
				}

				String[] split = line.split(":");
				if(split.length != 2){
					throw new ConfErr("Invalid line: " + line);
				}
				subEntries.put(split[0].trim(), split[1].trim());
			}
		} catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	public static boolean loadConfig(){
		Path p = Paths.get("game.cfg");
		File f = p.toFile();

		if(!f.exists()){
			return false;
		}

		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)))){
			String line;
			while((line = br.readLine()) != null){
				if(line.isEmpty()){
					continue;
				}

				String[] split = line.split(":");
				if(split.length != 2){
					throw new ConfErr("Invalid line: " + line);
				}
				baseEntries.put(split[0].trim(), split[1].trim());
			}
		} catch(IOException e){
			throw new RuntimeException(e);
		}

		return true;
	}

	public static String getEntry(String key){
		String s = baseEntries.get(key);
		if(s == null || s.isEmpty()){
			throw new ConfErr("Entry not found: " + key);
		}
		return s;
	}

	public static String getEntry(String key, Configuration c){
		String s = c.subEntries.get(key);
		if(s == null || s.isEmpty()){
			throw new ConfErr("Entry not found: " + key);
		}
		return s;
	}

	public static String getEntry(String key, String c){
		Configuration cfg = getSubConfig(c);
		return getEntry(key, cfg);
	}

	public static Configuration getSubConfig(String key){
		Configuration c = configs.get(key);
		if(c != null){
			return c;
		}

		String path = getEntry(key);

		c = new Configuration(path);
		configs.put(key, c);
		return c;
	}

	public static String getFilePath(String key){
		String s = getEntry(key);
		return Paths.get(s).toAbsolutePath().toString();
	}

	public static String getFilePath(String key, Configuration c){
		String s = getEntry(key, c);
		return Paths.get(s).toAbsolutePath().toString();
	}

	public static String getFilePath(String key, String c){
		Configuration cfg = getSubConfig(c);
		return getFilePath(key, cfg);
	}

	public static boolean exists(String key){
		return baseEntries.get(key) != null;
	}

	public static boolean exists(String key, Configuration c){
		return c.subEntries.get(key) != null;
	}

	public static boolean exists(String key, String c){
		Configuration cfg = getSubConfig(c);
		return exists(key, cfg);
	}

	public static int getInt(String key){
		return Integer.parseInt(getEntry(key));
	}

	public static int getInt(String key, Configuration c){
		return Integer.parseInt(getEntry(key, c));
	}

	public static int getInt(String key, String c){
		return Integer.parseInt(getEntry(key, c));
	}

	public static boolean getBoolean(String key){
		return Boolean.parseBoolean(getEntry(key));
	}

	public static boolean getBoolean(String key, Configuration c){
		return Boolean.parseBoolean(getEntry(key, c));
	}

	public static boolean getBoolean(String key, String c){
		return Boolean.parseBoolean(getEntry(key, c));
	}

	public static Set<String> getEntries(){
		return baseEntries.keySet();
	}

	public static Set<String> getEntries(Configuration c){
		return c.subEntries.keySet();
	}

	public static Set<String> getEntries(String c){
		Configuration cfg = getSubConfig(c);
		return getEntries(cfg);
	}

}
