package towers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import enemies.Enemy;

//TOWER: ICE TYPE

public class IceTower extends Tower{
	
	Timer timer;

	public IceTower(int intxLocation, int intyLocation, String id) {
		super(intxLocation, intyLocation, Tower.ICE, id);
	}

	@Override
	public void effectOnHit(Enemy enemy) {
		//WHEN PROJECTILE HITS AN ENEMY FREEZE THEM FOR A FEW SECONDS
		enemy.ICE_EFFECT = true;
		final double prevSpeed = enemy.intSpeed;
		enemy.intSpeed = 0;
		timer = new Timer(this.intAttackSpeed / 2, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enemy.intSpeed = prevSpeed;
				enemy.ICE_EFFECT = false;
				timer.stop();
			}
		});
		timer.start();
	}
}
