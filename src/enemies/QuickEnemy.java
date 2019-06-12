package enemies;

import main.Utils;

public class QuickEnemy extends Enemy {

	public QuickEnemy(String id) {
		super("quickEnemy", Enemy.QUICK, id);
	}
	
	public QuickEnemy(String id, SummonerEnemy summoner) {
		super("quickEnemy", Enemy.QUICK, id);
		this.intxLocation = summoner.intxLocation;
		this.intyLocation = summoner.intyLocation;
		this.currentCheckpoint = summoner.currentCheckpoint;
	}

}
