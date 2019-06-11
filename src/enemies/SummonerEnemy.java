package enemies;

import states.Game;

public class SummonerEnemy extends Enemy {
	
	long longSummonTime = 0;
	
	public SummonerEnemy(String id) {
		super("summonerEnemy", Enemy.SUMMONER, id);
	}
	
	@Override
	public void update(Game game) {
		super.update(game);
		if(System.currentTimeMillis() - longSummonTime >= 1000) {
			game.enemies.add(new QuickEnemy(id));
			longSummonTime = System.currentTimeMillis();
		}
	}

}
