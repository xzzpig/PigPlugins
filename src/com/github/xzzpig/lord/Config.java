package com.github.xzzpig.lord;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

	public static ConfigurationSection Lord, Lord_Notice;
	public static List<String> Lord_Notice_content;
	public static int Lord_Notice_time, Lord_Notice_command_times, Lord_Notice_command_delay;

	public static void read(FileConfiguration config) {
		Lord = config.getConfigurationSection("Lord");
		Lord_Notice = Lord.getConfigurationSection("Notice");
		Lord_Notice_time = Lord_Notice.getInt("time");
		Lord_Notice_command_times = Lord_Notice.getInt("command_times");
		Lord_Notice_command_delay = Lord_Notice.getInt("command_delay");
		Lord_Notice_content = Lord_Notice.getStringList("content");
	}
}
