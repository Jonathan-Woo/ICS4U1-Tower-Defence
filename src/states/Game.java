package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Timer;

import enemies.BasicEnemy;
import enemies.Enemy;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
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
	private int waveNumber = 1;
	private int intBalance = 1000000;
	private int intPlacingTower = -1;
	private Font font;
	private int[] enemyWave;
	private int roundTime = 3;
	private Timer roundTimer, waveTimer;
	private int enemiesInWave = 0;
	
	//path tile image variables
	//UD = up down
	//LR = left right

	private BufferedImage imgGrassTile, imgPathTileUD, imgPathTileLR, imgPathTileUR, imgPathTileUL, imgPathTileDL, imgPathTileDR;
	
	public ArrayList<Tower> towers;
	public ArrayList<Enemy> enemies;
	public ArrayList<Projectile> projectiles;
	
	public static String strMessage;
	
	//methods
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
			if(enemies.contains(enemy)) {
				enemies.remove(enemy);
				intBalance += enemy.getReward();
			}
		}
		removeEnemies.clear();
		
		//CHECK IF TOWER IS PRESSED FROM TOWER BAR
		if(InputListener.mouseButtons[MouseEvent.BUTTON1]) {
			if(InputListener.mouseX >= Game.TILE_SIZE * 28 && InputListener.mouseX <= Game.TILE_SIZE * 29) {
				if(InputListener.mouseY >= Game.TILE_SIZE * 6 && InputListener.mouseY <= Game.TILE_SIZE * 7) {
					//PRESSED BASIC TOWER
					this.intPlacingTower = Tower.BASIC;
				}else if(InputListener.mouseY >= Game.TILE_SIZE * 8 && InputListener.mouseY <= Game.TILE_SIZE * 9) {
					//PRESSED FIRE TOWER
					this.intPlacingTower = Tower.FIRE;
				}else if(InputListener.mouseY >= Game.TILE_SIZE * 10 && InputListener.mouseY <= Game.TILE_SIZE * 11) {
					//PRESSED ICE TOWER
					this.intPlacingTower = Tower.ICE;
				}else if(InputListener.mouseY >= Game.TILE_SIZE * 12 && InputListener.mouseY <= Game.TILE_SIZE * 13) {
					//PRESSED SNIPE TOWER
					this.intPlacingTower = Tower.SNIPE;
				}else if(InputListener.mouseY >= Game.TILE_SIZE * 14 && InputListener.mouseY <= Game.TILE_SIZE * 15) {
					//PRESSED BOMB TOWER
					this.intPlacingTower = Tower.BOMB;
				}
			}else if(InputListener.mouseX < Game.TILE_SIZE * 27 && intPlacingTower != -1) {
				int towerX = (int) Math.floor(InputListener.mouseX / Game.TILE_SIZE) * Game.TILE_SIZE;
				int towerY = (int) Math.floor(InputListener.mouseY / Game.TILE_SIZE) * Game.TILE_SIZE;
				
				//CHECK IF TOWER IS GETTING PLACED IN PATH
				boolean canBePlaced = true;
				int previousCheckpointX = map.getCheckpointX(0);
				int previousCheckpointY = map.getCheckpointY(0);
				for(int n = 1; n < map.getNumberOfCheckpoints(); n++) {
					int checkpointX = map.getCheckpointX(n);
					int checkpointY = map.getCheckpointY(n);
					
					Line line = new Line(previousCheckpointX + (Game.TILE_SIZE / 2), previousCheckpointY + (Game.TILE_SIZE / 2),
							checkpointX + (Game.TILE_SIZE / 2), checkpointY + (Game.TILE_SIZE / 2));
					if(line.intersects(towerX, towerY, Game.TILE_SIZE, Game.TILE_SIZE)) {
						canBePlaced = false;
						break;
					}
					
					previousCheckpointX = checkpointX;
					previousCheckpointY = checkpointY;
				}
				
				if(canBePlaced) {
					//CHECK IF TOWER IS GOING TO COLLIDE WITH OTHER TOWERS
					for(Tower tower : towers) {
						if(tower.intxLocation == towerX && tower.intyLocation == towerY) {
							canBePlaced = false;
							break;
						}
					}
				}
				
				if(canBePlaced) {
					int towerPrice = Integer.parseInt(Tower.towerFiles[intPlacingTower].get("price"));
					if(intBalance >= towerPrice) {				
						Tower tower = Tower.newTower(intPlacingTower, towerX, towerY);
						towers.add(tower);
						
						intBalance -= towerPrice;
						intPlacingTower = -1;
					}
				}
			}
		}
		
		//CHECK FOR PRESSED KEYS
		if(InputListener.keys[KeyEvent.VK_ESCAPE]) {
			if(intPlacingTower != -1) {
				intPlacingTower = -1;
			}
		}else if(InputListener.keys[KeyEvent.VK_1]) {
			intPlacingTower = Tower.BASIC;
		}else if(InputListener.keys[KeyEvent.VK_2]) {
			intPlacingTower = Tower.FIRE;
		}else if(InputListener.keys[KeyEvent.VK_3]) {
			intPlacingTower = Tower.ICE;
		}else if(InputListener.keys[KeyEvent.VK_4]) {
			intPlacingTower = Tower.SNIPE;
		}else if(InputListener.keys[KeyEvent.VK_5]) {
			intPlacingTower = Tower.BOMB;
		}
		
		//CREATE ENEMY WAVES AND HANDLE DOWNTIME
		if(roundTime > 0) {
			if(roundTimer == null) {
				enemyWave = null;
				roundTimer = new Timer(1000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						roundTime--;
						if(roundTime <= 0) {
							roundTimer.stop();
							roundTimer = null;
						}
					}
				});
				roundTimer.start();
			}			
		}else{
			if(enemyWave == null) {						
				enemyWave = new int[] {
						waveNumber,
						(int) Math.floor(waveNumber / 2),
						(int) Math.floor(waveNumber / 3),
						(int) Math.floor(waveNumber / 4),
						(int) Math.floor(waveNumber / 5)
				};
				
				spawnEnemies();
			}
			
			if(waveTimer == null) {
				waveTimer = new Timer(1500, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						spawnEnemies();
					}
				});
				waveTimer.start();
			}	
			
			if(enemies.size() == 0 && enemiesInWave == 0) {
				waveNumber++;
				roundTime = 3;
				waveTimer.stop();
				waveTimer = null;
				enemyWave = null;
			}
		}
	}
	
	private void spawnEnemies() {
		if(enemyWave != null) {
			enemiesInWave = 0;
			for(int enemyNum : enemyWave) {
				enemiesInWave += enemyNum;
			}
			
			boolean enemySpawned = false;
			while(!enemySpawned && enemiesInWave > 0) {
				int random = (int) Math.round(Math.random() * 4);
				if(enemyWave[random] > 0) {
					enemies.add(Enemy.newEnemy(random));
					enemyWave[random]--;
					enemySpawned = true;
				}
			}
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
		//draws the rest of the path tiles
		int previousCheckpointX = map.getCheckpointX(0);
		int previousCheckpointY = map.getCheckpointY(0);
		for(int n = 1; n < map.getNumberOfCheckpoints(); n++) {
			int checkpointX = map.getCheckpointX(n);
			int checkpointY = map.getCheckpointY(n);
			if(checkpointX > previousCheckpointX) {
				for(int x = previousCheckpointX; x <= checkpointX; x += Game.TILE_SIZE) {
					g.drawImage(imgPathTileLR, x, checkpointY, null);
				}
			}else if(checkpointX < previousCheckpointX) {
				for(int x = previousCheckpointX; x >= checkpointX; x -= Game.TILE_SIZE) {
					g.drawImage(imgPathTileLR, x, checkpointY, null);
				}
			}else if(checkpointY > previousCheckpointY) {
				for(int y = previousCheckpointY; y <= checkpointY; y += Game.TILE_SIZE) {
					g.drawImage(imgPathTileUD, checkpointX, y, null);
				}
			}else if(checkpointY < previousCheckpointY) {
				for(int y = previousCheckpointY; y >= checkpointY; y -= Game.TILE_SIZE) {
					g.drawImage(imgPathTileUD, checkpointX, y, null);
				}
			}
			
			previousCheckpointX = checkpointX;
			previousCheckpointY = checkpointY;
		}
		
		previousCheckpointX = map.getCheckpointX(0);
		previousCheckpointY = map.getCheckpointY(0);
		for(int n = 1; n < map.getNumberOfCheckpoints(); n++) {
			int checkpointX = map.getCheckpointX(n);
			int checkpointY = map.getCheckpointY(n);
			
			if(n + 1 < map.getNumberOfCheckpoints()) {
				int nextCheckpointX = map.getCheckpointX(n + 1);
				int nextCheckpointY = map.getCheckpointY(n + 1);
				
				if(previousCheckpointX > checkpointX) {
					if(nextCheckpointY > checkpointY){
						g.drawImage(imgPathTileUR, checkpointX, checkpointY, null);
					}
					else{
						g.drawImage(imgPathTileDR, checkpointX, checkpointY, null);
					}
				}
				else if(previousCheckpointX < checkpointX) {
					if(nextCheckpointY > checkpointY) {
						g.drawImage(imgPathTileUL, checkpointX, checkpointY, null);
					}
					else {
						g.drawImage(imgPathTileDL, checkpointX, checkpointY, null);
					}
				}else if(previousCheckpointY > checkpointY) {
					if(nextCheckpointX > previousCheckpointX) {
						g.drawImage(imgPathTileUR, checkpointX, checkpointY, null);
					}else {
						g.drawImage(imgPathTileUL, checkpointX, checkpointY, null);
					}
				}else if(previousCheckpointY < checkpointY) {
					if(nextCheckpointX > previousCheckpointX) {
						g.drawImage(imgPathTileDR, checkpointX, checkpointY, null);
					}else {
						g.drawImage(imgPathTileDL, checkpointX, checkpointY, null);
					}
				}
			}
			
			previousCheckpointX = checkpointX;
			previousCheckpointY = checkpointY;
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
		
		//RENDER TOWER WE WANT TO PLACE
		if (intPlacingTower != -1) {
			int towerX = (int) Math.floor(InputListener.mouseX / Game.TILE_SIZE) * Game.TILE_SIZE;
			int towerY = (int) Math.floor(InputListener.mouseY / Game.TILE_SIZE) * Game.TILE_SIZE;
			int towerRadius = Integer.parseInt(Tower.towerFiles[intPlacingTower].get("range"));

			g.setColor(new Color(0.8f, 0f, 1f, 0.4f));
			g.fillOval(towerX - towerRadius, towerY - towerRadius,
					(towerRadius * 2) + Game.TILE_SIZE, (towerRadius * 2) + Game.TILE_SIZE);
			g.drawImage(Tower.towerImages[intPlacingTower], towerX, towerY, null);
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
		g.setColor(Color.BLACK);
		g.drawString("Round: "+ waveNumber, 28 * Game.TILE_SIZE, 1 * Game.TILE_SIZE);
		//RENDER BALANCE
		BufferedImage cashSign = Utils.loadImage("sidebar/" + "cashsign.png");
		g.drawImage(cashSign, 28 * Game.TILE_SIZE, 2 * Game.TILE_SIZE,null);
		g.drawString("" +intBalance, 29* Game.TILE_SIZE, 3 * Game.TILE_SIZE);
		//RENDER HEALTH
		BufferedImage heart = Utils.loadImage("sidebar/" + "heart.png");
		g.drawImage(heart, 28 * Game.TILE_SIZE, 4 * Game.TILE_SIZE, null);
		g.drawString("" + intHealth, Game.TILE_SIZE * 29, 5*Game.TILE_SIZE);
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
		
		//DRAW ROUND TIMER
		if(this.roundTime > 0) {
			g.setFont(TowerDefence.font);
			g.drawString("Next Round: " + roundTime, (towerDefence.getWidth() / 2) - 3 * Game.TILE_SIZE, towerDefence.getHeight() - Game.TILE_SIZE);
		}
	}

	public void dealDamage(int intDamage) {
		this.intHealth -= intDamage;
		if(intHealth <= 0) {
			//GAME OVER
		}
	}
	
	//constructor
	public Game(TowerDefence towerDefence) {
		super(towerDefence);
		
		Tower.loadTowerFiles();
		
		this.imgGrassTile = Utils.loadImage("tiles/GrassTile.jpg");
		this.imgPathTileUD = Utils.loadImage("tiles/" + "PathTileUD.jpg");
		this.imgPathTileLR = Utils.loadImage("tiles/" + "PathTileLR.jpg");
		this.imgPathTileUR = Utils.loadImage("tiles/" + "PathTileUR.jpg");
		this.imgPathTileUL = Utils.loadImage("tiles/" + "PathTileUL.jpg");
		this.imgPathTileDR = Utils.loadImage("tiles/" + "PathTileDR.jpg");
		this.imgPathTileDL = Utils.loadImage("tiles/" + "PathTileDL.jpg");
		this.imgPathTileDL = Utils.loadImage("tiles/" + "PathTileDL.jpg");	
		
		map = new GameMap("map");
		
		towers = new ArrayList<>();		
		enemies = new ArrayList<>();
		projectiles = new ArrayList<>();
		
		font = new Font("Arial", Font.PLAIN, 18);
	}
}
