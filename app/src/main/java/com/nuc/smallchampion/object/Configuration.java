package com.nuc.smallchampion.object;

import android.content.Context;

public class Configuration {

	public static final int Lump_WIDTH = 40;
	public static final int Lump_HEIGHT = 40;
	public static int TIME1 = 120;
	public static int TIME2 = 100;
	public static int TIME3 = 140;
	private int xSize;
	private int ySize;
	private int beginImageX;
	private int beginImageY;
	private long gameTime;
	private Context context;

	public Configuration(int xSize, int ySize, int beginImageX,
			int beginImageY, long gameTime, Context context) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.beginImageX = beginImageX;
		this.beginImageY = beginImageY;
		this.gameTime = gameTime;
		this.context = context;
	}

	public long getGameTime() {
		return gameTime;
	}

	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public int getBeginImageX() {
		return beginImageX;
	}

	public int getBeginImageY() {
		return beginImageY;
	}

	public Context getContext() {
		return context;
	}
}
