package com.github.xzzpig.pigrpglore;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.xzzpig.pigapi.bukkit.TString;
import com.github.xzzpig.pigrpgcore.EDPlayerInfo;

public class BukkitRPGLoreListener implements Listener {
	public static BukkitRPGLoreListener instance = new BukkitRPGLoreListener();

	@EventHandler
	public void onLifeSteal(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;
		Player p = (Player) event.getDamager();
		ItemStack item = p.getItemInHand();
		if (item != null && item.getType() != Material.AIR && new EDPlayerInfo(p).canUse(item)) {
			List<String> lores = item.getItemMeta().getLore();
			if (lores == null)
				lores = new ArrayList<>();
			for (String lore : lores) {
				lore = TString.removeColor(lore);
				{
					Pattern pattern = Pattern.compile("\\+([0-9]{1,}) LifeSteal");
					Matcher m = pattern.matcher(lore);
					while (m.find()) {
						int i = Integer.valueOf(m.group(1));
						Damageable damageable = p;
						double health = damageable.getHealth() + i;
						p.setHealth(health > damageable.getMaxHealth() ? damageable.getMaxHealth() : health);
					}
				}
			}
		}
	}

	@EventHandler
	public void onFireball(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			return;
		}
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if (item == null || item.getType() == Material.AIR)
			return;
		if (item != null && item.getType() != Material.AIR && new EDPlayerInfo(player).canUse(item)) {
			List<String> lores = item.getItemMeta().getLore();
			if (lores == null)
				lores = new ArrayList<>();
			for (String lore : lores) {
				lore = TString.removeColor(lore);
				{
					Pattern p = Pattern.compile("\\+([0-9]{1,}) Fireball");
					Matcher m = p.matcher(lore);
					while (m.find()) {
						int i = Integer.valueOf(m.group(1));
						if (i > 1)
							new Thread(new Runnable() {
								@Override
								public void run() {
									for (int j = 0; j < i; j++) {
										player.launchProjectile(Fireball.class);
										try {
											Thread.sleep(200);
										} catch (InterruptedException e) {
										}
									}
								}
							}).start();
						else {
							player.launchProjectile(Fireball.class);
						}
					}
				}
				{
					Pattern p = Pattern.compile("\\+([0-9]{1,})-([0-9]{1,}) ([0-9]{1,}) Heal");
					Matcher m = p.matcher(lore);
					while (m.find()) {
						int i = Integer.valueOf(m.group(1));
						int j = Integer.valueOf(m.group(2));
						int k = Integer.valueOf(m.group(2));
						player.getNearbyEntities(k, k, k).stream().filter(e -> e instanceof Player).map(e -> (Damageable) e)
								.forEach(p2 -> {
									double heal = ((i < j ? i : j) + Math.random() * Math.abs(i - j)) + p2.getHealth();
									p2.setHealth(heal < p2.getMaxHealth() ? heal : p2.getMaxHealth());
								});
					}
				}
			}
		}
	}
}
