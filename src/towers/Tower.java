package towers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

import enemies.Enemy;
import main.Utils;
import projectiles.Projectile;
import states.Game;

public abstract class Tower {
	
	//properties
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
	private BufferedImage towerImage;
	private Enemy currentEnemy;
	
	private int intProjectileRadius;
	private int intProjectileSpeed;
	private Color projectileColor;
	
	//methods
	private boolean isInRange(Enemy enemy){
		return true;
	}
	
	private void findEnemy(ArrayList<Enemy> enemies) {
		
	}
	
	private void attackEnemy(Game game){
		long longCurrentTime = System.currentTimeMillis();
		if(longCurrentTime - longLastAttack >=intAttackSpeed) {
			Projectile projectile = new Projectile(intAttackDamage, this, intProjectileRadius, intProjectileSpeed, projectileColor, currentEnemy);
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
			attackEnemy(game);
		}
	}
	
	/**
	 * Renders tower image to screen every game loop.
	 * @param g The Graphics object used for drawing to the screen.
	 */
	public void render(Graphics g) {
		g.drawImage(towerImage, intxLocation, intyLocation, null);
	}
	
	//constructor
	/**
	 * Takes in the type of tower and the tower location. Loads the tower from a csv file based on type. Saves properties to local variables.
	 * @param type The Tower type to load.
	 * @param intxLocation The X coordinate of the Tower.
	 * @param intyLocation The Y coordinate of the Tower.
	 */
	public Tower(int type, int intxLocation, int intyLocation) {
		Map<String, String> data = Utils.loadTower(type);
		this.strName = data.get("name");
		this.intxLocation = intxLocation;
		this.intyLocation = intyLocation;
		this.intPrice = Integer.parseInt(data.get("price"));
		this.intRange = Integer.parseInt(data.get("range"));
		this.intAttackSpeed = Integer.parseInt(data.get("attackSpeed"));
		this.intAttackDamage = Integer.parseInt(data.get("damage"));
		this.towerImage = Utils.loadImage(data.get("image"));
		
		this.intProjectileRadius = Integer.parseInt(data.get("projectileRadius"));
		this.intProjectileSpeed = Integer.parseInt(data.get("projectileSpeed"));
		this.projectileColor = Color.decode(data.get("projectileColor"));
	}
}
