package com.github.xzzpig.pigrpgcore;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

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
			EDPlayerInfo damagerInfo = new EDPlayerInfo((Player) event.getDamager());
			ItemStack item = ((Player) event.getDamager()).getItemInHand();
			if (item != null && item.getType() != Material.AIR) {
				if (!damagerInfo.canUse(item, "Damage_Physical")) {
					event.setCancelled(true);
					return;
				}
			}
			damage += damagerInfo.getPhysicalDamage(event.getEntity());
			if (EDPlayerInfo.trigger(100, damagerInfo.getCriticalChance()))
				damage += damagerInfo.getCriticalDamage(event.getDamager());
		}
		if (event.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) event.getDamager();
			if (projectile.getShooter() instanceof Player) {
				EDPlayerInfo damagerInfo = new EDPlayerInfo((Player) projectile.getShooter());
				ItemStack item = ((Player) projectile.getShooter()).getItemInHand();
				if (item != null && item.getType() != Material.AIR) {
					if (!damagerInfo.canUse(item, "Damage_Remote")) {
						event.setCancelled(true);
						return;
					}
				}
				damage += new EDPlayerInfo((Player) projectile.getShooter()).getRemoteDamage(event.getEntity());
				if (EDPlayerInfo.trigger(100, damagerInfo.getCriticalChance()))
					damage += damagerInfo.getCriticalDamage(event.getDamager());
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
		if (damage <= 0)
			event.setCancelled(true);
		else
			event.setDamage(damage);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void ProjectileLaunch(ProjectileLaunchEvent event) {
		Projectile projectile = event.getEntity();
		if (!(projectile.getShooter() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		ItemStack item = player.getItemInHand();
		if (item != null && item.getType() != Material.AIR) {
			projectile.setMetadata("handItem", new FixedMetadataValue(Main.instance, item));
		}
	}
}
