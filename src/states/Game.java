package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import enemies.BasicEnemy;
import enemies.Enemy;
import main.GameMap;
import main.InputListener;
import main.TowerDefence;
import main.Utils;
import projectiles.Projectile;
import towers.BasicTower;
import towers.Tower;

public class Game extends State {
	
	public static final int TILE_SIZE = 40;

	public static ArrayList<Enemy> removeEnemies = new ArrayList<>();

	private TowerDefence towerDefence;
	public GameMap map;
	private int intHealth = 100;
	private int intPlacingTower = -1;
	private Font font;
	
	//path tile image variables
	//UD = up down
	//LR = left right

	private BufferedImage imgGrassTile, imgPathTileUD, imgPathTileLR, imgPathTileCorner;
	
	public ArrayList<Tower> towers;
	public ArrayList<Enemy> enemies;
	public ArrayList<Projectile> projectiles;
	
	public static String strMessage;
	
	int intNumMessages = 0;
	
	//methods	
	@Override
	public void update() {
		//UPDATE TOWERS
		for(int i = 0; i < towers.size(); i++) {
			towers.get(i).update(this);
		}
		
		//UPDATE ENEMIES
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update(this);
		}
		
		//UDPATE PROJECTILES
		for(int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update(this);
		}
		
		//REMOVE DEAD ENEMIES
		for(Enemy enemy : removeEnemies) {
			enemies.remove(enemy);
		}
		removeEnemies.clear();
		
