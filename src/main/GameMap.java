package main;

import java.io.File;

import states.Game;

public class GameMap {

	public static int startX, startY;
	private Integer[] mapCheckpoints;
	
	public GameMap(String mapFile) {
		mapCheckpoints = Utils.loadMap(new File("data/maps/" + mapFile + ".csv"));
		
		startX = mapCheckpoints[0] * Game.TILE_SIZE;
		startY = mapCheckpoints[1] * Game.TILE_SIZE;
	}
	
	public int getNumberOfCheckpoints() {
		return mapCheckpoints.length / 2;
	}
	
	public int getCheckpointX(int checkpoint) {
		return mapCheckpoints[checkpoint * 2] * Game.TILE_SIZE;
	}
	
	public int getCheckpointY(int checkpoint) {
		return mapCheckpoints[(checkpoint * 2) + 1] * Game.TILE_SIZE;
	}
	
}
