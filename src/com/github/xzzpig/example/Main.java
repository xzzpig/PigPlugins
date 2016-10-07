package com.github.xzzpig.example;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xzzpig.pigapi.PigData;
import com.github.xzzpig.pigapi.bukkit.TCommandHelp;

public class Main extends JavaPlugin {
	public static PigData data;
	
	@Override
	public void onEnable() {
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		saveDefaultConfig();
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		getLogger().info(getName() + "插件已被停用 ");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String arg0 = "help";
		try {
			arg0 = args[0];
		} catch (Exception e) {}
		if (arg0.equalsIgnoreCase("help")) {
			if (arg0.equalsIgnoreCase("help")) {
				for(TCommandHelp sub:Help.Example.getSubCommandHelps()){
					sub.getHelpMessage("ExpProtect").send(sender);
				}
				return true;
			}
		}
		return false;
	}
}

