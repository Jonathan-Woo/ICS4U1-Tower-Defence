package states;

import java.awt.Graphics;

/** BLUEPRINT FOR ALL STATE CLASSES */
public abstract class State {

	/** UPDATE METHOD THAT GETS CALLED EVERY GAME LOOP */
	public abstract void update();
	
	/** RENDER METHOD TO DRAW GRAPHICS TO SCREEN */
	public abstract void render(Graphics g);
	
}
