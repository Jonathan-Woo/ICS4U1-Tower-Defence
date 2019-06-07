package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import states.Game;
import states.GameCreation;
import states.GameOver;
import states.MainMenu;
import states.Settings;
import states.State;

//MAKE THIS MAIN CLASS THE JFRAME SO WE DON'T
//NEED A SEPARATE OBJECT FOR THAT
public class TowerDefence extends JFrame implements ActionListener {
	
	public final static int MAIN_MENU = 0, SETTINGS = 1, GAME_CREATION = 2, GAME = 3, GAME_OVER = 4;
	public final static int WIDTH = 1280, HEIGHT = 720;
	
	/** Keeps track of the current game State */	
	private State currentState;
	private AnimationPanel pnl;
	private Timer timer;
	public static Font font;
	
	public static void main(String[] args) {
		new TowerDefence();
	}
	
	public TowerDefence() {
		//INIT JFRAME
		super("Tower Defence");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//SHOW FRAME IN THE MIDDLE OF THE SCREEN ON CREATION
		//this.setLocationRelativeTo(null);
		
		//INIT ANIMATION PANEL
		pnl = new AnimationPanel(this);
		this.setContentPane(pnl);
		
		//SHOW FRAME
		this.pack();
		this.setVisible(true);
		
		//INIT DEFAULT STATE OF THE GAME
		currentState = new Settings(this);
		
		//SET INPUT LISTENER
		InputListener inputListener = new InputListener(this.getInsets().top);
		this.addKeyListener(inputListener);
		this.addMouseListener(inputListener);
		this.addMouseMotionListener(inputListener);
		
		//INIT FONT
		font = new Font("Arial", Font.PLAIN, 36);
		
		//START TIMER
		timer = new Timer(1000 / 60, this);
		timer.start();
	}
	
	public void resetPanel() {
		this.pnl.removeAll();
	}
	
	@Override
	public Component add(Component component) {
		return this.pnl.add(component);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return pnl.getPreferredSize();
	}
	
	/**
	 * Returns the current State of the game.
	 * 
	 * @return The current State object the game is using.
	 */
	public State getCurrentState() {
		return currentState;
	}
	
	public void changeState(final int state, Object... args) {
		switch(state) {
			case TowerDefence.MAIN_MENU:
				this.currentState = new MainMenu(this);
			case TowerDefence.GAME_CREATION:
				this.currentState = new GameCreation(this);
			case TowerDefence.GAME:
				this.currentState = new Game(this);
			case TowerDefence.SETTINGS:
				this.currentState = new MainMenu(this);
			case TowerDefence.GAME_OVER:
				this.currentState = new GameOver(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(timer) && currentState != null) {
			//UPDATE STATE
			currentState.update();
			
			//REPAINT ANIMATION PANEL
			pnl.repaint();
		}
	}
	
	//WE'LL MAKE THE ANIMATION PANEL CLASS IN HERE
	//SINCE IT'S SO SMALL AND WE ONLY USE IT HERE
	//WE DON'T NEED TO CREATE A WHOLE NEW FILE FOR IT
	private class AnimationPanel extends JPanel {
		
		private TowerDefence towerDefence;
		
		public AnimationPanel(TowerDefence towerDefence) {
			this.setPreferredSize(new Dimension (TowerDefence.WIDTH, TowerDefence.HEIGHT));
			this.setLayout(null);
			this.towerDefence = towerDefence;
		}
		
		@Override
		public void paintComponent(Graphics g) {
			if(currentState != null) {
				//CLEAR PANEL
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				
				//SET FONT
				g.setFont(font);
				
				//RENDER BASED ON STATE
				towerDefence.getCurrentState().render(g);
			}
		}
		
	}

}
