package networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import states.Game;

public class Connections implements ActionListener{
	
	//properties
	SuperSocketMaster ssm;
	boolean blnIsServer;
	public static final int CONNECT = 0, DISCONNECT = 1, SENDMESSAGE = 2;
	
	//methods
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ssm) {
			String strText = ssm.readText();
			String strMessageParts[] = strText.split(",");
			int intMessageType = Integer.parseInt(strMessageParts[0]);
			if(intMessageType == CONNECT) {
			}else if(intMessageType == DISCONNECT) {
			}else if(intMessageType == SENDMESSAGE) {
				Game.strMessage = strMessageParts[1];
			}
		}
	}
	
	//constructor

	
}
