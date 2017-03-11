package com.github.xzzpig.pigattribute;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(PigAttributeListener.instance, this);
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		getLogger().info(getName() + "插件已被停用 ");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return Help.PigAttribute.runCommand(Help.PigAttribute.new CommandInstance(sender, command, label, args));
	}
}
