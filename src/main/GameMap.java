package main;

import java.io.File;

//LOAD, STORE, AND ACCESS THE GAME MAP

import states.Game;

//LOAD, STORE, AND ACCESS THE GAME MAP

public class GameMap {

	public static int startX, startY;
	private Integer[] mapCheckpoints;
	
	public GameMap(String mapFile) {
		//LOAD MAP
		mapCheckpoints = Utils.loadMap(new File("data/maps/" + mapFile + ".csv"));
		
		//FIRST CHECKPOINT
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
