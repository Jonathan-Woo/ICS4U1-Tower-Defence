package states;

import java.awt.Graphics;

import enemies.Enemy;
import towers.Tower;

public class Game extends State{

	Tower[] towers;
	Enemy[] enemies;
	
	@Override
	public void update() {
		for(int i = 0; i < towers.length; i++) {
			towers[i].update(this);
		}
		
		for(Tower tower : towers) {
			tower.update(this);
		}
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	public Enemy[] getEnemies() {
		return enemies;
	}

}
