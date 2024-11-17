package com.wolke7.ge2d.level;

public abstract class Tile{

	public static final Tile[]	tiles		= new Tile[1024];

	// Builtin
	public static final Tile	DUMMY		= new DummyTile(0, 0, 0, 0, "DUMMY");
	public static final Tile	FACE		= new DummyTile(1, 1, 0, 0xFFFFD800, "FACE");
	public static final Tile	EYE			= new DummyTile(2, 2, 0, 0xFFFFFFFF, "EYE");
	public static final Tile	EYEBROW		= new DummyTile(3, 3, 0, 0xFF7F0000, "EYEBROW");
	public static final Tile	DARK_TEAR	= new DummyTile(4, 4, 0, 0xFF0026FF, "DARK_TEAR");
	public static final Tile	LIGHT_TEAR	= new DummyTile(5, 5, 0, 0xFF00FFFF, "LIGHT_TEAR");
	public static final Tile	MOUTH		= new DummyTile(6, 6, 0, 0xFFFF6A00, "MOUTH");

	public static final Tile	STONE		= new SolidTile(0, 0, 0, 0xFF555555, "STONE");
	public static final Tile	WATER		= new AnimatedTile(1, new int[][]{ { 0, 1}, { 1, 1}, { 2, 1}, { 1, 1}}, 0xFF4800FF, 1000, "WATER");
	public static final Tile	GRAVEL		= new BasicTile(2, 1, 0, 0xFF555544, "GRAVEL");
	public static final Tile	BLUE_STONE	= new SolidTile(3, 2, 0, 0xFF445555, "BLUESTONE");
	public static final Tile	BLACK_STONE	= new SolidTile(4, 3, 0, 0xFF554455, "BLACKSTONE");
	public static final Tile	BARRIER		= new SolidTile(5, 4, 0, 0xFF550000, "BARRIER");

	protected int				id;
	protected String			idName;
	protected boolean			solid;
	protected boolean			emitter;
	private int					levelColor;

	public Tile(int id, boolean isSolid, boolean isEmitter, int levelColor, String idName, boolean builtIn){
		if(!builtIn){
			id += 7;
		}
		this.id = id;
		if(tiles[id] != null){
			throw new RuntimeException("Duplicate tile id on " + id + ", " + idName);
		}

		this.solid = isSolid;
		this.emitter = isEmitter;
		this.levelColor = levelColor;
		tiles[id] = this;
		this.idName = idName;
	}

	public String getName(){
		return idName;
	}

	public int getId(){
		return id;
	}

	public boolean isSolid(){
		return solid;
	}

	public boolean isEmitter(){
		return emitter;
	}

	public abstract void tick();

	public abstract void render(int x, int y);

	public int getlevelColor(){
		return levelColor;
	}

}
