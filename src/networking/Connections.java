package networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import enemies.Enemy;
import main.TowerDefence;
import states.Game;
import states.GameCreation;
import states.Settings;
import towers.Tower;

public class Connections implements ActionListener{
	
	//properties
	public static final int CONNECT = 0, DISCONNECT = 1, CHAT_MESSAGE = 2, PLACE_TOWER = 3, STAT_UPDATE = 4,
			SPAWN_ENEMY = 5, UPDATE_TIMER = 6, UPDATE_ENEMY = 7, REMOVE_ENEMY = 8, UPDATE_TOWER = 9, REMOVE_TOWER = 10;
	
	private TowerDefence towerDefence;
	private Game game;
	private static SuperSocketMaster ssm;
	public static boolean isServer, blnConnected = false;
	
	//methods
	public synchronized void actionPerformed(ActionEvent e) {
		if(e.getSource() == ssm) {
			String strText = ssm.readText();
			String strMessageParts[] = strText.split(",");
			int intMessageType = Integer.parseInt(strMessageParts[0]);
			
			if(intMessageType == CONNECT) {
				if(!blnConnected) {
					if(isServer) {
						sendMessage(Connections.CONNECT, GameCreation.selectedMap);
					}else {
						GameCreation.selectedMap = strMessageParts[1];
					}
					blnConnected = true;
					game = (Game) towerDefence.changeState(TowerDefence.GAME, GameCreation.selectedMap);
				}
			}else if(intMessageType == DISCONNECT) {
				towerDefence.changeState(TowerDefence.GAME_OVER, game.waveNumber);
				game = null;
			}else if(intMessageType == CHAT_MESSAGE) {
				if(Connections.isServer) {
					Game.strMessageReceived = "Client: " + strMessageParts[1];
				}else {
					Game.strMessageReceived = "Server: " + strMessageParts[1];
				}
			}else if(intMessageType == Connections.PLACE_TOWER) {
				int placeTower = Integer.parseInt(strMessageParts[1]);
				int towerX = Integer.parseInt(strMessageParts[2]);
				int towerY = Integer.parseInt(strMessageParts[3]);
				if(isServer) {
					game.placeTower(placeTower, towerX, towerY, false);
				}else {
					game.towers.add(Tower.newTower(placeTower, towerX, towerY, strMessageParts[4]));
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
			}else if(intMessageType == Connections.UPDATE_TOWER) {
				if(!isServer) {
					for(Tower tower : game.towers) {
						if(tower.id.equals(strMessageParts[1])) {
							tower.damageUpgrades = Integer.parseInt(strMessageParts[2]);
							tower.rangeUpgrades = Integer.parseInt(strMessageParts[3]);
							tower.speedUpgrades = Integer.parseInt(strMessageParts[4]);
							
							tower.intAttackDamage = Integer.parseInt(strMessageParts[5]);
							tower.intAttackSpeed = Integer.parseInt(strMessageParts[6]);
							tower.intRange = Integer.parseInt(strMessageParts[7]);
							return;
						}
					}
				}else {
					for(Tower tower : game.towers) {
						if(tower.id.equals(strMessageParts[1])) {
							int upgrade = Integer.parseInt(strMessageParts[2]);
							if(game.intBalance >= tower.getUpgradePrice(upgrade)) {
								tower.upgrade(upgrade);
							}
							return;
						}
					}
				}
			}else if(intMessageType == Connections.REMOVE_TOWER) {
				if(isServer) {
					Connections.sendMessage(Connections.REMOVE_TOWER, strMessageParts[1]);
				}
				
				for(Tower tower : game.towers) {
					if(tower.id.equals(strMessageParts[1])) {
						Game.removeTowers.add(tower);
						if(game.selectedTower.id.equals(strMessageParts[1])) {
							game.selectedTower = null;
						}
						return;
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
