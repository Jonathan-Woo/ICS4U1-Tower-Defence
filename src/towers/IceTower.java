package towers;

import enemies.Enemy;

public class IceTower extends Tower{

	public IceTower(int intxLocation, int intyLocation) {
		super(intxLocation, intyLocation, Tower.ICE);
	}

	@Override
	public void effectOnHit(Enemy enemy) {
		enemy.intSpeed /= 2;
	}
}
