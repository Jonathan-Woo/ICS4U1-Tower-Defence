package states;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.TowerDefence;

public class Settings extends State implements ActionListener{
	
	public static int port = 3456;
	
	JTextField portField;
	JButton back;
	
	@Override
	public void update() {
		
	}

	@Override
	public void render(Graphics g) {
		
	}
	
	public Settings(TowerDefence towerDefence) {
		super(towerDefence);
		
		JLabel lbl = new JLabel("Port: ");
		lbl.setBounds((TowerDefence.WIDTH / 2) - 200, TowerDefence.HEIGHT / 2, 50, 30);
		towerDefence.add(lbl);
		
		portField = new JTextField(port + "");
		portField.setBounds((TowerDefence.WIDTH / 2) - 150, TowerDefence.HEIGHT / 2, 300, 30);
		towerDefence.add(portField);
		
		back = new JButton("BACK");
		back.setBounds((TowerDefence.WIDTH / 2) - 50 - 100, (TowerDefence.HEIGHT / 2) + 50, 100, 30);
		back.addActionListener(this);
		towerDefence.add(back);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(back)) {
			int port = Integer.parseInt(portField.getText());
			if(port >= 0 && port <= 65255) {
				Settings.port = port;
				towerDefence.changeState(TowerDefence.MAIN_MENU);
			}
		}
	}

}
