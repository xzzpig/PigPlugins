package com.github.xzzpig.lord.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

public class PlayerInfo implements IPlayerInfo {

	private static List<PlayerInfo> instances;

	private PlayerInfo(String name) {
		this.name = name;
	}

	public static PlayerInfo getBy(String name) {
		if (instances == null) {
			instances = new ArrayList<>();
		}
		for (PlayerInfo playerInfo : instances) {
			if (playerInfo.name.equalsIgnoreCase(name))
				return playerInfo;
		}
		PlayerInfo info = new PlayerInfo(name);
		return info;
	}

	private String name;

	public String getName() {
		return name;
	}

	@Override
	public double getArmor_M() {
		double r = 0;
		String command = "armor_m";
		Map<String, Object> data = new HashMap<>();
		data.put("command", command);
		data.put("info", this);
		data.put("result", r);
		PlayerInfoFormatEvent event = new PlayerInfoFormatEvent(data);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getData().containsKey("result")) {
			r = (double) event.getData().get("result");
		}
		return r;
	}

	@Override
	public double getArmor_P() {
		double r = 0;
		String command = "armor_p";
		Map<String, Object> data = new HashMap<>();
		data.put("command", command);
		data.put("info", this);
		data.put("result", r);
		PlayerInfoFormatEvent event = new PlayerInfoFormatEvent(data);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getData().containsKey("result")) {
			r = (double) event.getData().get("result");
		}
		return r;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Integer> getCritical() {
		Map<String, Integer> r = new HashMap<>();
		r.put("prob", 0);
		r.put("damage_p", 0);
		r.put("damage_m", 0);
		String command = "critical";
		Map<String, Object> data = new HashMap<>();
		data.put("command", command);
		data.put("info", this);
		data.put("result", r);
		PlayerInfoFormatEvent event = new PlayerInfoFormatEvent(data);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getData().containsKey("result")) {
			r = (Map<String, Integer>) event.getData().get("result");
		}
		return r;
	}

	@Override
	public double getDamage_M() {
		double r = 0;
		String command = "damage_m";
		Map<String, Object> data = new HashMap<>();
		data.put("command", command);
		data.put("info", this);
		data.put("result", r);
		PlayerInfoFormatEvent event = new PlayerInfoFormatEvent(data);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getData().containsKey("result")) {
			r = (double) event.getData().get("result");
		}
		return r;
	}

	@Override
	public double getDamage_P() {
		double r = 0;
		String command = "damage_p";
		Map<String, Object> data = new HashMap<>();
		data.put("command", command);
		data.put("info", this);
		data.put("result", r);
		PlayerInfoFormatEvent event = new PlayerInfoFormatEvent(data);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getData().containsKey("result")) {
			r = (double) event.getData().get("result");
		}
		return r;
	}

	@Override
	public double getEvasion() {
		double r = 0;
		String command = "evasion";
		Map<String, Object> data = new HashMap<>();
		data.put("command", command);
		data.put("info", this);
		data.put("result", r);
		PlayerInfoFormatEvent event = new PlayerInfoFormatEvent(data);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getData().containsKey("result")) {
			r = (double) event.getData().get("result");
		}
		return r;
	}

	@Override
	public double getHealth() {
		double r = 0;
		String command = "health";
		Map<String, Object> data = new HashMap<>();
		data.put("command", command);
		data.put("info", this);
		data.put("result", r);
		PlayerInfoFormatEvent event = new PlayerInfoFormatEvent(data);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getData().containsKey("result")) {
			r = (double) event.getData().get("result");
		}
		return r;
	}

	@Override
	public double getHunger() {
		double r = 0;
		String command = "hunger";
		Map<String, Object> data = new HashMap<>();
		data.put("command", command);
		data.put("info", this);
		data.put("result", r);
		PlayerInfoFormatEvent event = new PlayerInfoFormatEvent(data);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getData().containsKey("result")) {
			r = (double) event.getData().get("result");
		}
		return r;
	}

	@Override
	public int getLevel() {
		int r = 0;
		String command = "level";
		Map<String, Object> data = new HashMap<>();
		data.put("command", command);
		data.put("info", this);
		data.put("result", r);
		PlayerInfoFormatEvent event = new PlayerInfoFormatEvent(data);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getData().containsKey("result")) {
			r = (int) event.getData().get("result");
		}
		return r;
	}

	@Override
	public double getLifeSteal() {
		double r = 0;
		String command = "lifesteal";
		Map<String, Object> data = new HashMap<>();
		data.put("command", command);
		data.put("info", this);
		data.put("result", r);
		PlayerInfoFormatEvent event = new PlayerInfoFormatEvent(data);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getData().containsKey("result")) {
			r = (double) event.getData().get("result");
		}
		return r;
	}

	@Override
	public double getMaxHealth() {
		double r = 0;
		String command = "maxhealth";
		Map<String, Object> data = new HashMap<>();
		data.put("command", command);
		data.put("info", this);
		data.put("result", r);
		PlayerInfoFormatEvent event = new PlayerInfoFormatEvent(data);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getData().containsKey("result")) {
			r = (double) event.getData().get("result");
		}
		return r;
	}

	@Override
	public int getSpeed() {
		int r = 0;
		String command = "speed";
		Map<String, Object> data = new HashMap<>();
		data.put("command", command);
		data.put("info", this);
		data.put("result", r);
		PlayerInfoFormatEvent event = new PlayerInfoFormatEvent(data);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getData().containsKey("result")) {
			r = (int) event.getData().get("result");
		}
		return r;
	}

	@Override
	public String getZhiye() {
		String r = null;
		String command = "zhiye";
		Map<String, Object> data = new HashMap<>();
		data.put("command", command);
		data.put("info", this);
		data.put("result", r);
		PlayerInfoFormatEvent event = new PlayerInfoFormatEvent(data);
		Bukkit.getPluginManager().callEvent(event);
		if (event.getData().containsKey("result")) {
			r = (String) event.getData().get("result");
		}
		return r;
	}
}
