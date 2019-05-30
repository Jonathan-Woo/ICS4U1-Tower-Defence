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
		if(Math.abs(currentEnemy.intxLocation + (Game.TILE_SIZE / 2) - intxLocation) <= 5
				&& Math.abs(currentEnemy.intyLocation + (Game.TILE_SIZE / 2) - intyLocation) <= 5) {
			//HIT THE ENEMY: DAMAGE ENEMY, REMOVE PROJECTILE
			game.projectiles.remove(this);
			currentEnemy.dealDamage(intDamage);
		}else{
			if(currentEnemy.intxLocation + (Game.TILE_SIZE / 2) > intxLocation) {
				intxLocation += intProjectileSpeed;
			}else if(currentEnemy.intxLocation + (Game.TILE_SIZE / 2) < intxLocation) {
				intxLocation -= intProjectileSpeed;
			}
			if(currentEnemy.intyLocation + (Game.TILE_SIZE / 2) > intyLocation) {
				intyLocation += intProjectileSpeed;
			}else if(currentEnemy.intyLocation + (Game.TILE_SIZE / 2) < intyLocation) {
				intyLocation -= intProjectileSpeed;
			}
		}
	}
	
	public void render(Graphics g){
		g.setColor(color);
		g.fillOval(intxLocation - (intRadius / 2), intyLocation - (intRadius / 2), intRadius, intRadius);
	}
	
	//constructor
	public Projectile(int intDamage, Tower tower, int intRadius, int intProjectileSpeed, Color color, Enemy currentEnemy) {
		this.intDamage = intDamage;
		this.intxLocation = tower.intxLocation + (Game.TILE_SIZE / 2);
		this.intyLocation = tower.intyLocation + (Game.TILE_SIZE / 2);
		this.intRadius = intRadius;
		this.intProjectileSpeed = intProjectileSpeed;
		this.color = color;
		this.currentEnemy = currentEnemy;
	}

}
