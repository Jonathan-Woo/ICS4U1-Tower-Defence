package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

//THIS CLASS IS USED TO LISTEN TO ALL INPUT EVENTS FORM THE USER SUCH AS
//THE KEYBOARD AND MOSUE

public class InputListener implements KeyListener, MouseMotionListener, MouseListener{
	
	private int insets;
	public static boolean keys[] = new boolean[65555];
	public static boolean[] mouseButtons = new boolean[4];
	public static int mouseX, mouseY;
	public static boolean dragging = false;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseButtons[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseButtons[e.getButton()] = false;
		dragging = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		dragging = true;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY() - insets;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}
	
	//CONSTRUCTOR
	public InputListener(int insets) {
		this.insets = insets;
	}

}
