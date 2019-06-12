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
	public static Map<String, String>[] towerFiles;
	public static BufferedImage[] towerImages;
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
	
	public static final int UPGRADE_DAMAGE = 0, UPGRADE_RANGE = 1, UPGRADE_SPEED = 2;
	
	/**
	 * Type of the tower.
	 */
	public int type;
	
	public String id;
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
	
	public int damageUpgrades = 0;
	public int rangeUpgrades = 0;
	public int speedUpgrades = 0;
	private int defaultDamage, defaultRange, defaultSpeed;
	
	//methods
	@SuppressWarnings("unchecked")
	public static void loadTowerFiles() {
		towerFiles = (Map<String, String>[]) new Map[] {
			Utils.loadTower("basicTower"),
			Utils.loadTower("fireTower"),
			Utils.loadTower("iceTower"),
			Utils.loadTower("snipeTower"),
			Utils.loadTower("bombTower")
		};
		
		towerImages = new BufferedImage[] {
			Utils.loadImage("towers/" + towerFiles[Tower.BASIC].get("image")),
			Utils.loadImage("towers/" + towerFiles[Tower.FIRE].get("image")),
			Utils.loadImage("towers/" + towerFiles[Tower.ICE].get("image")),
			Utils.loadImage("towers/" + towerFiles[Tower.SNIPE].get("image")),
			Utils.loadImage("towers/" + towerFiles[Tower.BOMB].get("image"))
		};
	}
	
	private boolean isInRange(Enemy enemy){
		int xDistToTower = Math.abs(enemy.intxLocation - this.intxLocation) - (Game.TILE_SIZE / 2);
		int yDistToTower = Math.abs(enemy.intyLocation - this.intyLocation) - (Game.TILE_SIZE / 2);
		
		if(xDistToTower <= this.intRange && yDistToTower <= this.intRange) {
			return true;
		}else {
			return false;
		}
	}
	
	private void findEnemy(ArrayList<Enemy> enemies) {
		Enemy currentEnemy = null;
		int bestXDistToTower = this.intRange;
		int bestYDistToTower = this.intRange;
		
		for(int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			int xDistToTower = Math.abs(enemy.intxLocation - this.intxLocation) - (Game.TILE_SIZE / 2);
			int yDistToTower = Math.abs(enemy.intyLocation - this.intyLocation) - (Game.TILE_SIZE / 2);
			
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
	
	public int getPrice() {
		return this.intPrice;
	}
	
	public void upgrade(final int upgrade) {
		switch(upgrade) {
			case Tower.UPGRADE_DAMAGE:
				if(this.damageUpgrades < 5) {
					this.damageUpgrades++;
					this.intAttackDamage += this.defaultDamage * 0.2;
				}
				break;
			case Tower.UPGRADE_RANGE:
				if(this.rangeUpgrades < 5) {
					this.rangeUpgrades++;
					this.intRange += this.defaultRange * 0.1;
				}
				break;
			case Tower.UPGRADE_SPEED:
				if(this.speedUpgrades < 5) {
					this.speedUpgrades++;
					this.intAttackSpeed += this.defaultSpeed * 0.2;
				}
				break;
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
			if(game.enemies.contains(currentEnemy) && isInRange(currentEnemy)) {
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
		/*g.setColor(new Color(0.8f, 0f, 1f, 0.4f));
		g.fillOval(this.intxLocation - (intRange / 2),
				this.intyLocation - (intRange / 2), intRange + Game.TILE_SIZE, intRange + Game.TILE_SIZE);*/
		g.drawImage(towerImages[type], intxLocation, intyLocation, null);
	}
	
	public abstract void effectOnHit(Enemy enemy);
	
	public static Tower newTower(final int type, int towerX, int towerY, String id) {
		switch(type) {
			case Tower.BASIC:
				return new BasicTower(towerX, towerY, id);
			case Tower.FIRE:
				return new FireTower(towerX, towerY, id);
			case Tower.ICE:
				return new IceTower(towerX, towerY, id);
			case Tower.SNIPE:
				return new SnipeTower(towerX, towerY, id);
			case Tower.BOMB:
				return new BombTower(towerX, towerY, id);
		}
		return null;
	}
	
	//constructor
	/**
	 * Takes in the type of tower and the tower location. Loads the tower from a csv file based on type. Saves properties to local variables.
	 * @param type The Tower type to load.
	 * @param intxLocation The X coordinate of the Tower.
	 * @param intyLocation The Y coordinate of the Tower.
	 */
	public Tower(int intxLocation, int intyLocation, int type, String id) {
		this.type = type;
		this.id = id;
		
		Map<String, String> data = towerFiles[type];
		this.strName = data.get("name");
		this.intxLocation = intxLocation;
		this.intyLocation = intyLocation;
		this.intPrice = Integer.parseInt(data.get("price"));
		this.intRange = Integer.parseInt(data.get("range"));
		this.intAttackSpeed = Integer.parseInt(data.get("attackSpeed"));
		this.intAttackDamage = Integer.parseInt(data.get("damage"));
		
		this.intProjectileRadius = Integer.parseInt(data.get("projectileRadius"));
		this.intProjectileSpeed = Integer.parseInt(data.get("projectileSpeed"));
		this.projectileColor = Color.decode(data.get("projectileColor"));
		
		this.defaultDamage = this.intAttackDamage;
		this.defaultRange = this.intRange;
		this.defaultSpeed = this.intAttackSpeed;
	}
}
