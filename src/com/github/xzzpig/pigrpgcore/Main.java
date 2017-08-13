package com.github.xzzpig.pigrpgcore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static Main instance;
	
	@Override
	public void onEnable() {
		instance = this;
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(RPGCoreListener.instance, this);
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		instance = null;
		getLogger().info(getName() + "插件已被停用 ");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return Help.PigRPGCore.runCommand(Help.PigRPGCore.new CommandInstance(sender, command, label, args));
	}
}
