package states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import main.TowerDefence;
import main.Utils;
import networking.Connections;

//GAME OVER SCREEN

public class GameOver extends State implements ActionListener{
	
	BufferedImage gameOverImg;
	JButton mainMenuButton;
	int intWave;

	public void update() {
		
	}

	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawImage(gameOverImg,0,0,null);
		g.drawString("" + intWave, TowerDefence.WIDTH/2, 9*Game.TILE_SIZE + 20);
	}
	
	public void actionPerformed(ActionEvent e) {
		towerDefence.changeState(TowerDefence.MAIN_MENU);
	}
	
	public GameOver(TowerDefence towerDefence, int intWave) {
		super(towerDefence);
		
		//DISCONNECT FROM THE OTHER SIDE
		Connections.closeConnection();
		
		mainMenuButton = new JButton("Main Menu");
		mainMenuButton.setBounds(TowerDefence.WIDTH/2 - 150, 13 * Game.TILE_SIZE, 300, 50);
		mainMenuButton.addActionListener(this);
		
		towerDefence.add(mainMenuButton);

		this.intWave = intWave;
		gameOverImg = Utils.loadImage("game over/"+"GameOver.png");
	}

	

}
