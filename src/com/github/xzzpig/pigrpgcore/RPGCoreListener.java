package com.github.xzzpig.pigrpgcore;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class RPGCoreListener implements Listener {

	public static RPGCoreListener instance = new RPGCoreListener();

	private RPGCoreListener() {
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		double damage = event.getDamage();
		if (event.getDamager() instanceof Player) {
			damage += new EDPlayerInfo((Player) event.getDamager()).getPhysicalDamage(event.getEntity());
		}
		if (event.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) event.getDamager();
			if (projectile.getShooter() instanceof Player) {
				damage += new EDPlayerInfo((Player) projectile.getShooter()).getRemoteDamage(event.getEntity());
			}
		}
		if (event.getEntity() instanceof Player) {
			if (event.getDamager() instanceof Projectile) {
				damage -= new EDPlayerInfo((Player) event.getDamager()).getRemoteDefence(event.getDamager());
			} else {
				damage -= new EDPlayerInfo((Player) event.getDamager()).getPhysicalDefence(event.getDamager());
			}
		}
		if (damage < 0)
			damage = 0;
		event.setDamage(damage);
	}
}
