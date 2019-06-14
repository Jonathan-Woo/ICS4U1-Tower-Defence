package states;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import main.TowerDefence;
import main.Utils;
import networking.Connections;

public class GameCreation extends State implements ActionListener {
	
	public static String selectedMap;
	
	JTextField ipAddressField;
	JButton createGameBtn, connectToGameBtn, backBtn;
	JComboBox mapSelection;
	BufferedImage gameCreationImg;
	
	@Override
	public void update() {
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(gameCreationImg,0,0,null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(createGameBtn)) {
			//CREATE SERVER
			new Connections(towerDefence);
		}else if(e.getSource().equals(connectToGameBtn)) {
			//JOIN SERVER
			String ipAddress = ipAddressField.getText();
			if(!ipAddress.isEmpty()) {
				//ATTEMPT TO CONNECT TO SERVER
				new Connections(ipAddress, towerDefence);
				Connections.sendMessage(Connections.CONNECT, "0");
				
				//IF NO RESPONSE AFTER 10 SECS, RETURN CONTROL TO USER
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						if(!Connections.blnConnected && GameCreation.this != null) {
							Connections.closeConnection();
							mapSelection.setEnabled(true);
							createGameBtn.setEnabled(true);
							connectToGameBtn.setEnabled(true);
							backBtn.setEnabled(true);
						}						
						this.cancel();
					}
				}, 10000);
			}
		}else if(e.getSource().equals(backBtn)) {
			//GO BACK TO MAIN MENU
			this.towerDefence.changeState(TowerDefence.MAIN_MENU);
		}
		selectedMap = (String) mapSelection.getSelectedItem();
		mapSelection.setEnabled(false);
		createGameBtn.setEnabled(false);
		connectToGameBtn.setEnabled(false);
		backBtn.setEnabled(false);
	}
	
	public GameCreation(TowerDefence towerDefence) {
		super(towerDefence);
		
		JLabel lbl = new JLabel("Server IP Address:");
		lbl.setBounds((TowerDefence.WIDTH / 2) - 150, (TowerDefence.HEIGHT / 2) - 120, 300, 50);
		
		ipAddressField = new JTextField("localhost");
		ipAddressField.setBounds((TowerDefence.WIDTH / 2) - 150, (TowerDefence.HEIGHT / 2) - 70, 300, 30);

		createGameBtn = new JButton("Create Game");
		createGameBtn.setBounds((TowerDefence.WIDTH / 2) - 320, (TowerDefence.HEIGHT / 2) - 20, 300, 50);
		createGameBtn.addActionListener(this);
		
		connectToGameBtn = new JButton("Connect To Game");
		connectToGameBtn.setBounds((TowerDefence.WIDTH / 2) + 20, (TowerDefence.HEIGHT / 2) - 20, 300, 50);
		connectToGameBtn.addActionListener(this);
		
		backBtn = new JButton("Back");
		backBtn.setBounds((TowerDefence.WIDTH / 2) - 75, (TowerDefence.HEIGHT / 2)  + 50, 150, 30);
		backBtn.addActionListener(this);
		
		JLabel mapSelectionLabel = new JLabel("Choose Map:");
		mapSelectionLabel.setBounds((TowerDefence.WIDTH / 2) - 150, (TowerDefence.HEIGHT / 2) - 220, 300, 50);
		
		mapSelection = new JComboBox<>(Utils.findMaps());
		mapSelection.setBounds((TowerDefence.WIDTH / 2) - 150, (TowerDefence.HEIGHT / 2) - 170, 300, 30);
		
		towerDefence.add(lbl);
		towerDefence.add(ipAddressField);
		towerDefence.add(createGameBtn);
		towerDefence.add(connectToGameBtn);
		towerDefence.add(backBtn);
		towerDefence.add(mapSelection);
		towerDefence.add(mapSelectionLabel);
		
		gameCreationImg = Utils.loadImage("Main Menu/" + "Settings.png");
	}

}
