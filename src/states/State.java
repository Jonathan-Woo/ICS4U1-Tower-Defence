package states;

import java.awt.Graphics;

import main.TowerDefence;

/** BLUEPRINT FOR ALL STATE CLASSES */
public abstract class State {
	
	protected TowerDefence towerDefence;

	/** UPDATE METHOD THAT GETS CALLED EVERY GAME LOOP */
	public abstract void update();
	
	/** RENDER METHOD TO DRAW GRAPHICS TO SCREEN */
	public abstract void render(Graphics g);
	
	public State(TowerDefence towerDefence) {
		this.towerDefence = towerDefence;
	}
	
}
