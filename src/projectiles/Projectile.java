package projectiles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import enemies.Enemy;
import states.Game;
import towers.Tower;

//PROJECTILE THAT GETS SPAWNED BY EACH TOWER AND MOVES TOWARDS IT'S SPECIFIED ENEMY TARGET.

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
		if((Math.abs(currentEnemy.intxLocation + (Game.TILE_SIZE / 2) - intxLocation) <= intProjectileSpeed + intRadius
				&& Math.abs(currentEnemy.intyLocation + (Game.TILE_SIZE / 2) - intyLocation) <= intProjectileSpeed + intRadius) ||
				(System.currentTimeMillis() - longSpawnTime >= 2000)) {
			//HIT THE ENEMY: DAMAGE ENEMY, REMOVE PROJECTILE
			game.projectiles.remove(this);
			currentEnemy.dealDamage(intDamage);
			tower.effectOnHit(currentEnemy);
		}else{
			//CALCULATES HOW MUCH WE NEED TO MOVE IN THE Y DIRECTION BASED ON
			//HOW MUCH WE MOVE IN THE X DIRECTION
			int intDeltaY;
			int intDeltaX;
			double dblSlope;
			intDeltaX = (currentEnemy.intxLocation + (Game.TILE_SIZE / 2)) - this.intxLocation;
			intDeltaY = (currentEnemy.intyLocation + (Game.TILE_SIZE / 2)) - this.intyLocation;
			if(Math.abs(intDeltaX) <= 5) {
				intDeltaX = 5;
			}
			
			dblSlope = (double) ((double) intDeltaY / (double) Math.abs(intDeltaX));
			
			if(intDeltaX > 0) {
				intxLocation += intProjectileSpeed;
			}else if(intDeltaX < 0) {
				intxLocation -= intProjectileSpeed;
			}
			intyLocation += (int) (dblSlope * intProjectileSpeed);
		}
	}
	
	public void render(Graphics g){
		//DRAW CIRCLE OF SPECIFIED COLOR AND RADIUS
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
