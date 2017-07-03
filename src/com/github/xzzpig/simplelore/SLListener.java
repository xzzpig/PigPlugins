package com.github.xzzpig.simplelore;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

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
						damage = Double.MAX_VALUE;
					}
				}
			}
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,})-([0-9]{1,}) Damage");
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
					}
				}
			}
			{
				Pattern p = Pattern.compile("Type:(.{1,})");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					String type = m.group(1);
					if (damager.hasPermission("simplelore.type." + type)) {
						event.setCancelled(true);
						damager.sendMessage("[SimpleLore]你没有权限使用这个物品");
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
				{
					Pattern p = Pattern.compile("\\+([0-9]{1,}) Armor");
					Matcher m = p.matcher(lore);
					while (m.find()) {
						int i = Integer.valueOf(m.group(1));
						damage -= i;
					}
				}
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
		}
	}
}