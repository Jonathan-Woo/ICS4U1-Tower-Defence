package towers;

import java.awt.image.BufferedImage;

import enemies.Enemy;
import main.Utils;

public abstract class Tower {
	
	//properties
	String strName;
	int intxLocation;
	int intyLocation;
	int intPrice;
	int intRange;
	int intAttackSpeed;
	long longLastAttack;
	BufferedImage towerImage;
	
	//methods
	public boolean isInRange(Enemy enemy){
		return true;
	}
	public void attack(Enemy enemy) {
	}
	
	//constructor
	public Tower(String strName, int intxLocation, int intyLocation, int intPrice, int intRange, int intAttackSpeed, String strTowerImage) {
		this.strName = strName;
		this.intxLocation = intxLocation;
		this.intyLocation = intyLocation;
		this.intPrice = intPrice;
		this.intRange = intRange;
		this.intAttackSpeed = intAttackSpeed;
		this.towerImage = Utils.loadImage(strTowerImage);

	}
}
