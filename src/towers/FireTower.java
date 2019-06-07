package towers;

import enemies.Enemy;

public class FireTower extends Tower{
	
	public FireTower(int intxLocation, int intyLocation) {
		super(intxLocation, intyLocation, Tower.FIRE);
	}

	@Override
	public void effectOnHit(Enemy enemy) {
		
	}
}
