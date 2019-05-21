package states;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import enemies.Enemy;
import projectiles.Projectile;
import towers.Tower;

public class Game extends State{

	public ArrayList<Tower> towers;
	public ArrayList<Enemy> enemies;
	public ArrayList<Projectile> projectiles;
	
	@Override
	public void update() {
		for(int i = 0; i < towers.size(); i++) {
			towers.get(i).update(this);
		}
		
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update(this);
		}
		
		for(int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update(this);
		}
	}

	@Override
	public void render(Graphics g) {
		for(int i = 0; i < towers.size(); i++) {
			towers.get(i).render(g);
		}
		
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).render(g);
		}
		
		for(int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).render(g);
		}
	}

}
