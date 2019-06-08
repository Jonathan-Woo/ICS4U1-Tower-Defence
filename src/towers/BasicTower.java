package towers;

import enemies.Enemy;

public class BasicTower extends Tower{

	/**
	 * Constructor for a Basic Tower.
	 * 
	 * @param intxLocation The X coordinate of the Tower.
	 * @param intyLocation The Y coordinate of the Tower.
	 */
	public BasicTower(int intxLocation, int intyLocation) {
		super(intxLocation, intyLocation, Tower.BASIC);
	}

	@Override
	public void effectOnHit(Enemy enemy) {
		
	}

}
