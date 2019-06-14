package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.Timer;

import enemies.BasicEnemy;
import enemies.Enemy;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import main.GameMap;
import main.InputListener;
import main.TowerDefence;
import main.Utils;
import networking.Connections;
import projectiles.Projectile;
import towers.BasicTower;
import towers.Tower;

public class Game extends State {
	
	public static final int TILE_SIZE = 40;

	public static ArrayList<Enemy> removeEnemies = new ArrayList<>();
	public static ArrayList<Tower> removeTowers = new ArrayList<>();

	public GameMap map;
	public int intHealth = 100;
	public int waveNumber = 1;
	public int intBalance = 1000000;
	private int intPlacingTower = -1;
	private Font font;
	private int[] enemyWave;
	public int roundTime = 5;
	private Timer roundTimer, waveTimer;
	private int enemiesInWave = 0;
	private boolean clicked = false;
	
	JTextField chatField = new JTextField();
	ArrayList<String> strOldMessage = new ArrayList<>();
	
	//path tile image variables
	//UD = up down
	//LR = left right

	private BufferedImage imgGrassTile, imgPathTileUD, imgPathTileLR, imgPathTileUR, imgPathTileUL, imgPathTileDL, imgPathTileDR,
			speed, attack, range, plus, cross;
	
	public ArrayList<Tower> towers;
	public ArrayList<Enemy> enemies;
	public ArrayList<Projectile> projectiles;
	
	public static String strMessageReceived;
	int intCount;
	
	public Tower selectedTower;
	
	//methods
	int intNumMessages = 0;
	
	//methods
	@Override
	public void update() {
		//UPDATE TOWERS
		for(int i = 0; i < towers.size(); i++) {
			towers.get(i).update(this);
		}
		
		//UPDATE ENEMIES
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update(this);
		}
		
		//UDPATE PROJECTILES
		for(int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update(this);
		}
		
		removeDeadEnemies();
		removeSoldTowers();
		
		if(InputListener.mouseButtons[MouseEvent.BUTTON1]) {
			clicked = true;
		}
		
