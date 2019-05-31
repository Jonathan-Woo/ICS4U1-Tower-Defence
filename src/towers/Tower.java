package towers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import enemies.Enemy;
import main.Utils;
import projectiles.Projectile;
import states.Game;

public abstract class Tower {
	
	//properties
	private static Map<String, String>[] towerFiles;
	private static BufferedImage[] towerImages;
	/**
	 *Numerical representation of the basic tower.
	 **/
	public static final int BASIC = 0;
	/**
	 *Numerical representation of the fire tower.
	 **/
	public static final int FIRE = 1;
	/**
	 *Numerical representation of the ice tower.
	 **/
	public static final int ICE  = 2;
	/**
	 *Numerical representation of the snipe tower.
	 **/
	public static final int SNIPE = 3;
	/**
	 *Numerical representation of the bomb tower.
	 **/
	public static final int BOMB = 4;
	
	/**
	 * Type of the tower.
	 */
	private int type;
	/**
	 * Name of the tower type.
	 **/
	public String strName;
	/**
	 * X coordinate of tower.
	 **/
	public int intxLocation;
	/**
	 * Y coordinate of tower.
	 **/
	public int intyLocation;
	/**
	 * Tower price.
	 **/
	public int intPrice;
	/**
	 * Radius range of tower.
	 **/
	public int intRange;
	/**
	 * Time it takes before the tower can fire again.
	 **/
	public int intAttackSpeed;
	/**
	 * Damage that each projectile deals to enemies.
	 **/
	public int intAttackDamage;
	private long longLastAttack = 0;
	private Enemy currentEnemy;
	
	private int intProjectileRadius;
	private int intProjectileSpeed;
	private Color projectileColor;
	
	//methods
	@SuppressWarnings("unchecked")
	public static void loadTowerFiles() {
		towerFiles = (Map<String, String>[]) new Map[] {
			Utils.loadTower("basicTower")
		};
		
		towerImages = new BufferedImage[] {
			Utils.loadImage("towers/" + towerFiles[Tower.BASIC].get("image"))
		};
	}
	

	public static BufferedImage getImage(int type) {
		return towerImages[type];
	}
	
	private void findEnemy(ArrayList<Enemy> enemies) {
		Enemy currentEnemy = null;
		int bestXDistToTower = 200;
		int bestYDistToTower = 200;
		
		for(int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			int xDistToTower = Math.abs(enemy.intxLocation + (Game.TILE_SIZE / 2) - this.intxLocation);
			int yDistToTower = Math.abs(enemy.intxLocation + (Game.TILE_SIZE / 2) - this.intxLocation);
			
			if(xDistToTower <= this.intRange && yDistToTower <= this.intRange) {
				if(currentEnemy != null) {
					if(xDistToTower < bestXDistToTower && yDistToTower < bestYDistToTower) {
						currentEnemy = enemy;
						bestXDistToTower = xDistToTower;
						bestYDistToTower = yDistToTower;
					}
				}else {
					currentEnemy = enemy;
					bestXDistToTower = xDistToTower;
					bestYDistToTower = yDistToTower;
				}			
			}
		}
		
		this.currentEnemy = currentEnemy;
	}
	
	private void attackEnemy(Game game){
		long longCurrentTime = System.currentTimeMillis();
		if(longCurrentTime - longLastAttack >=intAttackSpeed) {
			Projectile projectile = new Projectile(intAttackDamage, this, intProjectileRadius,
					intProjectileSpeed, projectileColor, currentEnemy);
			game.projectiles.add(projectile);
			longLastAttack = longCurrentTime;
		}
	}
	
	/**
	 * Update method gets called every game loop. Either finds new enemy or attacks currently targeted enemy
	 * @param game The Game State.
	 */
	public void update(Game game) {
		if (currentEnemy == null) {
			findEnemy(game.enemies);
		}else {
			if(game.enemies.contains(currentEnemy)) {
				attackEnemy(game);
			}else {
				currentEnemy = null;
			}			
		}
	}
	
	/**
	 * Renders tower image to screen every game loop.
	 * @param g The Graphics object used for drawing to the screen.
	 */
	public void render(Graphics g) {
		g.drawImage(towerImages[type], intxLocation, intyLocation, null);
	}
	
	//constructor
	/**
	 * Takes in the type of tower and the tower location. Loads the tower from a csv file based on type. Saves properties to local variables.
	 * @param type The Tower type to load.
	 * @param intxLocation The X coordinate of the Tower.
	 * @param intyLocation The Y coordinate of the Tower.
	 */
	public Tower(int intxLocation, int intyLocation, int type) {
		this.type = type;
		Map<String, String> data = towerFiles[type];
		this.strName = data.get("name");
		this.intxLocation = intxLocation * Game.TILE_SIZE;
		this.intyLocation = intyLocation * Game.TILE_SIZE;
		this.intPrice = Integer.parseInt(data.get("price"));
		this.intRange = Integer.parseInt(data.get("range"));
		this.intAttackSpeed = Integer.parseInt(data.get("attackSpeed"));
		this.intAttackDamage = Integer.parseInt(data.get("damage"));
		
		this.intProjectileRadius = Integer.parseInt(data.get("projectileRadius"));
		this.intProjectileSpeed = Integer.parseInt(data.get("projectileSpeed"));
		this.projectileColor = Color.decode(data.get("projectileColor"));
	}
}
