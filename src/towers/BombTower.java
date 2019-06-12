package towers;

import java.awt.Color;
import java.awt.Graphics;
import enemies.Enemy;
import states.Game;

public class BombTower extends Tower{
	
	private long explosionStart = 0L, damageCounter = 0L;
	private int explosionX, explosionY, explosionRadius = Game.TILE_SIZE * 2;

	public BombTower(int intxLocation, int intyLocation, String id) {
		super(intxLocation, intyLocation, Tower.BOMB, id);
	}

	@Override
	public void effectOnHit(Enemy enemy) {
		explosionX = enemy.intxLocation + (Game.TILE_SIZE / 2);
		explosionY = enemy.intyLocation + (Game.TILE_SIZE / 2);
		explosionStart = System.currentTimeMillis();
	}
	
	@Override
	public void update(Game game) {
		super.update(game);
		if(System.currentTimeMillis() - explosionStart <= intAttackSpeed / 2) {
			if(System.currentTimeMillis() - damageCounter >= intAttackSpeed / 4) {
				damageCounter = System.currentTimeMillis();
				for(Enemy enemy : game.enemies) {
					int enemyX = enemy.intxLocation + (Game.TILE_SIZE / 2);
					int enemyY = enemy.intyLocation + (Game.TILE_SIZE / 2);
					if(enemyX >= explosionX - explosionRadius && enemyX <= explosionX + explosionRadius
							&& enemyY >= explosionY - explosionRadius && enemyY <= explosionY + explosionRadius) {
						enemy.dealDamage(this.intAttackDamage / 4);
					}
				}
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		if(System.currentTimeMillis() - explosionStart <= intAttackSpeed / 2) {
			g.setColor(new Color(1f, 246 / 255, 79 / 255, 0.7f));
			g.fillOval(explosionX - explosionRadius, explosionY - explosionRadius,
					explosionRadius * 2, explosionRadius * 2);
		}
	}
	
}
