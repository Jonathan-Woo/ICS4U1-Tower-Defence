package networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import enemies.Enemy;
import main.TowerDefence;
import states.Game;
import states.GameCreation;
import states.Settings;
import towers.Tower;

//THIS CLASS IS USED TO CONNECT AND DISCONNECT FROM ANOTHER COMPUTER,
//AND RECEIVE ALL NETWORKING MESSAGES AND PERFORM ACTIONS BASED ON THEM

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
			//GET INCOMING MESSAGE TYPE, AND MESSAGE PARTS
			String strText = ssm.readText();
			String strMessageParts[] = strText.split(",");
			int intMessageType = Integer.parseInt(strMessageParts[0]);
			
			if(intMessageType == CONNECT) {
				if(!blnConnected) {
					if(isServer) {
						//TELL CLIENT TO START GAME WITH SELECTED MAP
						sendMessage(Connections.CONNECT, GameCreation.selectedMap);
					}else {
						GameCreation.selectedMap = strMessageParts[1];
					}
					blnConnected = true;
					//GO TO GAME
					game = (Game) towerDefence.changeState(TowerDefence.GAME, GameCreation.selectedMap);
				}
			}else if(intMessageType == DISCONNECT) {
				//DISCONNECT AND GO TO GAME OVER SCREEN
				towerDefence.changeState(TowerDefence.GAME_OVER, game.waveNumber);
				game = null;
			}else if(intMessageType == CHAT_MESSAGE) {
				//SEND CHAT MESSAGE
				if(Connections.isServer) {
					Game.strMessageReceived = "Client: " + strMessageParts[1];
				}else {
					Game.strMessageReceived = "Server: " + strMessageParts[1];
				}
			}else if(intMessageType == Connections.PLACE_TOWER) {
				//PLACE NEW TOWER
				int placeTower = Integer.parseInt(strMessageParts[1]);
				int towerX = Integer.parseInt(strMessageParts[2]);
				int towerY = Integer.parseInt(strMessageParts[3]);
				if(isServer) {
					game.placeTower(placeTower, towerX, towerY, false);
				}else {
					game.towers.add(Tower.newTower(placeTower, towerX, towerY, strMessageParts[4]));
				}
			}else if(intMessageType == Connections.STAT_UPDATE) {
				//UPDATE HEALTH AND MONEY
				if(!isServer) {
					game.intBalance = Integer.parseInt(strMessageParts[1]);
					game.intHealth = Integer.parseInt(strMessageParts[2]);
					game.checkIfGameOver();
				}
			}else if(intMessageType == Connections.SPAWN_ENEMY) {
				//SPAWN ENEMY
				if(!isServer) {
					game.enemies.add(Enemy.newEnemy(Integer.parseInt(strMessageParts[1]), strMessageParts[2]));
				}
			}else if(intMessageType == Connections.UPDATE_TIMER) {
				//UPDATE WAVE NUMBER AND ROUND TIMER
				if(!isServer) {
					game.roundTime = Integer.parseInt(strMessageParts[1]);
					game.waveNumber = Integer.parseInt(strMessageParts[2]);
					game.enemies.clear();
				}
			}else if(intMessageType == Connections.UPDATE_ENEMY) {
				//SYNCHRONIZE ENEMY CHECKPOINT AND LOCATION WHEN IT REACHED TARGET CHECKPOINT
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
				//REMOVE ENEMY WHEN KILLED
				if(!isServer) {
					for(Enemy enemy : game.enemies) {
						if(enemy.id.equals(strMessageParts[1])) {
							Game.removeEnemies.add(enemy);
							return;
						}
					}
				}
			}else if(intMessageType == Connections.UPDATE_TOWER) {
				//UPDATE TOWER STATS WHEN UPGRADED
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
				//REMOVE TOWER WHEN SOLD
				if(isServer) {
					Connections.sendMessage(Connections.REMOVE_TOWER, strMessageParts[1]);
				}
				
				for(Tower tower : game.towers) {
					if(tower.id.equals(strMessageParts[1])) {
						Game.removeTowers.add(tower);
						if(game.selectedTower != null && game.selectedTower.id.equals(strMessageParts[1])) {
							game.selectedTower = null;
						}
						return;
					}
				}
			}
		}
	}
	
	//SEND A MESSAGE TO THE OTHER SIDE WITH THE SPECIFIED MESSAGE TYPE
	//AND AN ARRAY OF DESIRED OBJECTS (USUALLY STRINGS OR INTS) ATTACHED
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
	
	//DISCONNECT FROM THE OTHER SIDE
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
	//SERVER CONSTRUCTOR
	public Connections(TowerDefence towerDefence) {
		this.towerDefence = towerDefence;		
		Connections.isServer = true;
		ssm = new SuperSocketMaster(Settings.port, this);
		ssm.connect();
	}
	
	//CLIENT CONSTRUCTOR
	public Connections(String ipAddress, TowerDefence towerDefence) {
		this.towerDefence = towerDefence;
		Connections.isServer = false;
		ssm = new SuperSocketMaster(ipAddress, Settings.port, this);
		ssm.connect();
	}
	
}
