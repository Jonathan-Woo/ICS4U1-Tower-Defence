package states;

import java.awt.Color;
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
	
	private BufferedImage imgGrassTile, imgPathTile;
	private BufferedImage imgPlacingTower;
	
	public ArrayList<Tower> towers;
	public ArrayList<Enemy> enemies;
	public ArrayList<Projectile> projectiles;
	
	public static String strMessage;
	
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
			
			if(checkpointX > currentCheckpointX) {
				for(int x = currentCheckpointX; x <= checkpointX; x += Game.TILE_SIZE) {
					g.drawImage(imgPathTile, x, checkpointY, null);
				}
			}else if(checkpointX < currentCheckpointX) {
				for(int x = currentCheckpointX; x >= checkpointX; x -= Game.TILE_SIZE) {
					g.drawImage(imgPathTile, x, checkpointY, null);
				}
			}
			
			if(checkpointY > currentCheckpointY) {
				for(int y = currentCheckpointY; y <= checkpointY; y += Game.TILE_SIZE) {
					g.drawImage(imgPathTile, checkpointX, y, null);
				}
			}else if(checkpointY < currentCheckpointY) {
				for(int y = currentCheckpointY; y >= checkpointY; y -= Game.TILE_SIZE) {
					g.drawImage(imgPathTile, checkpointX, y, null);
				}
			}
			
			currentCheckpointX = checkpointX;
			currentCheckpointY = checkpointY;
		}
		
		//RENDER TOWER WE WANT TO PLACE
		if(intPlacingTower != -1) {
			if(imgPlacingTower == null) {
				imgPlacingTower = Utils.loadImage("towers/" + Utils.loadTower(Tower.towerFiles[intPlacingTower]).get("image"));
			}
			
			g.drawImage(imgPlacingTower, (int) Math.round(InputListener.mouseX / Game.TILE_SIZE) * Game.TILE_SIZE,
					 (int) Math.round(InputListener.mouseY / Game.TILE_SIZE) * Game.TILE_SIZE, null);
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
			g.drawString(Game.strMessage, 0, 700);
		}
		
		//RENDER TOOL BAR
		g.setColor(Color.ORANGE);
		g.fillRect(Game.TILE_SIZE * 27, 0, Game.TILE_SIZE * 5, towerDefence.getHeight());
		//RENDER HEALTH
		g.setColor(Color.BLACK);
		g.drawString("" + intHealth, Game.TILE_SIZE * 28, Game.TILE_SIZE * 2);
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
		
		this.imgGrassTile = Utils.loadImage("tiles/GrassTile.jpg");
		this.imgPathTile = Utils.loadImage("PathTile_Unfinished.jpg");
		
		map = new GameMap("map");
		
		towers = new ArrayList<>();
		towers.add(new BasicTower(9, 15));
		
		enemies = new ArrayList<>();
		enemies.add(new BasicEnemy());
		
		projectiles = new ArrayList<>();
	}
}
