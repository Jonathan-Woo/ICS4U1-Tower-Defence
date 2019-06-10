package towers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import enemies.Enemy;

public class FireTower extends Tower{
	
	Timer timer;
	
	public FireTower(int intxLocation, int intyLocation) {
		super(intxLocation, intyLocation, Tower.FIRE);
	}

	@Override
	public void effectOnHit(Enemy enemy) {
		timer = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(enemy.intHealth <= 0) {
					timer.stop();
				}else {
					enemy.dealDamage(intAttackDamage / 4);
				}
			}
		});
		timer.start();
	}
}
