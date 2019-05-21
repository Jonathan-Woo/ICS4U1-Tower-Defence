package enemies;

import java.awt.Graphics;

import states.Game;

public abstract class Enemy {
	//properties
	public int intxLocation;
	public int intyLocation;
	
	//methods
	
	//constructor
	public Enemy() {
		
	}
	
	public void update(Game game) {
		
	}
	
	public abstract void render(Graphics g);
	
}
