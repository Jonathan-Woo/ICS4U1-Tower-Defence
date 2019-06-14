package towers;

import enemies.Enemy;

//TOWER: SNIPE TYPE

public class SnipeTower extends Tower{

	public SnipeTower(int intxLocation, int intyLocation, String id) {
		super(intxLocation, intyLocation, Tower.SNIPE, id);
	}

	@Override
	public void effectOnHit(Enemy enemy) {
		
	}

	
}
