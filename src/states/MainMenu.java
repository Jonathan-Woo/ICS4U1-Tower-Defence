package states;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import main.TowerDefence;

public class MainMenu extends State implements ActionListener{
	
	//properties
	JButton playGameButton;
	JButton helpButton;
	JButton settingsButton;
	
	//methods

	public void update() {
		
	}

	public void render(Graphics g) {
		
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}

	//constructor
	public MainMenu() {
		playGameButton = new JButton ("Play Game");
		playGameButton.setBounds(TowerDefence.WIDTH/4, 3 * Game.TILE_SIZE, TowerDefence.WIDTH/2, 3 * Game.TILE_SIZE);
		
		helpButton = new JButton("Help");
		helpButton.setBounds(TowerDefence.WIDTH/4, 3 * Game.TILE_SIZE, TowerDefence.WIDTH/2, 8 * Game.TILE_SIZE);
		
		settingsButton = new JButton("Settings");
		settingsButton.setBounds(TowerDefence.WIDTH/4, 3 * Game.TILE_SIZE, TowerDefence.WIDTH/2, 13 * Game.TILE_SIZE);
	}

	
}
