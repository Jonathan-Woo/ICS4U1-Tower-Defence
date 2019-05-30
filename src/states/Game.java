package states;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import enemies.BasicEnemy;
import enemies.Enemy;
import main.GameMap;
import main.TowerDefence;
import main.Utils;
import projectiles.Projectile;
import towers.BasicTower;
import towers.Tower;

public class Game extends State{
	
	public static final int TILE_SIZE = 40;

	public static ArrayList<Enemy> removeEnemies = new ArrayList<>();

	private TowerDefence towerDefence;
	public GameMap map;
	private int intHealth = 100;
	
	private BufferedImage imgGrassTile, imgPathTile;
	
	public ArrayList<Tower> towers;
	public ArrayList<Enemy> enemies;
	public ArrayList<Projectile> projectiles;
	
	public static String strMessage;
	
	public Game(TowerDefence towerDefence) {
		this.towerDefence = towerDefence;
		
		this.imgGrassTile = Utils.loadImage("GrassTile.jpg");
		this.imgPathTile = Utils.loadImage("PathTile_Unfinished.jpg");
		
		map = new GameMap("map");
		
		towers = new ArrayList<>();
		towers.add(new BasicTower(9, 15));
		
		enemies = new ArrayList<>();
		enemies.add(new BasicEnemy());
		
		projectiles = new ArrayList<>();
	}
	
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
	}

	@Override
	public void render(Graphics g) {
		//RENDER GRASS TILES
		for(int y = 0; y < towerDefence.getHeight(); y += Game.TILE_SIZE) {
			for(int x = 0; x < towerDefence.getWidth(); x += Game.TILE_SIZE) {
				g.drawImage(imgGrassTile, x, y, null);
				g.drawRect(x, y, Game.TILE_SIZE, Game.TILE_SIZE);
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
		
		//RENDER CHAT
		if(strMessage != null) {
			g.drawString(strMessage, 0, 700);
		}
	}

	public void dealDamage(int intDamage) {
		this.intHealth -= intDamage;
		if(intHealth <= 0) {
			//GAME OVER
		}
	}
}
