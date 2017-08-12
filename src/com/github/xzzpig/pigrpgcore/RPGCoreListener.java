package com.github.xzzpig.pigrpgcore;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.github.xzzpig.pigrpgcore.EDPlayerInfo.DamageType;

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
			EDPlayerInfo damagedInfo = new EDPlayerInfo((Player) event.getEntity());
			if (event.getDamager() instanceof Projectile) {
				damage -= damagedInfo.getRemoteDefence(event.getDamager());
				if (EDPlayerInfo.trigger(100, damagedInfo.getEvasionChance(DamageType.Remote)))
					damage = 0;
			} else {
				damage -= damagedInfo.getPhysicalDefence(event.getDamager());
				if (EDPlayerInfo.trigger(100, damagedInfo.getEvasionChance(DamageType.Physical)))
					damage = 0;
			}
		}
		if (damage < 0)
			damage = 0;
		event.setDamage(damage);
	}
}
