package states;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

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
		playGameButton.setBounds(TowerDefence.wi);
		
		helpButton = new JButton("Help");
		
		settingsButton = new JButton("Settings");
	}

	
}
