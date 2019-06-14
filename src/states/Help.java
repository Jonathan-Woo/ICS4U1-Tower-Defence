package states;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import main.TowerDefence;
import main.Utils;

//HELP SCREEN

public class Help extends State implements ActionListener{
	
	BufferedImage helpImage;
	JButton backBtn = new JButton("Back");

	public void update() {
		
	}

	public void render(Graphics g) {
		helpImage = Utils.loadImage("Main Menu/"+"help.png");
		g.drawImage(helpImage, 0, -10, null);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==backBtn) {
			towerDefence.changeState(TowerDefence.MAIN_MENU);
		}
	}
	
	public Help (TowerDefence towerDefence) {
		super(towerDefence);
		backBtn.setBounds(350,650,150, 50);
		backBtn.addActionListener(this);
		towerDefence.add(backBtn);
	}
	

}
