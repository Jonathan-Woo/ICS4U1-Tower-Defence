package enemies;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;

import main.GameMap;
import main.Utils;
import states.Game;

public abstract class Enemy {
	//properties
	public static final int BASIC = 0, ARMORED = 1, QUICK = 2, SUMMONER = 3, BOSS = 4;
	public static int startX, startY;
	
	private int type;
	public int intxLocation;
	public int intyLocation;
	private int intDamage;
	private double intSpeed;
	private int intHealth;
	private BufferedImage enemyImage;
	
	private int checkpointX = -1, checkpointY = -1, currentCheckpoint = 0;
	
	//methods
	public void update(Game game) {
		if(checkpointX == -1 || checkpointY == -1) {
			checkpointX = game.map.getCheckpointX(currentCheckpoint);
			checkpointY = game.map.getCheckpointY(currentCheckpoint);
		}
		
		if(intxLocation == checkpointX && intyLocation == checkpointY) {
			//WE HAVE REACHED MAP CHECKPOINT
			if(game.map.getNumberOfCheckpoints() > currentCheckpoint + 1) {
				//GET NEXT CHECKPOINT
				currentCheckpoint++;
				checkpointX = game.map.getCheckpointX(currentCheckpoint);
				checkpointY = game.map.getCheckpointY(currentCheckpoint);
			}else {
				//ENEMY HAS REACHED THE END OF THE MAP
				game.enemies.remove(this);
				game.dealDamage(intDamage);
			}
		}else {
			//MOVE CLOSER TO CURRENT CHECKPOINT
			if(intxLocation < checkpointX) {
				intxLocation += intSpeed;
			}else if(intxLocation > checkpointX) {
				intxLocation -= intSpeed;
			}
			
			if(intyLocation < checkpointY) {
				intyLocation += intSpeed;
			}else if(intyLocation > checkpointY) {
				intyLocation -= intSpeed;
			}
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(enemyImage, intxLocation, intyLocation, null);
	}
	
	//constructor
	public Enemy(String enemyFile, int type) {
		this.type = type;
		Map<String, String> data = Utils.loadEnemy(enemyFile);
		this.intDamage = Integer.parseInt(data.get("damage"));
		this.intSpeed = Double.parseDouble(data.get("speed"));
		this.intHealth = Integer.parseInt(data.get("health"));
		this.enemyImage = Utils.loadImage("enemies/" + data.get("image"));
		
		this.intxLocation = GameMap.startX;
		this.intyLocation = GameMap.startY;
	}
	
}
