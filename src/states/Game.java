package states;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import enemies.Enemy;
import main.TowerDefence;
import main.Utils;
import projectiles.Projectile;
import towers.BasicTower;
import towers.Tower;

public class Game extends State{

	private TowerDefence towerDefence;
	
	private BufferedImage imgGrassTile;
	
	public ArrayList<Tower> towers;
	public ArrayList<Enemy> enemies;
	public ArrayList<Projectile> projectiles;
	
	public Game(TowerDefence towerDefence) {
		this.towerDefence = towerDefence;
		
		this.imgGrassTile = Utils.loadImage("images/GrassTile.jpg");
		
		towers = new ArrayList<>();
		towers.add(new BasicTower(40 * 10, 40 * 5));
		
		enemies = new ArrayList<>();
		projectiles = new ArrayList<>();
	}
	
	@Override
	public void update() {
		for(int i = 0; i < towers.size(); i++) {
			towers.get(i).update(this);
		}
		
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update(this);
		}
		
		for(int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update(this);
		}
	}

	@Override
	public void render(Graphics g) {
		//RENDER GRASS TILES
		for(int y = 0; y < towerDefence.getHeight(); y += imgGrassTile.getHeight()) {
			for(int x = 0; x < towerDefence.getWidth(); x += imgGrassTile.getWidth()) {
				g.drawImage(imgGrassTile, x, y, null);
				g.drawRect(x, y, imgGrassTile.getWidth(), imgGrassTile.getHeight());
			}
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
	}

}