		//CHECK IF TOWER IS PRESSED FROM TOWER BAR
		if(clicked && !InputListener.mouseButtons[MouseEvent.BUTTON1]) {
			clicked = false;
			if(selectedTower == null) {
				if(InputListener.mouseX >= Game.TILE_SIZE * 28 && InputListener.mouseX <= Game.TILE_SIZE * 29) {
					if(InputListener.mouseY >= Game.TILE_SIZE * 6 && InputListener.mouseY <= Game.TILE_SIZE * 7) {
						//PRESSED BASIC TOWER
						this.intPlacingTower = Tower.BASIC;
					}else if(InputListener.mouseY >= Game.TILE_SIZE * 8 && InputListener.mouseY <= Game.TILE_SIZE * 9) {
						//PRESSED FIRE TOWER
						this.intPlacingTower = Tower.FIRE;
					}else if(InputListener.mouseY >= Game.TILE_SIZE * 10 && InputListener.mouseY <= Game.TILE_SIZE * 11) {
						//PRESSED ICE TOWER
						this.intPlacingTower = Tower.ICE;
					}else if(InputListener.mouseY >= Game.TILE_SIZE * 12 && InputListener.mouseY <= Game.TILE_SIZE * 13) {
						//PRESSED SNIPE TOWER
						this.intPlacingTower = Tower.SNIPE;
					}else if(InputListener.mouseY >= Game.TILE_SIZE * 14 && InputListener.mouseY <= Game.TILE_SIZE * 15) {
						//PRESSED BOMB TOWER
						this.intPlacingTower = Tower.BOMB;
					}
				}else if(InputListener.mouseX < Game.TILE_SIZE * 27 && intPlacingTower != -1) {
					int towerX = (int) Math.floor(InputListener.mouseX / Game.TILE_SIZE) * Game.TILE_SIZE;
					int towerY = (int) Math.floor(InputListener.mouseY / Game.TILE_SIZE) * Game.TILE_SIZE;
					this.placeTower(intPlacingTower, towerX, towerY, Connections.isServer);
				}else if(InputListener.mouseX < Game.TILE_SIZE * 27 && intPlacingTower == -1){
					//CHECK IF TOWER IS PRESSED FROM MAP
					int towerX = (int) Math.floor(InputListener.mouseX / Game.TILE_SIZE) * Game.TILE_SIZE;
					int towerY = (int) Math.floor(InputListener.mouseY / Game.TILE_SIZE) * Game.TILE_SIZE;
					
					for(Tower tower : towers) {
						if(tower.intxLocation == towerX && tower.intyLocation == towerY) {						
							this.selectedTower = tower;
							break;
						}
					}
				}
			}else {
				//Check if tower is being upgraded
				if(InputListener.mouseX >= Game.TILE_SIZE * 30.5 && InputListener.mouseX <= Game.TILE_SIZE * 31.5){
					//Check which upgrade is being applied
					if(InputListener.mouseY >= Game.TILE_SIZE * 9 && InputListener.mouseY <= Game.TILE_SIZE * 10){
						//Check if there is room for improvement and enough money
						if(intBalance >= selectedTower.getUpgradePrice(Tower.UPGRADE_SPEED)){
							//Speed Upgrade for selected tower
							selectedTower.upgrade(Tower.UPGRADE_SPEED);
						}
					}else if(InputListener.mouseY >= Game.TILE_SIZE * 11 && InputListener.mouseY <= Game.TILE_SIZE * 12){
						//Check if there is room for improvement and enough money
						if(intBalance >= selectedTower.getUpgradePrice(Tower.UPGRADE_DAMAGE)){
							//Damage Upgrade for selected tower
							selectedTower.upgrade(Tower.UPGRADE_DAMAGE);
						}
					}else if(InputListener.mouseY >= Game.TILE_SIZE * 13 && InputListener.mouseY <= Game.TILE_SIZE * 15){
						//Check if there is room for improvement and enough money
						if(intBalance >= selectedTower.getUpgradePrice(Tower.UPGRADE_RANGE)){
							//Range Upgrade for selected tower
							selectedTower.upgrade(Tower.UPGRADE_RANGE);
						}
					}
				}
				
				//Check if tower is being sold
				if(InputListener.mouseX >= Game.TILE_SIZE * 28 && InputListener.mouseX <= Game.TILE_SIZE * 31
						&& InputListener.mouseY >= Game.TILE_SIZE * 17 && InputListener.mouseY <= Game.TILE_SIZE * 17.5){
					//Initiate Sell tower method
					if(Connections.isServer) {
						removeTowers.add(selectedTower);
					}
					Connections.sendMessage(Connections.REMOVE_TOWER, selectedTower.id);
					selectedTower = null;
				}
			}			
		}
		
		//CHECK FOR PRESSED KEYS
		if(InputListener.keys[KeyEvent.VK_ESCAPE]) {
			intPlacingTower = -1;
			selectedTower = null;
		}else if(InputListener.keys[KeyEvent.VK_1]) {
			intPlacingTower = Tower.BASIC;
		}else if(InputListener.keys[KeyEvent.VK_2]) {
			intPlacingTower = Tower.FIRE;
		}else if(InputListener.keys[KeyEvent.VK_3]) {
			intPlacingTower = Tower.ICE;
		}else if(InputListener.keys[KeyEvent.VK_4]) {
			intPlacingTower = Tower.SNIPE;
		}else if(InputListener.keys[KeyEvent.VK_5]) {
			intPlacingTower = Tower.BOMB;
		}
		
		if(Game.strMessageReceived != null) {
			strOldMessage.add(Game.strMessageReceived);
			Game.strMessageReceived = null;			
		}
		
