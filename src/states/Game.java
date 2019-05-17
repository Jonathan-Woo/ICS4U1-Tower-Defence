package states;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import enemies.Enemy;
import projectiles.Projectile;
import towers.Tower;

public class Game extends State{

	Tower[] towers;
	Enemy[] enemies;
	public ArrayList<Projectile> projectiles;
	
	@Override
	public void update() {
		for(int i = 0; i < towers.length; i++) {
			towers[i].update(this);
		}
		
		for(int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update(this);
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
