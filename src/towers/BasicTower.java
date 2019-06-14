package towers;

import enemies.Enemy;
import main.Utils;

public class BasicTower extends Tower{

	/**
	 * Constructor for a Basic Tower.
	 * 
	 * @param intxLocation The X coordinate of the Tower.
	 * @param intyLocation The Y coordinate of the Tower.
	 */
	public BasicTower(int intxLocation, int intyLocation, String id) {
		super(intxLocation, intyLocation, Tower.BASIC, id);
	}

	//Apply basic tower effect
	public void effectOnHit(Enemy enemy) {
		//Does nothing on hit
	}

}
