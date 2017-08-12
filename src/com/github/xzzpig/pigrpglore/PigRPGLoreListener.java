package com.github.xzzpig.pigrpglore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.xzzpig.pigapi.bukkit.TString;
import com.github.xzzpig.pigrpgcore.EDPlayerInfo;
import com.github.xzzpig.pigrpgcore.EDPlayerInfo.DamageType;
import com.github.xzzpig.pigutils.eddata.EventDriveDataGetEvent;
import com.github.xzzpig.pigutils.event.Event;
import com.github.xzzpig.pigutils.event.EventHandler;
import com.github.xzzpig.pigutils.event.Listener;

public class PigRPGLoreListener implements Listener {
	public static PigRPGLoreListener instance = new PigRPGLoreListener();

	@EventHandler
	public void onGetDouble(EventDriveDataGetEvent<Double> event) {
		if (event.getValue() == null)
			event.setValue(0d);
		Player p = event.getExtras("player", Player.class);
		// Entity demaged = event.getExtras("entity", Entity.class);
		ItemStack item = p.getItemInHand();
		if (item != null && item.getType() != Material.AIR && new EDPlayerInfo(p).canUse(item)) {
			PigItemEffectEvent e = new PigItemEffectEvent(item, p, event.getKey() + "");
			e.putExtras("GetEvent", event);
			Event.callEvent(e);
		}
	}

