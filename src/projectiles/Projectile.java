package projectiles;

import java.awt.Color;

import enemies.Enemy;
import towers.Tower;

public abstract class Projectile {
	//properties
	int intDamage;
	int intxLocation;
	int intyLocation;
	int intRadius;
	int intProjectileSpeed;
	Color color;
	Enemy currentEnemy;
	
	//methods
	public void update() {
		if(currentEnemy.intxLocation > intxLocation) {
			intxLocation += intProjectileSpeed;
		}else if(currentEnemy.intxLocation < intxLocation) {
			intxLocation -= intProjectileSpeed;
		}
		if(currentEnemy.intyLocation > intyLocation) {
			intyLocation += intProjectileSpeed;
		}else if(currentEnemy.intyLocation < intyLocation) {
			intyLocation -= intProjectileSpeed;
		}
		
		if(currentEnemy.intxLocation - intxLocation <=5 && currentEnemy.intyLocation - intyLocation<=5) {
			
		}
	}
	
	public void render() {
	}
	
	//constructor
	public Projectile(int intDamage, Tower tower, int intRadius, int intProjectileSpeed, Color color, Enemy currentEnemy) {
		this.intDamage = intDamage;
		this.intxLocation = tower.intxLocation;
		this.intyLocation = tower.intyLocation;
		this.intRadius = intRadius;
		this.intProjectileSpeed = intProjectileSpeed;
		this.color = color;
		this.currentEnemy = currentEnemy;
	}

}
