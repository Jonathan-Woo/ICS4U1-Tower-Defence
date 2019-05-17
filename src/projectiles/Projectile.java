package projectiles;

import java.awt.Color;
import java.awt.Graphics;

import enemies.Enemy;
import states.Game;
import towers.Tower;

public class Projectile {
	//properties
	int intDamage;
	int intxLocation;
	int intyLocation;
	int intRadius;
	int intProjectileSpeed;
	Color color;
	Enemy currentEnemy;
	
	//methods
	public void update(Game game) {
		if(Math.abs(currentEnemy.intxLocation - intxLocation) <=5 && Math.abs(currentEnemy.intyLocation - intyLocation)<=5) {
			//HIT THE ENEMY: DAMAGE ENEMY, REMOVE PROJECTILE
			game.projectiles.remove(this);
		}else{
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
		}
	}
	
	public void render(Graphics g){
		//Draw projectile (ball)
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
