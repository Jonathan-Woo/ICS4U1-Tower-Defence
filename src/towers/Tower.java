package towers;

import java.awt.image.BufferedImage;

import enemies.Enemy;
import main.Utils;
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
	
	//methods
	private boolean isInRange(Enemy enemy){
		return true;
	}
	private void findEnemy(Enemy[] enemies) {
	}
	private void attackEnemy(){
		long longCurrentTime = System.currentTimeMillis();
		if(longCurrentTime - longLastAttack >=intAttackSpeed) {
			//Take away health from enemy and create projectile animation
			longLastAttack = longCurrentTime;
		}
	}
	
	public void update(Game game) {
		if (currentEnemy == null) {
			findEnemy(game.getEnemies());
		}else {
			attackEnemy();
		}
	}
	
	//constructor
	public Tower(String strName, int intxLocation, int intyLocation, int intPrice, int intRange, int intAttackSpeed, int intAttackDamage, String strTowerImage) {
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
