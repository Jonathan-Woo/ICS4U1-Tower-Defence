package towers;

import enemies.Enemy;

public class SnipeTower extends Tower{

	public SnipeTower(int intxLocation, int intyLocation) {
		super(intxLocation, intyLocation, Tower.SNIPE);
	}

	@Override
	public void effectOnHit(Enemy enemy) {
		
	}

	
}
