package towers;

import java.awt.Color;
import java.awt.image.BufferedImage;

import enemies.Enemy;
import main.Utils;
import projectiles.Projectile;
import states.Game;

public abstract class Tower {
	
	//properties
	/***/
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
	private void findEnemy(Enemy[] enemies) {
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
			findEnemy(game.getEnemies());
		}else {
			attackEnemy(game);
		}
	}
	
	//constructor
	public Tower(String strName, int intxLocation, int intyLocation, int intPrice, int intRange,
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

	}
}
