package states;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
		/*g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1280, 720);*/
	}
	
	public Settings(TowerDefence towerDefence) {
		JPanel pnl = new JPanel(null);
		pnl.setPreferredSize(towerDefence.getPreferredSize());
		
		JLabel lbl = new JLabel("Port: ");
		lbl.setLocation((TowerDefence.WIDTH / 2) - 200, TowerDefence.HEIGHT);
		pnl.add(lbl);
		
		portField = new JTextField(port + "");
		portField.setBounds((TowerDefence.WIDTH / 2) - 150, TowerDefence.HEIGHT, 300, 30);
		pnl.add(portField);
		
		JButton btn = new JButton("BACK");
		btn.setBounds((TowerDefence.WIDTH / 2) - 25, TowerDefence.HEIGHT + 50, 50, 30);
		pnl.add(btn);
		
		towerDefence.add(pnl);
	}

}
