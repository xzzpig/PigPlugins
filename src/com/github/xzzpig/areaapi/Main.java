package com.github.xzzpig.areaapi;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static Main instance;

	public static Main getInstance() {
		return instance;
	}

	private void loadConfig() {
		FileConfiguration config = getConfig();
		AreaAPI.selectTool = config.getInt("selectTool", 280);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return Help.AreaAPI.runCommand(Help.AreaAPI.new CommandInstance(sender, command, label, args));
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		AreaAPI.getLoadedAreas().forEach(area -> {
			try {
				area.saveData();
				getLogger().info("Area(" + area.name + ")已保存");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		AreaAPI.getLoadedAreas().clear();
		AreaAPI.playerSelectedLocMap.clear();
		getLogger().info(getName() + "插件已被停用 ");
	}

	@Override
	public void onEnable() {
		instance = this;
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		saveDefaultConfig();
		loadConfig();
		getServer().getPluginManager().registerEvents(BukkitListener.instance, this);
	}
}