	public void onGetInt(EventDriveDataGetEvent<Integer> event) {
		if (event.getValue() == null)
			event.setValue(0);
		Player p = event.getExtras("player", Player.class);
		ItemStack item = p.getItemInHand();
		if (item != null && item.getType() != Material.AIR && new EDPlayerInfo(p).canUse(item)) {
			PigItemEffectEvent e = new PigItemEffectEvent(item, p, event.getKey() + "");
			e.putExtras("GetEvent", event);
			Event.callEvent(e);
		}
	}

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onDamagex(PigItemEffectEvent event) {
		if (!event.getEffectTime().equals("Damage"))
			return;
		DamageType type = event.getExtras("GetEvent", EventDriveDataGetEvent.class).getExtras("type", DamageType.class);
		if (type == DamageType.Critical)
			return;
		double damage = (double) event.getExtras("GetEvent", EventDriveDataGetEvent.class).getValue();
		for (String lore : event.getLores()) {
			lore = TString.removeColor(lore);
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,})% Damagex");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					damage = damage * i / 100;
				}
			}
		}
		event.getExtras("GetEvent", EventDriveDataGetEvent.class).setValue(damage);
	}

	@EventHandler
	public void onDamages(PigItemEffectEvent event) {
		if (!event.getEffectTime().equals("Damage"))
			return;
		DamageType type = event.getExtras("GetEvent", EventDriveDataGetEvent.class).getExtras("type", DamageType.class);
		if (type == DamageType.Critical)
			return;
		for (String lore : event.getLores()) {
			lore = TString.removeColor(lore);
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,})% Damages");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					if (Math.random() * 100 < i) {
						event.getExtras("GetEvent", EventDriveDataGetEvent.class)
								.getExtras("entity", LivingEntity.class).setHealth(0d);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onDamaged(PigItemEffectEvent event) {
		if (!event.getEffectTime().equals("Damage"))
			return;
		DamageType type = event.getExtras("GetEvent", EventDriveDataGetEvent.class).getExtras("type", DamageType.class);
		if (type == DamageType.Critical)
			return;
		double damage = (double) event.getExtras("GetEvent", EventDriveDataGetEvent.class).getValue();
		for (String lore : event.getLores()) {
			lore = TString.removeColor(lore);
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,})-([0-9]{1,}) Damaged");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					int j = Integer.valueOf(m.group(2));
					damage += ((i < j ? i : j) + Math.random() * Math.abs(i - j));
				}
			}
		}
		event.getExtras("GetEvent", EventDriveDataGetEvent.class).setValue(damage);
	}

	@SuppressWarnings("unchecked")
	public void onDamagep(PigItemEffectEvent event) {
		if (!event.getEffectTime().equals("Damage"))
			return;
		DamageType type = event.getExtras("GetEvent", EventDriveDataGetEvent.class).getExtras("type", DamageType.class);
		if (type == DamageType.Critical)
			return;
		Entity demaged = event.getExtras("GetEvent", EventDriveDataGetEvent.class).getExtras("entity", Entity.class);
		if (!(demaged instanceof Player))
			return;
		double damage = (double) event.getExtras("GetEvent", EventDriveDataGetEvent.class).getValue();
		for (String lore : event.getLores()) {
			lore = TString.removeColor(lore);
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,})-([0-9]{1,}) Damagep");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					int j = Integer.valueOf(m.group(2));
					damage += ((i < j ? i : j) + Math.random() * Math.abs(i - j));
				}
			}
		}
		event.getExtras("GetEvent", EventDriveDataGetEvent.class).setValue(damage);
	}

	@SuppressWarnings("unchecked")
	public void onDamagee(PigItemEffectEvent event) {
		if (!event.getEffectTime().equals("Damage"))
			return;
		DamageType type = event.getExtras("GetEvent", EventDriveDataGetEvent.class).getExtras("type", DamageType.class);
		if (type == DamageType.Critical)
			return;
		Entity demaged = event.getExtras("GetEvent", EventDriveDataGetEvent.class).getExtras("entity", Entity.class);
		if (demaged instanceof Player)
			return;
		double damage = (double) event.getExtras("GetEvent", EventDriveDataGetEvent.class).getValue();
		for (String lore : event.getLores()) {
			lore = TString.removeColor(lore);
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
		event.getExtras("GetEvent", EventDriveDataGetEvent.class).setValue(damage);
	}

	@SuppressWarnings("unchecked")
	public void onEvasion(PigItemEffectEvent event) {
		if (!event.getEffectTime().equals("EvasionChance"))
			return;
		int evasion = (int) event.getExtras("GetEvent", EventDriveDataGetEvent.class).getValue();
		for (String lore : event.getLores()) {
			lore = TString.removeColor(lore);
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,})% Evasion");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					evasion += i;
				}
			}
		}
		event.getExtras("GetEvent", EventDriveDataGetEvent.class).setValue(evasion);
	}

	@SuppressWarnings("unchecked")
	public void onCriticalChance(PigItemEffectEvent event) {
		if (!event.getEffectTime().equals("CriticalChance"))
			return;
		int criticalChance = (int) event.getExtras("GetEvent", EventDriveDataGetEvent.class).getValue();
		for (String lore : event.getLores()) {
			lore = TString.removeColor(lore);
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,})% CriticalChance");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					criticalChance += i;
				}
			}
		}
		event.getExtras("GetEvent", EventDriveDataGetEvent.class).setValue(criticalChance);
	}

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onCriticalDamage(PigItemEffectEvent event) {
		if (!event.getEffectTime().equals("Damage"))
			return;
		DamageType type = event.getExtras("GetEvent", EventDriveDataGetEvent.class).getExtras("type", DamageType.class);
		if (type != DamageType.Critical)
			return;
		double damage = (double) event.getExtras("GetEvent", EventDriveDataGetEvent.class).getValue();
		for (String lore : event.getLores()) {
			lore = TString.removeColor(lore);
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,}) CriticalDamage");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					damage += i;
				}
			}
		}
		event.getExtras("GetEvent", EventDriveDataGetEvent.class).setValue(damage);
	}

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onArmor(PigItemEffectEvent event) {
		if (!event.getEffectTime().equals("Defence"))
			return;
		double defence = (double) event.getExtras("GetEvent", EventDriveDataGetEvent.class).getValue();
		for (String lore : event.getLores()) {
			lore = TString.removeColor(lore);
			{
				Pattern p = Pattern.compile("\\+([0-9]{1,}) Armor");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					defence += i;
				}
			}
		}
		event.getExtras("GetEvent", EventDriveDataGetEvent.class).setValue(defence);
	}

	@EventHandler
	public void onCanUse(EventDriveDataGetEvent<Boolean> event) {
		if (!event.getKey().equals("canUse"))
			return;
		ItemStack item = event.getExtras("item", ItemStack.class);
		if (item.getItemMeta().getLore() == null)
			return;
		Player player = event.getExtras("player", Player.class);
		// String usage = event.getExtras("usage", String.class);
		for (String lore : item.getItemMeta().getLore()) {
			lore = TString.removeColor(lore);
			{
				Pattern p = Pattern.compile("Lv ([0-9]{1,})");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					int i = Integer.valueOf(m.group(1));
					if (player.getLevel() < i) {
						event.setValue(false);
						event.setCanceled(true);
						return;
					}
				}
			}
			{
				Pattern p = Pattern.compile("Type:(.{1,})");
				Matcher m = p.matcher(lore);
				while (m.find()) {
					String type = m.group(1);
					if (!player.hasPermission("pigrpglore.type." + type)) {
						event.setCanceled(true);
						return;
					}
				}
			}
		}
		event.setValue(true);
	}
}