		//CREATE ENEMY WAVES AND HANDLE DOWNTIME
		if(Connections.isServer) {
			if(roundTime > 0) {
				if(roundTimer == null) {
					Connections.sendMessage(Connections.UPDATE_TIMER, roundTime, waveNumber);
					enemyWave = null;
					roundTimer = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							roundTime--;
							if(roundTime <= 0) {
								roundTimer.stop();
								roundTimer = null;
							}
							Connections.sendMessage(Connections.UPDATE_TIMER, roundTime, waveNumber);
						}
					});
					roundTimer.start();
				}
			}else{
				if(enemyWave == null) {						
					enemyWave = new int[] {
							waveNumber,
							(int) Math.floor(waveNumber / 2),
							(int) Math.floor(waveNumber / 3),
							(int) Math.floor(waveNumber / 4),
							(int) Math.floor(waveNumber / 5)
					};
					
					spawnEnemies();
				}
				
				if(waveTimer == null) {
					waveTimer = new Timer(1500, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							spawnEnemies();
						}
					});
					waveTimer.start();
				}	
				
				if(enemies.size() == 0 && enemiesInWave == 0) {
					waveNumber++;
					roundTime = 3;
					waveTimer.stop();
					waveTimer = null;
					enemyWave = null;
				}
			}
		}
	}
	
	private synchronized void removeDeadEnemies() {
		//REMOVE DEAD ENEMIES
		for(Enemy enemy : removeEnemies) {
			if(enemies.contains(enemy)) {
				enemies.remove(enemy);
				if(Connections.isServer) {
					updateBalance(enemy.getReward());
				}
			}
		}
		removeEnemies.clear();
	}
	
	private synchronized void removeSoldTowers() {
		//REMOVE SOLD TOWERS
		for(Tower tower : removeTowers) {
			if(towers.contains(tower)) {
				towers.remove(tower);
				if(Connections.isServer) {
					updateBalance(tower.getSellPrice());
				}
			}
		}
		removeEnemies.clear();
	}
	
	private void spawnEnemies() {
		if(enemyWave != null) {
			enemiesInWave = 0;
			for(int enemyNum : enemyWave) {
				enemiesInWave += enemyNum;
			}
			
			boolean enemySpawned = false;
			while(!enemySpawned && enemiesInWave > 0) {
				int random = (int) Math.round(Math.random() * 4);
				if(enemyWave[random] > 0) {
					String id = Utils.genId();
					Connections.sendMessage(Connections.SPAWN_ENEMY, random, id);
					
					enemies.add(Enemy.newEnemy(random, id));
					enemyWave[random]--;
					enemySpawned = true;
				}
			}
		}		
	}

	@Override
	public void render(Graphics g) {
		//RENDER GRASS TILES
		for(int y = 0; y < towerDefence.getHeight(); y += Game.TILE_SIZE) {
			for(int x = 0; x < towerDefence.getWidth() - Game.TILE_SIZE * 5; x += Game.TILE_SIZE) {
				g.drawImage(imgGrassTile, x, y, null);
				if(intPlacingTower != -1) {
					g.drawRect(x, y, Game.TILE_SIZE, Game.TILE_SIZE);
				}
			}
		}
		
		//RENDER PATH
		//draws the rest of the path tiles
		int previousCheckpointX = map.getCheckpointX(0);
		int previousCheckpointY = map.getCheckpointY(0);
		for(int n = 1; n < map.getNumberOfCheckpoints(); n++) {
			int checkpointX = map.getCheckpointX(n);
			int checkpointY = map.getCheckpointY(n);
			if(checkpointX > previousCheckpointX) {
				for(int x = previousCheckpointX; x <= checkpointX; x += Game.TILE_SIZE) {
					g.drawImage(imgPathTileLR, x, checkpointY, null);
				}
			}else if(checkpointX < previousCheckpointX) {
				for(int x = previousCheckpointX; x >= checkpointX; x -= Game.TILE_SIZE) {
					g.drawImage(imgPathTileLR, x, checkpointY, null);
				}
			}else if(checkpointY > previousCheckpointY) {
				for(int y = previousCheckpointY; y <= checkpointY; y += Game.TILE_SIZE) {
					g.drawImage(imgPathTileUD, checkpointX, y, null);
				}
			}else if(checkpointY < previousCheckpointY) {
				for(int y = previousCheckpointY; y >= checkpointY; y -= Game.TILE_SIZE) {
					g.drawImage(imgPathTileUD, checkpointX, y, null);
				}
			}
			
			previousCheckpointX = checkpointX;
			previousCheckpointY = checkpointY;
		}
		
		previousCheckpointX = map.getCheckpointX(0);
		previousCheckpointY = map.getCheckpointY(0);
		for(int n = 1; n < map.getNumberOfCheckpoints(); n++) {
			int checkpointX = map.getCheckpointX(n);
			int checkpointY = map.getCheckpointY(n);
			
			if(n + 1 < map.getNumberOfCheckpoints()) {
				int nextCheckpointX = map.getCheckpointX(n + 1);
				int nextCheckpointY = map.getCheckpointY(n + 1);
				
				if(previousCheckpointX > checkpointX) {
					if(nextCheckpointY > checkpointY){
						g.drawImage(imgPathTileUR, checkpointX, checkpointY, null);
					}
					else{
						g.drawImage(imgPathTileDR, checkpointX, checkpointY, null);
					}
				}
				else if(previousCheckpointX < checkpointX) {
					if(nextCheckpointY > checkpointY) {
						g.drawImage(imgPathTileUL, checkpointX, checkpointY, null);
					}
					else {
						g.drawImage(imgPathTileDL, checkpointX, checkpointY, null);
					}
				}else if(previousCheckpointY > checkpointY) {
					if(nextCheckpointX > previousCheckpointX) {
						g.drawImage(imgPathTileUR, checkpointX, checkpointY, null);
					}else {
						g.drawImage(imgPathTileUL, checkpointX, checkpointY, null);
					}
				}else if(previousCheckpointY < checkpointY) {
					if(nextCheckpointX > previousCheckpointX) {
						g.drawImage(imgPathTileDR, checkpointX, checkpointY, null);
					}else {
						g.drawImage(imgPathTileDL, checkpointX, checkpointY, null);
					}
				}
			}
			
			previousCheckpointX = checkpointX;
			previousCheckpointY = checkpointY;
		}	
		
		//RENDER TOWERS
		for(int i = 0; i < towers.size(); i++) {
			towers.get(i).render(g);
		}
		
		//RENDER ENEMIES
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).render(g);
		}
		
		//RENDER PROJECTILES
		for(int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).render(g);
		}
		
		//RENDER TOWER WE WANT TO PLACE
		if (intPlacingTower != -1) {
			int towerX = (int) Math.floor(InputListener.mouseX / Game.TILE_SIZE) * Game.TILE_SIZE;
			int towerY = (int) Math.floor(InputListener.mouseY / Game.TILE_SIZE) * Game.TILE_SIZE;
			int towerRadius = Integer.parseInt(Tower.towerFiles[intPlacingTower].get("range"));

			g.setColor(new Color(0.8f, 0f, 1f, 0.4f));
			g.fillOval(towerX - towerRadius, towerY - towerRadius,
					(towerRadius * 2) + Game.TILE_SIZE, (towerRadius * 2) + Game.TILE_SIZE);
			g.drawImage(Tower.towerImages[intPlacingTower], towerX, towerY, null);
			
			g.setColor(new Color(1f, 0f, 0f, 0.6f));
			if(Connections.isServer) {
				g.fillRect(Game.TILE_SIZE * 14, 0, TowerDefence.WIDTH - (Game.TILE_SIZE * (14 + 5)), TowerDefence.HEIGHT);
			}else {
				g.fillRect(0, 0, Game.TILE_SIZE * 14, TowerDefence.HEIGHT);
			}
		}
		
		//RENDER OUTLINE OF EACH PLAYER'S SIDE OF THE MAP
		/*g.setColor(Color.RED);
		g.drawRect(Game.TILE_SIZE * 14, 0, TowerDefence.WIDTH - (Game.TILE_SIZE * (14 + 5)), TowerDefence.HEIGHT);
		g.setColor(Color.BLUE);
		g.drawRect(0, 0, Game.TILE_SIZE * 14, TowerDefence.HEIGHT);*/
		
		/////UI/////
		//RENDER CHAT
		
		g.setColor(Color.WHITE);
		g.setFont(font);
		for(intCount = 0; intCount < strOldMessage.size(); intCount++) {
			if(intCount > 5) {
				break;
			}
			g.drawString(strOldMessage.get(strOldMessage.size() - 1 - intCount), 0,
					(18  * Game.TILE_SIZE) - ((intCount + 2) * Game.TILE_SIZE / 2));
		}
		
		if(selectedTower != null) {
			g.setColor(new Color(0.8f, 0f, 1f, 0.4f));
			g.fillOval(selectedTower.intxLocation - selectedTower.intRange,
					selectedTower.intyLocation - selectedTower.intRange,
					(selectedTower.intRange * 2) + Game.TILE_SIZE, (selectedTower.intRange * 2) + Game.TILE_SIZE);
			g.drawImage(Tower.towerImages[selectedTower.type], selectedTower.intxLocation,
					selectedTower.intyLocation, null);
		}
		
		g.setFont(TowerDefence.font);
		//RENDER TOOL BAR
		g.setColor(Color.ORANGE);
		g.fillRect(Game.TILE_SIZE * 27, 0, Game.TILE_SIZE * 5, towerDefence.getHeight());
		//RENDER ROUND COUNTER
		g.setColor(Color.BLACK);
		g.drawString("Round: "+ waveNumber, 28 * Game.TILE_SIZE, 1 * Game.TILE_SIZE);
		//RENDER BALANCE
		BufferedImage cashSign = Utils.loadImage("sidebar/" + "CashSign.png");
		g.drawImage(cashSign, 28 * Game.TILE_SIZE, 2 * Game.TILE_SIZE,null);
		g.drawString("" +intBalance, 29* Game.TILE_SIZE, 3 * Game.TILE_SIZE);
		//RENDER HEALTH
		BufferedImage heart = Utils.loadImage("sidebar/" + "heart.png");
		g.drawImage(heart, 28 * Game.TILE_SIZE, 4 * Game.TILE_SIZE, null);
		g.drawString("" + intHealth, Game.TILE_SIZE * 29, 5*Game.TILE_SIZE);
		
		//IF TOWER IS NOT SELECTED
		if(selectedTower == null){
			//RENDER PURCHASABLE TOWERS
			g.drawImage(Tower.towerImages[Tower.BASIC], 28* Game.TILE_SIZE, 6 * Game.TILE_SIZE, null);
			g.drawImage(Tower.towerImages[Tower.FIRE], 28* Game.TILE_SIZE, 8 * Game.TILE_SIZE, null);
			g.drawImage(Tower.towerImages[Tower.ICE], 28* Game.TILE_SIZE, 10 * Game.TILE_SIZE, null);
			g.drawImage(Tower.towerImages[Tower.SNIPE], 28* Game.TILE_SIZE, 12 * Game.TILE_SIZE, null);
			g.drawImage(Tower.towerImages[Tower.BOMB], 28* Game.TILE_SIZE,14 * Game.TILE_SIZE, null);
			//RENDER TOWER PRICES
			g.drawString("$"+ Tower.towerFiles[Tower.BASIC].get("price"), 29 * Game.TILE_SIZE, 7 * Game.TILE_SIZE);
			g.drawString("$"+ Tower.towerFiles[Tower.FIRE].get("price"), 29 * Game.TILE_SIZE, 9 * Game.TILE_SIZE);
			g.drawString("$"+ Tower.towerFiles[Tower.ICE].get("price"), 29 * Game.TILE_SIZE, 11 * Game.TILE_SIZE);
			g.drawString("$"+ Tower.towerFiles[Tower.SNIPE].get("price"), 29 * Game.TILE_SIZE, 13 * Game.TILE_SIZE);
			g.drawString("$"+ Tower.towerFiles[Tower.BOMB].get("price"), 29 * Game.TILE_SIZE, 15 * Game.TILE_SIZE);
			//RENDER TOWER NAMES
			g.setFont(font);
			g.drawString("Basic", 28 * Game.TILE_SIZE, 6 * Game.TILE_SIZE);
			g.drawString("Fire", 28 * Game.TILE_SIZE, 8 * Game.TILE_SIZE);
			g.drawString("Ice", 28 * Game.TILE_SIZE, 10 * Game.TILE_SIZE);
			g.drawString("Snipe", 28 * Game.TILE_SIZE, 12 * Game.TILE_SIZE);
			g.drawString("Bomb", 28 * Game.TILE_SIZE, 14 * Game.TILE_SIZE);
		}else{
			//Sidebar when tower is selected, see Tower Upgrade UI			
			g.drawImage(Tower.towerImages[selectedTower.type], 28 * Game.TILE_SIZE, 6 * Game.TILE_SIZE, null);
			g.setColor(Color.BLACK);
			g.setFont(font);
			g.drawString(selectedTower.strName, 29 * Game.TILE_SIZE, (int)7 * Game.TILE_SIZE);
			g.drawString(selectedTower.strName, 29 * Game.TILE_SIZE, (int)7.5 * Game.TILE_SIZE);
			
			//Speed Upgrade
			g.drawImage(speed, (int) (27.5 * Game.TILE_SIZE), 9 * Game.TILE_SIZE, null);
			if(selectedTower.speedUpgrades < 5){
				g.drawString("$" + selectedTower.getUpgradePrice(Tower.UPGRADE_SPEED),
						(int) (28.5 * Game.TILE_SIZE), (int) (9.5 * Game.TILE_SIZE));
			}
			
			//Attack Upgrade
			g.drawImage(attack, (int) (27.5 * Game.TILE_SIZE), (int)11 * Game.TILE_SIZE, null);
			if(selectedTower.speedUpgrades < 5){
				g.drawString("$" + selectedTower.getUpgradePrice(Tower.UPGRADE_DAMAGE),
						(int) (28.5 * Game.TILE_SIZE), (int) (11.5 * Game.TILE_SIZE));
			}
			
			//Range Upgrade
			g.drawImage(range, (int) (27.5 * Game.TILE_SIZE), (int)13 * Game.TILE_SIZE, null);
			if(selectedTower.rangeUpgrades < 5){
				g.drawString("$" + selectedTower.getUpgradePrice(Tower.UPGRADE_RANGE),
						(int) (28.5 * Game.TILE_SIZE), (int) (13.5 * Game.TILE_SIZE));
			}
			
			g.setColor(Color.WHITE);
			g.fillRoundRect((int) (27.5 * Game.TILE_SIZE), (int) (8.5 * Game.TILE_SIZE),
					4 * Game.TILE_SIZE, (int) (0.5 * Game.TILE_SIZE), (int) (0.25 * Game.TILE_SIZE),
					(int) (0.25 * Game.TILE_SIZE));
			g.fillRoundRect((int) (27.5 * Game.TILE_SIZE), (int) (10.5 * Game.TILE_SIZE),
					4 * Game.TILE_SIZE, (int) (0.5 * Game.TILE_SIZE), (int) (0.25 * Game.TILE_SIZE),
					(int) (0.25 * Game.TILE_SIZE));
			g.fillRoundRect((int) (27.5 * Game.TILE_SIZE), (int) (12.5 * Game.TILE_SIZE),
					4 * Game.TILE_SIZE, (int) (0.5 * Game.TILE_SIZE), (int) (0.25 * Game.TILE_SIZE),
					(int) (0.25 * Game.TILE_SIZE));
			
			g.setColor(Color.GREEN);
			g.fillRoundRect((int) (27.5 * Game.TILE_SIZE), (int) (8.5 * Game.TILE_SIZE),
					(int) (selectedTower.speedUpgrades * 0.8 * Game.TILE_SIZE), Game.TILE_SIZE / 2,
					Game.TILE_SIZE / 4, Game.TILE_SIZE / 4);
			g.fillRoundRect((int) (27.5 * Game.TILE_SIZE), (int) (10.5 * Game.TILE_SIZE),
					(int) (selectedTower.damageUpgrades * 0.8 * Game.TILE_SIZE), Game.TILE_SIZE / 2,
					Game.TILE_SIZE / 4, Game.TILE_SIZE / 4);
			g.fillRoundRect((int) (27.5 * Game.TILE_SIZE), (int) (12.5 * Game.TILE_SIZE),
					(int) (selectedTower.rangeUpgrades * 0.8 * Game.TILE_SIZE), Game.TILE_SIZE / 2,
					Game.TILE_SIZE / 4, Game.TILE_SIZE / 4);
			
			g.setColor(Color.RED);
			g.fillRoundRect((int)28 * Game.TILE_SIZE, 15 * Game.TILE_SIZE, Game.TILE_SIZE,
					Game.TILE_SIZE,Game.TILE_SIZE / 4, Game.TILE_SIZE / 4);
			g.drawString("$" + selectedTower.getSellPrice(), 29*Game.TILE_SIZE, 16 * Game.TILE_SIZE);
			
			//Upgrade Button
			if(selectedTower.speedUpgrades < 5){
				g.drawImage(plus, (int) (30.5 * Game.TILE_SIZE),(int)9.5 * Game.TILE_SIZE, null);
			}else{
				g.drawImage(cross, (int) (30.5 * Game.TILE_SIZE), (int)9.5 * Game.TILE_SIZE, null);
			}
			
			if(selectedTower.damageUpgrades < 5){
				g.drawImage(plus, (int) (30.5 * Game.TILE_SIZE),(int)11.5 * Game.TILE_SIZE, null);
			}else{
				g.drawImage(cross, (int) (30.5 * Game.TILE_SIZE), (int)11.5 * Game.TILE_SIZE, null);
			}
			
			if(selectedTower.rangeUpgrades < 5){
				g.drawImage(plus, (int) (30.5 * Game.TILE_SIZE), (int)13.5 * Game.TILE_SIZE, null);
			}else{
				g.drawImage(cross, (int) (30.5 * Game.TILE_SIZE), (int)13.5 * Game.TILE_SIZE, null);
			}
		}

		//DRAW ROUND TIMER
		if(this.roundTime > 0) {
			g.setColor(Color.BLACK);
			g.setFont(TowerDefence.font);
			g.drawString("Next Round: " + roundTime, (towerDefence.getWidth() / 2) - 3 * Game.TILE_SIZE,
					towerDefence.getHeight() - Game.TILE_SIZE);
		}
	}

	public void placeTower(int placeTower, int towerX, int towerY, boolean fromServer) {		
		if(fromServer) {
			if(towerX >= Game.TILE_SIZE * 14) {
				return;
			}
		}else {
			if(towerX < Game.TILE_SIZE * 14) {
				return;
			}
		}
		
		//CHECK IF TOWER IS GETTING PLACED IN PATH
		int previousCheckpointX = map.getCheckpointX(0);
		int previousCheckpointY = map.getCheckpointY(0);
		for(int n = 1; n < map.getNumberOfCheckpoints(); n++) {
			int checkpointX = map.getCheckpointX(n);
			int checkpointY = map.getCheckpointY(n);
			
			Line line = new Line(previousCheckpointX + (Game.TILE_SIZE / 2), previousCheckpointY + (Game.TILE_SIZE / 2),
					checkpointX + (Game.TILE_SIZE / 2), checkpointY + (Game.TILE_SIZE / 2));
			if(line.intersects(towerX, towerY, Game.TILE_SIZE, Game.TILE_SIZE)) {
				return;
			}
			
			previousCheckpointX = checkpointX;
			previousCheckpointY = checkpointY;
		}
		
		//CHECK IF TOWER IS GOING TO COLLIDE WITH OTHER TOWERS
		for(Tower tower : towers) {
			if(tower.intxLocation == towerX && tower.intyLocation == towerY) {
				return;
			}
		}
		
		String id = Utils.genId();
		if(Connections.isServer) {
			int towerPrice = Integer.parseInt(Tower.towerFiles[placeTower].get("price"));
			if(intBalance >= towerPrice) {				
				Tower tower = Tower.newTower(placeTower, towerX, towerY, id);
				towers.add(tower);
			
				updateBalance(-towerPrice);
			}
		}
			
		Connections.sendMessage(Connections.PLACE_TOWER, placeTower, towerX, towerY, id);
	}
	
	private void updateBalance(int money) {
		this.intBalance += money;
		Connections.sendMessage(Connections.STAT_UPDATE, this.intBalance, this.intHealth);
	}

	public void dealDamage(int intDamage) {
		if(Connections.isServer) {
			this.intHealth -= intDamage;
			checkIfGameOver();
		}
	}
	
	public void checkIfGameOver() {
		if(intHealth <= 0) {
			//GAME OVER
			towerDefence.changeState(TowerDefence.GAME_OVER, waveNumber);
		}
	}
	
	//constructor
	public Game(TowerDefence towerDefence, String mapName) {
		super(towerDefence);
		
		Tower.loadTowerFiles();
		
		this.imgGrassTile = Utils.loadImage("tiles/GrassTile.jpg");
		this.imgPathTileUD = Utils.loadImage("tiles/" + "PathTileUD.jpg");
		this.imgPathTileLR = Utils.loadImage("tiles/" + "PathTileLR.jpg");
		this.imgPathTileUR = Utils.loadImage("tiles/" + "PathTileUR.jpg");
		this.imgPathTileUL = Utils.loadImage("tiles/" + "PathTileUL.jpg");
		this.imgPathTileDR = Utils.loadImage("tiles/" + "PathTileDR.jpg");
		this.imgPathTileDL = Utils.loadImage("tiles/" + "PathTileDL.jpg");
		this.imgPathTileDL = Utils.loadImage("tiles/" + "PathTileDL.jpg");
		
		speed = Utils.loadImage("sidebar/" + "Speed.png");
		attack = Utils.loadImage("sidebar/" + "Damage.png");
		range = Utils.loadImage("sidebar/" + "Range.png");
		plus = Utils.loadImage("sidebar/" + "Addition.png");
		cross = Utils.loadImage("sidebar/" + "Cross.png");
		
		map = new GameMap(mapName);
		
		towers = new ArrayList<>();		
		enemies = new ArrayList<>();
		projectiles = new ArrayList<>();		
		
		chatField.setBounds(0,17 * Game.TILE_SIZE, 4 * Game.TILE_SIZE,Game.TILE_SIZE);
		chatField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strMessageSend = chatField.getText();
				if(!strMessageSend.isEmpty()) {
					Connections.sendMessage(Connections.CHAT_MESSAGE, strMessageSend);
					if(Connections.isServer) {
						strOldMessage.add("Server: " + strMessageSend);
					}else {
						strOldMessage.add("Client: " + strMessageSend);
					}
				}
				chatField.setText("");
				towerDefence.requestFocusInWindow();
			}
		});
		towerDefence.add(chatField);
		
		font = new Font("Arial", Font.PLAIN, 18);
	}
}