		//CHECK FOR PRESSED KEYS
		if(InputListener.keys[KeyEvent.VK_ESCAPE]) {
			if(intPlacingTower != -1) {
				intPlacingTower = -1;
			}
		}else if(InputListener.keys[KeyEvent.VK_1]) {
			intPlacingTower = Tower.BASIC;
		}
	}

	@Override
	public void render(Graphics g) {
		//RENDER GRASS TILES
		for(int y = 0; y < towerDefence.getHeight(); y += Game.TILE_SIZE) {
			for(int x = 0; x < towerDefence.getWidth() - Game.TILE_SIZE * 5; x += Game.TILE_SIZE) {
				g.drawImage(imgGrassTile, x, y, null);
				if(intPlacingTower != -1) {
					g.drawRect(x, y, Game.TILE_SIZE, Game.TILE_SIZE);
				}
			}
		}
		
		//RENDER PATH
		int currentCheckpointX = map.getCheckpointX(0);
		int currentCheckpointY = map.getCheckpointY(0);
		for(int n = 1; n < map.getNumberOfCheckpoints(); n++) {
			int checkpointX = map.getCheckpointX(n);
			int checkpointY = map.getCheckpointY(n);
			
			int previouscheckpointX = map.getCheckpointX(n-1);
			int previouscheckpointY = map.getCheckpointY(n-1);
			if(previouscheckpointX >= currentCheckpointX) {
				if(checkpointY > currentCheckpointY){
					g.drawImage(imgPathTileCorner, currentCheckpointX, currentCheckpointY, null);
				}
				else{
					g.drawImage(imgPathTileCorner, currentCheckpointX, currentCheckpointY, null);
				}
			}
			else if(previouscheckpointX <= currentCheckpointX) {
				if(checkpointY > currentCheckpointY) {
					g.drawImage(imgPathTileCorner, currentCheckpointX, currentCheckpointY, null);
				}
				else {
					g.drawImage(imgPathTileCorner, currentCheckpointX, currentCheckpointY, null);

				}
			}
			
			
			if(checkpointX > currentCheckpointX) {
				for(int x = currentCheckpointX + Game.TILE_SIZE; x <= checkpointX - Game.TILE_SIZE; x += Game.TILE_SIZE) {
					g.drawImage(imgPathTileLR, x, checkpointY, null);
				}
			}else if(checkpointX < currentCheckpointX) {
				for(int x = currentCheckpointX - Game.TILE_SIZE; x >= checkpointX + Game.TILE_SIZE; x -= Game.TILE_SIZE) {
					g.drawImage(imgPathTileLR, x, checkpointY, null);
				}
			}
			
			if(checkpointY > currentCheckpointY) {
				for(int y = currentCheckpointY + Game.TILE_SIZE; y <= checkpointY - Game.TILE_SIZE; y += Game.TILE_SIZE) {
					g.drawImage(imgPathTileUD, checkpointX, y, null);
				}
			}else if(checkpointY < currentCheckpointY) {
				for(int y = currentCheckpointY - Game.TILE_SIZE; y >= checkpointY + Game.TILE_SIZE; y -= Game.TILE_SIZE) {
					g.drawImage(imgPathTileUD, checkpointX, y, null);
				}
			}
			
			currentCheckpointX = checkpointX;
			currentCheckpointY = checkpointY;
		}
		
		//RENDER TOWER WE WANT TO PLACE
		if(intPlacingTower != -1) {			
			g.drawImage(Tower.towerImages[intPlacingTower], (int) Math.floor(InputListener.mouseX / Game.TILE_SIZE) * Game.TILE_SIZE,
					 (int) Math.floor((InputListener.mouseY - (Game.TILE_SIZE / 2)) / Game.TILE_SIZE) * Game.TILE_SIZE, null);
		}
		
		//RENDER TOWERS
		for(int i = 0; i < towers.size(); i++) {
			towers.get(i).render(g);
		}
		
		//RENDER ENEMIES
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).render(g);
		}
		
		//RENDER PROJECTILES
		for(int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).render(g);
		}
		
		/////UI/////
		//RENDER CHAT
		if(Game.strMessage != null) {
			int intCount;
			//creates an array to store old messages
			String [] strOldMessage = new String [intNumMessages];
			//loops through and draws 
				for(intCount = 0; intCount<5; intCount++) {
					g.drawString(strOldMessage[intNumMessages - intCount], 0, (18 - intCount) * Game.TILE_SIZE);
				}
			strOldMessage[intNumMessages] = Game.strMessage;
			intNumMessages += 1;
		}
		
		//RENDER TOOL BAR
		g.setColor(Color.ORANGE);
		g.fillRect(Game.TILE_SIZE * 27, 0, Game.TILE_SIZE * 5, towerDefence.getHeight());
		//RENDER ROUND COUNTER
		//waiting to set up round counter
		//g.drawString("Round:"+, 28 * Game.TILE_SIZE, 1 * Game.TILE_SIZE);
		//RENDER HEALTH
		BufferedImage heart = Utils.loadImage("sidebar/" + "heart.png");
		g.drawImage(heart, 28 * Game.TILE_SIZE, 3 * Game.TILE_SIZE, null);
		g.setColor(Color.BLACK);
		g.drawString("" + intHealth, Game.TILE_SIZE * 29, Game.TILE_SIZE * 4);
		//RENDER PURCHASABLE TOWERS
		g.drawImage(Tower.towerImages[Tower.BASIC], 28* Game.TILE_SIZE, 6 * Game.TILE_SIZE, null);
		g.drawImage(Tower.towerImages[Tower.FIRE], 28* Game.TILE_SIZE, 8 * Game.TILE_SIZE, null);
		g.drawImage(Tower.towerImages[Tower.ICE], 28* Game.TILE_SIZE, 10 * Game.TILE_SIZE, null);
		g.drawImage(Tower.towerImages[Tower.SNIPE], 28* Game.TILE_SIZE, 12 * Game.TILE_SIZE, null);
		g.drawImage(Tower.towerImages[Tower.BOMB], 28* Game.TILE_SIZE,14 * Game.TILE_SIZE, null);
		//RENDER TOWER PRICES
		g.drawString("$"+ Tower.towerFiles[Tower.BASIC].get("price"), 29 * Game.TILE_SIZE, 7 * Game.TILE_SIZE);
		g.drawString("$"+ Tower.towerFiles[Tower.FIRE].get("price"), 29 * Game.TILE_SIZE, 9 * Game.TILE_SIZE);
		g.drawString("$"+ Tower.towerFiles[Tower.ICE].get("price"), 29 * Game.TILE_SIZE, 11 * Game.TILE_SIZE);
		g.drawString("$"+ Tower.towerFiles[Tower.SNIPE].get("price"), 29 * Game.TILE_SIZE, 13 * Game.TILE_SIZE);
		g.drawString("$"+ Tower.towerFiles[Tower.BOMB].get("price"), 29 * Game.TILE_SIZE, 15 * Game.TILE_SIZE);
		//RENDER TOWER NAMES
		g.setFont(font);
		g.drawString("Basic", 28 * Game.TILE_SIZE, 6 * Game.TILE_SIZE);
		g.drawString("Fire", 28 * Game.TILE_SIZE, 8 * Game.TILE_SIZE);
		g.drawString("Ice", 28 * Game.TILE_SIZE, 10 * Game.TILE_SIZE);
		g.drawString("Snipe", 28 * Game.TILE_SIZE, 12 * Game.TILE_SIZE);
		g.drawString("Bomb", 28 * Game.TILE_SIZE, 14 * Game.TILE_SIZE);
	}

	public void dealDamage(int intDamage) {
		this.intHealth -= intDamage;
		if(intHealth <= 0) {
			//GAME OVER
		}
	}
	
	//constructor
	public Game(TowerDefence towerDefence) {
		this.towerDefence = towerDefence;
		
		Tower.loadTowerFiles();
		
		this.imgGrassTile = Utils.loadImage("tiles/GrassTile.jpg");
		this.imgPathTileUD = Utils.loadImage("tiles/" + "PathTileUD.jpg");
		this.imgPathTileLR = Utils.loadImage("tiles/" + "PathTileLR.jpg");
		this.imgPathTileCorner = Utils.loadImage("tiles/" + "PathTileDR.jpg");
		
		
		map = new GameMap("map");
		
		towers = new ArrayList<>();
		towers.add(new BasicTower(9, 15));
		
		enemies = new ArrayList<>();
		enemies.add(new BasicEnemy());
		
		projectiles = new ArrayList<>();
		
		font = new Font("Arial", Font.PLAIN, 18);
	}
}
