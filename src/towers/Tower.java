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
	/***/
	public static final int BASIC = 0;
	
	String strName;
	public int intxLocation;
	public int intyLocation;
	int intPrice;
	int intRange;
	int intAttackSpeed;
	int intAttackDamage;
	long longLastAttack = 0;
	BufferedImage towerImage;
	Enemy currentEnemy;
	
	int intProjectileRadius;
	int intProjectileSpeed;
	Color projectileColor;
	
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
	
	public void update(Game game) {
		if (currentEnemy == null) {
			findEnemy(game.enemies);
		}else {
			attackEnemy(game);
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(towerImage, intxLocation, intyLocation, null);
	}
	
	//constructor
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
	
	/*public Tower(String strName, int intxLocation, int intyLocation, int intPrice, int intRange,
			int intAttackSpeed, int intAttackDamage, String strTowerImage, int intProjectileRadius,
			int intProjectileSpeed, Color projectileColor) {
		this.strName = strName;
		this.intxLocation = intxLocation;
		this.intyLocation = intyLocation;
		this.intPrice = intPrice;
		this.intRange = intRange;
		this.intAttackSpeed = intAttackSpeed;
		this.intAttackDamage = intAttackDamage;
		this.towerImage = Utils.loadImage(strTowerImage);		
	}*/
}
