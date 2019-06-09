package networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.TowerDefence;
import states.Game;
import states.Settings;

public class Connections implements ActionListener{
	
	//properties
	public static final int CONNECT = 0, DISCONNECT = 1, CHATMESSAGE = 2;
	
	private static TowerDefence towerDefence;
	private static SuperSocketMaster ssm;
	public static boolean blnIsServer, blnConnected = false;
	
	//methods
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ssm) {
			String strText = ssm.readText();
			String strMessageParts[] = strText.split(",");
			int intMessageType = Integer.parseInt(strMessageParts[0]);
			
			if(intMessageType == CONNECT) {
				if(blnIsServer && !blnConnected) {
					sendMessage(Connections.CONNECT, "0");
				}
				blnConnected = true;
				towerDefence.changeState(TowerDefence.GAME);
			}else if(intMessageType == DISCONNECT) {
				Connections.closeConnection();
				towerDefence.changeState(TowerDefence.MAIN_MENU);
			}else if(intMessageType == CHATMESSAGE) {
				Game.strMessage = strMessageParts[1];
			}
		}
	}
	
	public static void sendMessage(int intType, String... strMessages) {
		if(ssm != null) {
			String strFinalMsg = "" + intType;
			for(int i = 0; i < strMessages.length; i++) {
				strFinalMsg += "," + strMessages[i];
			}
			
			ssm.sendText(strFinalMsg);
		}
	}
	
	public static void closeConnection() {
		if(ssm != null) {
			sendMessage(Connections.DISCONNECT, "0");
			ssm.disconnect();
			ssm = null;
		}
		blnConnected = false;
	}
	
	//constructor
	public Connections(TowerDefence towerDefence) {
		Connections.towerDefence = towerDefence;		
		Connections.blnIsServer = true;
		ssm = new SuperSocketMaster(Settings.port, this);
		ssm.connect();
	}
	
	public Connections(String ipAddress, TowerDefence towerDefence) {
		Connections.towerDefence = towerDefence;
		Connections.blnIsServer = false;
		ssm = new SuperSocketMaster(ipAddress, Settings.port, this);
		ssm.connect();
	}
	
}
