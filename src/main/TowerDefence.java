package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import networking.Connections;
import states.Game;
import states.GameCreation;
import states.GameOver;
import states.Help;
import states.MainMenu;
import states.Settings;
import states.State;

//MAIN CLASS WITH MAIN METHOD THAT RUNS THE MAIN GAME LOOP, UPDATES, AND RENDER THE CURRENT GAME STATE

//MAKE THIS MAIN CLASS THE JFRAME SO WE DON'T
//NEED A SEPARATE OBJECT FOR THAT
public class TowerDefence extends JFrame implements ActionListener, WindowListener {
	
	public final static int MAIN_MENU = 0, SETTINGS = 1, GAME_CREATION = 2, GAME = 3, GAME_OVER = 4, HELP = 5;
	public final static int WIDTH = 1280, HEIGHT = 720;
	
	/** Keeps track of the current game State */	
	private State currentState;
	private AnimationPanel pnl;
	private Timer timer;
	public static Font font = new Font("Arial", Font.PLAIN, 36);
	
	///////MAIN METHOD HERE///////
	public static void main(String[] args) {
		new TowerDefence();
	}
	
	public TowerDefence() {
		//INIT JFRAME
		super("TowerField");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(this);
		//SHOW FRAME IN THE MIDDLE OF THE SCREEN ON CREATION
		//this.setLocationRelativeTo(null);
		
		//SHOW FRAME
		this.setVisible(true);
		
		//INIT ANIMATION PANEL
		pnl = new AnimationPanel(this);
		this.setContentPane(pnl);
		this.pack();
		
		//INIT DEFAULT STATE OF THE GAME
		currentState = new MainMenu(this);
		
		//SET INPUT LISTENER
		InputListener inputListener = new InputListener(this.getInsets().top);
		this.addKeyListener(inputListener);
		this.addMouseListener(inputListener);
		this.addMouseMotionListener(inputListener);
		
		//START TIMER
		timer = new Timer(1000 / 60, this);
		timer.start();
	}
	
	public void resetPanel() {
		this.pnl.removeAll();
	}
	
	@Override
	public Component add(Component component) {
		Component comp = this.pnl.add(component);
		this.pack();
		return comp;
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
	
	//CHANGE GAME STATE, PASSING ON THE GIVEN ARGUMENTS TO THE NEW STATE
	public State changeState(final int state, Object... args) {
		//REMOVE ALL JCOMPONENTS FORM JPANEL BEFORE ADDING NEW ONES
		resetPanel();
		switch(state) {
			case TowerDefence.MAIN_MENU:
				this.currentState = new MainMenu(this);
				break;
			case TowerDefence.GAME_CREATION:
				this.currentState = new GameCreation(this);
				break;
			case TowerDefence.GAME:
				this.currentState = new Game(this, (String) args[0]);
				this.requestFocus();
				break;
			case TowerDefence.SETTINGS:
				this.currentState = new Settings(this);
				break;
			case TowerDefence.GAME_OVER:
				this.currentState = new GameOver(this, (int) args[0]);
				break;
			case TowerDefence.HELP:
				this.currentState = new Help(this);
				break;
		}
		return this.currentState;
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
		
		//ANIMATION PANEL CONSTRUCTOR
		public AnimationPanel(TowerDefence towerDefence) {
			this.setPreferredSize(new Dimension(TowerDefence.WIDTH, TowerDefence.HEIGHT + towerDefence.getInsets().top));
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

	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		//DISCONNECT FROM THE OTHER SIDE WHEN THE USER TRIES TO CLOSE THE GAME
		Connections.closeConnection();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}

}
