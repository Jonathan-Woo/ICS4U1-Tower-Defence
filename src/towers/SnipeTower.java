package towers;

import enemies.Enemy;

//TOWER: SNIPE TYPE

public class SnipeTower extends Tower{

	public SnipeTower(int intxLocation, int intyLocation, String id) {
		super(intxLocation, intyLocation, Tower.SNIPE, id);
	}

	//Apply snipe tower effect
	public void effectOnHit(Enemy enemy) {
		//Does nothing on hit
	}

	
}
