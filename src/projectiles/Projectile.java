package projectiles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import enemies.Enemy;
import states.Game;
import towers.Tower;

public class Projectile {
	//properties
	private int intDamage;
	private int intxLocation;
	private int intyLocation;
	private int intRadius;
	private int intProjectileSpeed;
	private Color color;
	private Enemy currentEnemy;
	private Tower tower;
	private long longSpawnTime;
	
	//methods
	public void update(Game game) {
		if((Math.abs(currentEnemy.intxLocation + (Game.TILE_SIZE / 2) - intxLocation) <= 5
				&& Math.abs(currentEnemy.intyLocation + (Game.TILE_SIZE / 2) - intyLocation) <= 5) ||
				(System.currentTimeMillis() - longSpawnTime >= 3000)) {
			//HIT THE ENEMY: DAMAGE ENEMY, REMOVE PROJECTILE
			game.projectiles.remove(this);
			currentEnemy.dealDamage(intDamage);
			tower.effectOnHit(currentEnemy);
		}else{
			//calculates the distance from the enemy
			int intPythagA;
			int intPythagB;
			double dblPythagC;
			intPythagA = (currentEnemy.intxLocation + (Game.TILE_SIZE / 2)) - this.intxLocation;
			intPythagB = (currentEnemy.intyLocation + (Game.TILE_SIZE/2)) - this.intyLocation;
			if(intPythagA == 0) {
				dblPythagC = 1.0;
			}else {
				//dblPythagC = Math.pow((intPythagA * intPythagA) + (intPythagB * intPythagB), 0.5);
				dblPythagC = (double) ((double) intPythagB / (double) Math.abs(intPythagA));
			}
			if(intPythagA > 0) {
				intxLocation += intProjectileSpeed;
			}else if(intPythagA < 0) {
				intxLocation -= intProjectileSpeed;
			}
			//intyLocation += (int)Math.pow(((dblPythagC * dblPythagC) - 1), 0.5);
			intyLocation += (int) (dblPythagC * intProjectileSpeed);
		}
	}
	
	public void render(Graphics g){
		g.setColor(color);
		g.fillOval(intxLocation - (intRadius / 2), intyLocation - (intRadius / 2), intRadius, intRadius);
	}
	
	//constructor
	public Projectile(int intDamage, Tower tower, int intRadius, int intProjectileSpeed, Color color, Enemy currentEnemy) {
		this.intDamage = intDamage;
		this.tower = tower;
		this.intxLocation = tower.intxLocation + (Game.TILE_SIZE / 2);
		this.intyLocation = tower.intyLocation + (Game.TILE_SIZE / 2);
		this.intRadius = intRadius;
		this.intProjectileSpeed = intProjectileSpeed;
		this.color = color;
		this.currentEnemy = currentEnemy;

		longSpawnTime = System.currentTimeMillis();
	}

}
