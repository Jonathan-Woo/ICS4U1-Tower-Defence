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

//BLUEPRINT FOR ALL ENEMY CLASSES

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
	
	private int checkpointX, checkpointY;
	public int currentCheckpoint = 0;
	private double rotationAngle;
	
	public boolean FIRE_EFFECT = false, ICE_EFFECT = false;
	
	//REDUCES THE HEALTH OF THIS ENEMY BY A CERTAIN AMOUNT OF DAMAGE
	public void dealDamage(int intDamage) {
		this.intHealth -= intDamage;
		if(this.intHealth <= 0) {
			//TELL CLIENT TO REMOVE THE ENEMY IF THEY HAVEN'T ALREADY
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
		//CHECK IF ENEMY HAS REACHED TARGET CHECKPOINT
		if(Math.abs(intxLocation - checkpointX) <= this.intSpeed &&
				Math.abs(intyLocation - checkpointY) <= this.intSpeed) {
			this.intxLocation = checkpointX;
			this.intyLocation = checkpointY;
			
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
				rotationAngle = Math.PI / 2;
			}else if(intyLocation > checkpointY) {
				intyLocation -= intSpeed;
				rotationAngle = (Math.PI / 2) * 3;
			}
		}
	}
	
	public void render(Graphics g) {
		//DRAW ENEMY IMAGE WITH CORRECT ROTATION
		((Graphics2D) g).rotate(rotationAngle, intxLocation + (Game.TILE_SIZE / 2), intyLocation + (Game.TILE_SIZE / 2));
		g.drawImage(enemyImage, intxLocation, intyLocation, null);
		((Graphics2D) g).rotate(-rotationAngle, intxLocation + (Game.TILE_SIZE / 2), intyLocation + (Game.TILE_SIZE / 2));
		
		//DRAW FIRE EFFECT RECTANGLE
		if(FIRE_EFFECT) {
			g.setColor(Color.RED);
			g.drawRect(intxLocation, intyLocation, Game.TILE_SIZE, Game.TILE_SIZE);
		}
		
		//DRAW FIRE EFFECT ROTATED RECTANGLE
		if(ICE_EFFECT) {
			g.setColor(Color.CYAN);
			((Graphics2D) g).rotate(Math.PI / 4, intxLocation + (Game.TILE_SIZE / 2), intyLocation + (Game.TILE_SIZE / 2));
			g.drawRect(intxLocation, intyLocation, Game.TILE_SIZE, Game.TILE_SIZE);
			((Graphics2D) g).rotate(-(Math.PI / 4), intxLocation + (Game.TILE_SIZE / 2), intyLocation + (Game.TILE_SIZE / 2));
		}
	}
	
	//CREATE A NEW ENEMY BASED ON THE TYPE
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
		
		//LOAD ENEMY DATA
		Map<String, String> data = Utils.loadEnemy(enemyFile);
		this.intDamage = Integer.parseInt(data.get("damage"));
		this.intSpeed = Double.parseDouble(data.get("speed"));
		this.intHealth = Integer.parseInt(data.get("health"));
		this.enemyImage = Utils.loadImage("enemies/" + data.get("image"));
		this.reward = Integer.parseInt(data.get("reward"));
		
		//GET FIRST CHECKPOINT
		this.intxLocation = GameMap.startX * Game.TILE_SIZE;
		this.intyLocation = GameMap.startY * Game.TILE_SIZE;
		this.checkpointX = GameMap.startX;
		this.checkpointY = GameMap.startY;
	}
	
}
