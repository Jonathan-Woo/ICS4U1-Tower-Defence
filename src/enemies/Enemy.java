package enemies;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import main.GameMap;
import main.Utils;
import networking.Connections;
import states.Game;
import towers.Tower;

public abstract class Enemy {
	//properties
	public static final int BASIC = 0, ARMORED = 1, QUICK = 2, SUMMONER = 3, BOSS = 4;
	public static int startX, startY;
	
	private int type;
	public String id;
	public int intxLocation;
	public int intyLocation;
	private int intDamage;
	public double intSpeed;
	public int intHealth;
	private BufferedImage enemyImage;
	private int reward;
	
	private int checkpointX = -1, checkpointY = -1;
	public int currentCheckpoint = 0;
	private double rotationAngle;
	
	public boolean FIRE_EFFECT = false, ICE_EFFECT = false;
	
	public void dealDamage(int intDamage) {
		this.intHealth -= intDamage;
		if(this.intHealth <= 0) {
			if(Connections.isServer) {
				Connections.sendMessage(Connections.REMOVE_ENEMY, this.id);
			}
			Game.removeEnemies.add(this);
		}
	}	

	public int getReward() {
		return this.reward;
	}
	
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
				if(Connections.isServer) {
					Connections.sendMessage(Connections.UPDATE_ENEMY, this.id, currentCheckpoint);
				}
				currentCheckpoint++;
				checkpointX = game.map.getCheckpointX(currentCheckpoint);
				checkpointY = game.map.getCheckpointY(currentCheckpoint);
			}else {
				//ENEMY HAS REACHED THE END OF THE MAP
				if(Connections.isServer) {
					Connections.sendMessage(Connections.REMOVE_ENEMY, this.id);
				}
				Game.removeEnemies.add(this);
				game.dealDamage(intDamage);
			}
		}else {
			//MOVE CLOSER TO CURRENT CHECKPOINT
			if(intxLocation < checkpointX) {
				intxLocation += intSpeed;
				rotationAngle = 0;
			}else if(intxLocation > checkpointX) {
				intxLocation -= intSpeed;
				rotationAngle = Math.PI;
			}
			
			if(intyLocation < checkpointY) {
				intyLocation += intSpeed;
				rotationAngle = (Math.PI / 2) * 3;
			}else if(intyLocation > checkpointY) {
				intyLocation -= intSpeed;
				rotationAngle = Math.PI / 2;
			}
		}
	}
	
	public void render(Graphics g) {
		((Graphics2D) g).rotate(rotationAngle, intxLocation + (Game.TILE_SIZE / 2), intyLocation + (Game.TILE_SIZE / 2));
		g.drawImage(enemyImage, intxLocation, intyLocation, null);
		((Graphics2D) g).rotate(-rotationAngle, intxLocation + (Game.TILE_SIZE / 2), intyLocation + (Game.TILE_SIZE / 2));
		
		if(FIRE_EFFECT) {
			g.setColor(Color.RED);
			g.drawRect(intxLocation, intyLocation, Game.TILE_SIZE, Game.TILE_SIZE);
		}
		
		if(ICE_EFFECT) {
			g.setColor(Color.CYAN);
			((Graphics2D) g).rotate(Math.PI / 4, intxLocation + (Game.TILE_SIZE / 2), intyLocation + (Game.TILE_SIZE / 2));
			g.drawRect(intxLocation, intyLocation, Game.TILE_SIZE, Game.TILE_SIZE);
			((Graphics2D) g).rotate(-(Math.PI / 4), intxLocation + (Game.TILE_SIZE / 2), intyLocation + (Game.TILE_SIZE / 2));
		}
	}
	
	public static Enemy newEnemy(final int type, String id) {
		switch(type) {
			case Enemy.BASIC:
				return new BasicEnemy(id);
			case Enemy.ARMORED:
				return new ArmouredEnemy(id);
			case Enemy.QUICK:
				return new QuickEnemy(id);
			case Enemy.SUMMONER:
				return new SummonerEnemy(id);
			case Enemy.BOSS:
				return new BossEnemy(id);
		}
		return null;
	}
	
	//constructor
	public Enemy(String enemyFile, int type, String id) {
		this.type = type;
		this.id = id;
		
		Map<String, String> data = Utils.loadEnemy(enemyFile);
		this.intDamage = Integer.parseInt(data.get("damage"));
		this.intSpeed = Double.parseDouble(data.get("speed"));
		this.intHealth = Integer.parseInt(data.get("health"));
		this.enemyImage = Utils.loadImage("enemies/" + data.get("image"));
		this.reward = Integer.parseInt(data.get("reward"));
		
		this.intxLocation = GameMap.startX;
		this.intyLocation = GameMap.startY;
	}
	
}
