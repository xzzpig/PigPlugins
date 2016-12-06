package com.github.xzzpig.lord.equip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.xzzpig.lord.Vars;
import com.github.xzzpig.pigapi.bukkit.TStringMatcher;

public class Equipment {
	public static List<String> defaultlorelist;
	public static List<Equipment> equiplist;

	public static Equipment getBy(int id) {
		for (Equipment e : equiplist) {
			if (e.getID() == id)
				return e;
		}
		return null;
	}

	public static Equipment getBy(ItemStack item) {
		String name = item.getItemMeta().getDisplayName();
		String[] ss = name.split(ChatColor.COLOR_CHAR + "r");
		if (ss.length == 0)
			return null;
		int id = 0;
		try {
			id = Integer.parseInt(ss[0]);
		} catch (Exception e) {
			return null;
		}
		return getBy(id);
	}

	public static void loadConfig() {
		ConfigurationSection equipment = Vars.config.getConfigurationSection("Lord.Equipment");
		List<String> equiplist = equipment.getStringList("EquipList");
		equiplist = new ArrayList<>();
		for (String equip : equiplist) {
			Equipment.equiplist.add(new Equipment(equipment.getConfigurationSection(equip)));
		}
		defaultlorelist = equipment.getStringList("DefaultLore");
	}

	private ConfigurationSection config;

	public Equipment(ConfigurationSection equipconfig) {
		config = equipconfig;
	}

	public ConfigurationSection getConfig() {
		return config;
	}

	public int getID() {
		return config.getInt("id", 1);
	}

	public int getItemID() {
		return config.getInt("item", 276);
	}

	public List<String> getLore() {
		HashMap<String, Object> d = new HashMap<>();
		d.put("equip", this);
		List<String> lore = new ArrayList<>();
		for (String l : defaultlorelist) {
			lore.add(TStringMatcher.buildStr(l, d, false));
		}
		if (config.getKeys(false).contains("lore"))
			for (String l : config.getStringList("lore")) {
				lore.add(TStringMatcher.buildStr(l, d, false));
			}
		return lore;
	}

	public String getName() {
		return config.getString("name");
	}

	@SuppressWarnings("deprecation")
	public ItemStack toItem() {
		ItemStack item = new ItemStack(getItemID());
		ItemMeta data = item.getItemMeta();
		String head_1 = getID() + "";
		StringBuffer head = new StringBuffer();
		;
		for (char c : head_1.toCharArray()) {
			head.append(ChatColor.COLOR_CHAR).append(c);
		}
		head.append(ChatColor.COLOR_CHAR).append('r');
		HashMap<String, Object> d = new HashMap<>();
		d.put("equip", this);
		data.setDisplayName(head.toString() + TStringMatcher.buildStr(getName(), d, false));
		data.setLore(getLore());
		item.setItemMeta(data);
		return item;
	}
}