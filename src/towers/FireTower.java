package towers;

import java.util.ArrayList;
import enemies.Enemy;
import states.Game;

public class FireTower extends Tower{
	
	long longFireTick = 0;
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	
	public FireTower(int intxLocation, int intyLocation) {
		super(intxLocation, intyLocation, Tower.FIRE);
	}

	@Override
	public void update(Game game){
		super.update(game);
		if(System.currentTimeMillis() - longFireTick >= 500) {
			ArrayList<Enemy> rmEnemies = new ArrayList<Enemy>();
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
	
	@Override
	public void effectOnHit(Enemy enemy) {
		enemy.FIRE_EFFECT = true;
		if(!enemies.contains(enemy)) {
			enemies.add(enemy);
		}
	}
}
