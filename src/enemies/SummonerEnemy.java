package enemies;

import main.Utils;
import networking.Connections;
import states.Game;

public class SummonerEnemy extends Enemy {
	
	long longSummonTime = 0;
	
	public SummonerEnemy(String id) {
		super("summonerEnemy", Enemy.SUMMONER, id);
	}
	
	@Override
	public void update(Game game) {
		super.update(game);
		if(Connections.isServer) {
			if(System.currentTimeMillis() - longSummonTime >= 1500) {
				String newQuickId = Utils.genId();
				Connections.sendMessage(Connections.SPAWN_ENEMY, Enemy.QUICK, newQuickId,
						this.intxLocation, this.intyLocation);
				game.enemies.add(new QuickEnemy(newQuickId));
				longSummonTime = System.currentTimeMillis();
			}
		}
	}

}
