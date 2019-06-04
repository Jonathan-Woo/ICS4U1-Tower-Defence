package networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import states.Game;

public class Connections implements ActionListener{
	
	//properties
	static SuperSocketMaster ssm;
	public static boolean blnIsServer;
	public static final int CONNECT = 0, DISCONNECT = 1, CHATMESSAGE = 2;
	
	//methods
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ssm) {
			String strText = ssm.readText();
			String strMessageParts[] = strText.split(",");
			int intMessageType = Integer.parseInt(strMessageParts[0]);
			if(intMessageType == CONNECT) {
				
			}else if(intMessageType == DISCONNECT) {
				
			}else if(intMessageType == CHATMESSAGE) {
				Game.strMessage = strMessageParts[1];
			}
		}
	}
	
	public static void sendMessage(int intType, String... strMessages) {
		String strFinalMsg = "" + intType;
		for(int i = 0; i < strMessages.length; i++) {
			strFinalMsg += "," + strMessages[i];
		}
		
		ssm.sendText(strFinalMsg);
	} 
	
	//constructor

	
}
