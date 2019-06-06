package states;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import main.TowerDefence;

public class Settings extends State {
	
	public static int port = 3456;
	
	JTextField portField;
	JButton save, back;
	
	@Override
	public void update() {
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1280, 720);
	}
	
	public Settings(TowerDefence towerDefence) {
		JLabel lbl = new JLabel("Port: ");
		lbl.setLocation((towerDefence.getWidth() / 2) - 200, (towerDefence.getHeight() / 2));
		towerDefence.add(lbl);
		
		portField = new JTextField(port + "");
		portField.setLocation((towerDefence.getWidth() / 2) - 150, (towerDefence.getHeight() / 2));
		portField.setPreferredSize(new Dimension(300, 30));
		towerDefence.add(portField);
	}

}
