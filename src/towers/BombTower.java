package towers;

import enemies.Enemy;

public class BombTower extends Tower{

	public BombTower(int intxLocation, int intyLocation) {
		super(intxLocation, intyLocation, Tower.BOMB);
	}

	@Override
	public void effectOnHit(Enemy enemy) {
		
	}

	
}
