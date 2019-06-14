package towers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import enemies.Enemy;

public class IceTower extends Tower{
	
	Timer timer;

	public IceTower(int intxLocation, int intyLocation, String id) {
		super(intxLocation, intyLocation, Tower.ICE, id);
	}

	//Apply ice tower effect
	public void effectOnHit(Enemy enemy) {
		enemy.ICE_EFFECT = true;
		//Slows enemy temporarily on hit
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
