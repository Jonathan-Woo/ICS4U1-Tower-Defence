package states;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

import main.TowerDefence;
import main.Utils;

//MAIN MENU SCREEN

public class MainMenu extends State implements ActionListener{
	
	//properties
	JButton playGameButton;
	JButton helpButton;
	JButton settingsButton;
	
	BufferedImage titleImg;
	BufferedImage sidebarImg;
	
	//methods
	public void update() {
		
	}

	public void render(Graphics g) {
		g.drawImage(titleImg,0,0,null);
		g.drawImage(sidebarImg, 0,200,null);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == playGameButton) {
			//GO TO GAME CREATION SCREEN
			towerDefence.changeState(TowerDefence.GAME_CREATION);
		}else if(e.getSource() == helpButton) {
			//GO TO HELP SCREEN
			towerDefence.changeState(TowerDefence.HELP);
		}else if(e.getSource() == settingsButton) {
			//GO TO SETTINGS SCREEN
			towerDefence.changeState(TowerDefence.SETTINGS);
		}
	}

	//constructor
	public MainMenu(TowerDefence towerDefence) {
		super(towerDefence);
	
		playGameButton = new JButton ("Play Game");
		playGameButton.setBounds(TowerDefence.WIDTH/4, 5 * Game.TILE_SIZE, TowerDefence.WIDTH/2, 3 * Game.TILE_SIZE);
		playGameButton.addActionListener(this);
		
		helpButton = new JButton("Help");
		helpButton.setBounds(TowerDefence.WIDTH/4, 8 * Game.TILE_SIZE, TowerDefence.WIDTH/2, 3 * Game.TILE_SIZE);
		helpButton.addActionListener(this);
		
		settingsButton = new JButton("Settings");
		settingsButton.setBounds(TowerDefence.WIDTH/4, 11 * Game.TILE_SIZE, TowerDefence.WIDTH/2, 3 * Game.TILE_SIZE);
		settingsButton.addActionListener(this);
		
		towerDefence.add(playGameButton);
		towerDefence.add(helpButton);
		towerDefence.add(settingsButton);
		
		titleImg = Utils.loadImage("main menu/"+"Title.png");
		sidebarImg = Utils.loadImage("main menu/"+"SideBar.png");
	}

	
}
