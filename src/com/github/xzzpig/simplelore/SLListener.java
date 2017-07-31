package com.github.xzzpig.simplelore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.projectiles.ProjectileSource;

import com.github.xzzpig.pigapi.bukkit.TString;

public class SLListener implements Listener {
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		Player damager = (Player) event.getDamager();
		ItemStack item = damager.getItemInHand();
		if (item == null || item.getType() == Material.AIR)
			return;
		double damage = event.getDamage();
		List<String> lores = item.getItemMeta().getLore();
		if (lores == null)
			return;
		for (String lore : lores) {
			lore = TString.removeColor(lore);
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,})% Damagex");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					damage = damage * i / 100;
				}
			}
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,})% Damages");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					if (Math.random() * 100 < i) {
						damage = Double.MAX_VALUE / 3;
					}
				}
			}
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,})-([0-9]{1,}) Damaged");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					int j = Integer.valueOf(m.group(2));
					damage += ((i < j ? i : j) + Math.random() * Math.abs(i - j));
				}
			}
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,})% CriticalChance \\+([0-9]{1,}) CriticalDamage");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					int j = Integer.valueOf(m.group(2));
					if (Math.random() * 100 < i) {
						damage += j;
					}
				}
			}
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,}) LifeSteal");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					Damageable d = damager;
					double health = d.getHealth() + i;
					d.setHealth(health > d.getMaxHealth() ? d.getMaxHealth() : health);
				}
			}
			{
				Pattern p = Pattern.compile("Lv ([0-9]{1,})");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					if (damager.getLevel() < i) {
						event.setCancelled(true);
						damager.sendMessage("[SimpleLore]你的等级不足以使用这个物品");
						return;
					}
				}
			}
			{
				Pattern p = Pattern.compile("Type:(.{1,})");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					String type = m.group(1);
					if (!damager.hasPermission("simplelore.type." + type)) {
						event.setCancelled(true);
						damager.sendMessage("[SimpleLore]你没有权限使用这个物品");
						return;
					}
				}
			}
			if ((event.getEntity() instanceof Player)) {
				{
					Pattern p = Pattern.compile("\\+([0-9]{1,})-([0-9]{1,}) Damagep");
					Matcher m = p.matcher(lore);
					while (m.find()) {
						int i = Integer.valueOf(m.group(1));
						int j = Integer.valueOf(m.group(2));
						damage += ((i < j ? i : j) + Math.random() * Math.abs(i - j));
					}
				}
				Player player = (Player) event.getEntity();
				List<ItemStack> items = new ArrayList<>();
				if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
					items.add(player.getItemInHand());
				}
				items.addAll(Arrays.asList(player.getEquipment().getArmorContents()));
				equip: for (ItemStack itemStack : items) {
					if (itemStack == null || itemStack.getType() == Material.AIR)
						continue;
					List<String> lores2 = item.getItemMeta().getLore();
					if (lores2 == null)
						continue;
					for (String lore2 : lores2) {
						lore2 = TString.removeColor(lore2);
						{
							Pattern p = Pattern.compile("Lv ([0-9]{1,})");
							Matcher m = p.matcher(lore2);
							while (m.find()) {
								int i = Integer.valueOf(m.group(1));
								if (player.getLevel() < i) {
									continue equip;
								}
							}
						}
						{
							Pattern p = Pattern.compile("Type:(.{1,})");
							Matcher m = p.matcher(lore);
							while (m.find()) {
								String type = m.group(1);
								if (!player.hasPermission("simplelore.type." + type)) {
									continue equip;
								}
							}
						}
						{
							Pattern p = Pattern.compile("\\+([0-9]{1,}) Armor");
							Matcher m = p.matcher(lore2);
							while (m.find()) {
								int i = Integer.valueOf(m.group(1));
								damage -= i;
							}
						}
					}
				}
				// {
				// Pattern p = Pattern.compile("\\+([0-9]{1,}) Armor");
				// Matcher m = p.matcher(lore);
				// while (m.find()) {
				// int i = Integer.valueOf(m.group(1));
				// damage -= i;
				// }
				// }
			} else {
				{
					Pattern p = Pattern.compile("\\+([0-9]{1,})-([0-9]{1,}) Damagee");
					Matcher m = p.matcher(lore);
					while (m.find()) {
						int i = Integer.valueOf(m.group(1));
						int j = Integer.valueOf(m.group(2));
						damage += ((i < j ? i : j) + Math.random() * Math.abs(i - j));
					}
				}
			}
			event.setDamage(damage);
		}
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			return;
		}
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if (item == null || item.getType() == Material.AIR)
			return;
		List<String> lores = item.getItemMeta().getLore();
		if (lores == null)
			return;
		for (String lore : lores) {
			lore = TString.removeColor(lore);
			{
				Pattern p = Pattern.compile("Lv ([0-9]{1,})");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					if (player.getLevel() < i) {
						event.setCancelled(true);
						player.sendMessage("[SimpleLore]你的等级不足以使用这个物品");
						return;
					}
				}
			}
			{
				Pattern p = Pattern.compile("Type:(.{1,})");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					String type = m.group(1);
					if (!player.hasPermission("simplelore.type." + type)) {
						event.setCancelled(true);
						player.sendMessage("[SimpleLore]你没有权限使用这个物品");
						return;
					}
				}
			}
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,}) ([0-9]{1,}) Fireball");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					long space = Long.valueOf(m.group(2));
					List<MetadataValue> ms = player.getMetadata("fireball_last_sl");
					if (ms != null && ms.size() != 0) {
						long last = ms.get(0).asLong();
						if (last > System.currentTimeMillis()) {
							continue;
						}
					}
					double damage = ((LivingEntity) player).getLastDamage();
					MetadataValueAdapter damageValue = new MetadataValueAdapter(Main.self) {
						@Override
						public Object value() {
							return damage;
						}

						@Override
						public void invalidate() {

						}
					};
					if (i > 1)
						new Thread(new Runnable() {
							@Override
							public void run() {
								for (int j = 0; j < i; j++) {
									Fireball fireball = player.launchProjectile(Fireball.class);
									fireball.setMetadata("damage_sl", damageValue);
									try {
										Thread.sleep(200);
									} catch (InterruptedException e) {
									}
								}
							}
						}).start();
					else {
						Fireball fireball = player.launchProjectile(Fireball.class);
						fireball.setMetadata("damage_sl", damageValue);
					}
					player.setMetadata("fireball_last_sl", new MetadataValueAdapter(Main.self) {
						@Override
						public Object value() {
							return System.currentTimeMillis() + space;
						}

						@Override
						public void invalidate() {
							// TODO Auto-generated method stub

						}
					});
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

	@EventHandler
	public void onDamageByFB(EntityDamageByEntityEvent event) {
		Entity entity = event.getDamager();
		if (!(entity instanceof Fireball))
			return;
		Fireball fireball = (Fireball) entity;
		@SuppressWarnings("deprecation")
		ProjectileSource shooter = ((Projectile) fireball).getShooter();
		if (!(shooter instanceof Player))
			return;
		List<MetadataValue> ms = fireball.getMetadata("damage_sl");
		if (ms == null || ms.size() == 0)
			return;
		LivingEntity livingEntity = (LivingEntity) event.getEntity();
		livingEntity.damage(ms.get(0).asDouble());
	}
}
