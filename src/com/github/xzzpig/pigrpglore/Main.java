package com.github.xzzpig.pigrpglore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xzzpig.pigutils.event.Event;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		saveDefaultConfig();
		Event.registListener(PigRPGLoreListener.instance);
		Bukkit.getPluginManager().registerEvents(BukkitRPGLoreListener.instance, this);
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		Event.unregListener(PigRPGLoreListener.instance);
		getLogger().info(getName() + "插件已被停用 ");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return Help.PigRPGLore.runCommand(Help.PigRPGLore.new CommandInstance(sender, command, label, args));
	}
}
