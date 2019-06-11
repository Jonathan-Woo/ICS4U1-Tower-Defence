package networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import enemies.Enemy;
import main.TowerDefence;
import states.Game;
import states.Settings;
import towers.Tower;

public class Connections implements ActionListener{
	
	//properties
	public static final int CONNECT = 0, DISCONNECT = 1, CHAT_MESSAGE = 2, PLACE_TOWER = 3, STAT_UPDATE = 4,
			SPAWN_ENEMY = 5, UPDATE_TIMER = 6, UPDATE_ENEMY = 7, REMOVE_ENEMY = 8;
	
	private TowerDefence towerDefence;
	private Game game;
	private static SuperSocketMaster ssm;
	public static boolean isServer, blnConnected = false;
	
	//methods
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ssm) {
			String strText = ssm.readText();
			String strMessageParts[] = strText.split(",");
			int intMessageType = Integer.parseInt(strMessageParts[0]);
			
			if(intMessageType == CONNECT) {
				if(!blnConnected) {
					if(isServer) {
						sendMessage(Connections.CONNECT, "0");
					}
					blnConnected = true;
					game = (Game) towerDefence.changeState(TowerDefence.GAME);
				}
			}else if(intMessageType == DISCONNECT) {
				Connections.closeConnection();
				game = null;
				towerDefence.changeState(TowerDefence.MAIN_MENU);
			}else if(intMessageType == CHAT_MESSAGE) {
				Game.strMessageReceived = strMessageParts[1];
			}else if(intMessageType == Connections.PLACE_TOWER) {
				int placeTower = Integer.parseInt(strMessageParts[1]);
				int towerX = Integer.parseInt(strMessageParts[2]);
				int towerY = Integer.parseInt(strMessageParts[3]);
				if(isServer) {
					game.placeTower(placeTower, towerX, towerY, false);
				}else {
					game.towers.add(Tower.newTower(placeTower, towerX, towerY));
				}
			}else if(intMessageType == Connections.STAT_UPDATE) {
				if(!isServer) {
					game.intBalance = Integer.parseInt(strMessageParts[1]);
					game.intHealth = Integer.parseInt(strMessageParts[2]);
					game.checkIfGameOver();
				}
			}else if(intMessageType == Connections.SPAWN_ENEMY) {
				if(!isServer) {
					game.enemies.add(Enemy.newEnemy(Integer.parseInt(strMessageParts[1]), strMessageParts[2]));
				}
			}else if(intMessageType == Connections.UPDATE_TIMER) {
				if(!isServer) {
					game.roundTime = Integer.parseInt(strMessageParts[1]);
					game.waveNumber = Integer.parseInt(strMessageParts[2]);
					game.enemies.clear();
				}
			}else if(intMessageType == Connections.UPDATE_ENEMY) {
				if(!isServer) {
					for(Enemy enemy : game.enemies) {
						if(enemy.id.equals(strMessageParts[1])) {
							int checkPoint = Integer.parseInt(strMessageParts[2]);
							enemy.currentCheckpoint = checkPoint;
							enemy.intxLocation = game.map.getCheckpointX(checkPoint);
							enemy.intyLocation = game.map.getCheckpointY(checkPoint);
							return;
						}
					}
				}
			}else if(intMessageType == Connections.REMOVE_ENEMY) {
				if(!isServer) {
					for(Enemy enemy : game.enemies) {
						if(enemy.id.equals(strMessageParts[1])) {
							Game.removeEnemies.add(enemy);
							return;
						}
					}
				}
			}
		}
	}
	
	public static void sendMessage(int intType, Object... strMessages) {
		if(ssm != null) {
			String strFinalMsg = "" + intType;
			for(int i = 0; i < strMessages.length; i++) {
				strFinalMsg += "," + strMessages[i];
			}
			//System.out.println(strFinalMsg);
			ssm.sendText(strFinalMsg);
		}
	}
	
	public static void closeConnection() {
		if(ssm != null) {
			if(blnConnected) {
				sendMessage(Connections.DISCONNECT, "0");
			}			
			ssm.disconnect();
			ssm = null;
			//FORCE JAVA TO GARBAGE COLLECT
			System.gc();
		}
		blnConnected = false;
	}
	
	//constructor
	public Connections(TowerDefence towerDefence) {
		this.towerDefence = towerDefence;		
		Connections.isServer = true;
		ssm = new SuperSocketMaster(Settings.port, this);
		ssm.connect();
	}
	
	public Connections(String ipAddress, TowerDefence towerDefence) {
		this.towerDefence = towerDefence;
		Connections.isServer = false;
		ssm = new SuperSocketMaster(ipAddress, Settings.port, this);
		ssm.connect();
	}
	
}
