package towers;

import java.util.ArrayList;
import enemies.Enemy;
import states.Game;

//TOWER: FIRE TYPE

public class FireTower extends Tower{
	
	long longFireTick = 0;
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	
	public FireTower(int intxLocation, int intyLocation, String id) {
		super(intxLocation, intyLocation, Tower.FIRE, id);
	}

	//Overide update method
	public void update(Game game){
		super.update(game);
		//HURT ENEMIES HIT EVERY FEW TICKS
			if(System.currentTimeMillis() - longFireTick >= intAttackSpeed / 2) {
			//Add fire effect
			if(System.currentTimeMillis() - longFireTick >= 500) {
				ArrayList<Enemy> rmEnemies = new ArrayList<Enemy>();
				//Check for damage
				for(Enemy enemy : enemies) {
					if(enemy.intHealth <= 0) {
						rmEnemies.add(enemy);
					}else {
						enemy.dealDamage(intAttackDamage / 2);
					}
				}
				
				for(Enemy enemy : rmEnemies) {
					enemies.remove(enemy);
				}
				longFireTick = System.currentTimeMillis();
			}
		}
	}
	
	//Apply fire tower effect on enemy
	public void effectOnHit(Enemy enemy) {
		//WHEN PROJECTILE HITS ENEMY, KEEP TRACK OF IT SO WE CAN HURT IT OVER TIME
		//Does damage over time
		enemy.FIRE_EFFECT = true;
		if(!enemies.contains(enemy)) {
			enemies.add(enemy);
		}
	}
}
