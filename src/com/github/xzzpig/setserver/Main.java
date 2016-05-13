package com.github.xzzpig.setserver;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xzzpig.pigapi.bukkit.TConfig;

public class Main extends JavaPlugin {
	public static HashMap<Integer, Integer> levelexp = new HashMap<Integer, Integer>();
	public static boolean debug;
	private boolean firstload = true;
	@Override
	public void onEnable() {
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		saveDefaultConfig();
		FileConfiguration config = TConfig.getConfigFile(getName(), "config.yml");
		debug = config.getBoolean("setserver.debug");
		Debuger.print("已开启调试模式");
		for(String arg:config.getStringList("setserver.level")){
			Debuger.print(arg);
			String[] args = arg.split(",");
			int min = Integer.valueOf(args[0]),max=Integer.valueOf(args[1]),exp=Integer.valueOf(args[2]);
			for (int i = min; i <= max; i++) {
				levelexp.put(i,exp);
				Debuger.print("Lv"+i+"="+exp+"Exp");
			}
		}
		if(firstload){
			getServer().getPluginManager().registerEvents(new SSListener(),this);
			firstload = false;
		}
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		levelexp.clear();
		getLogger().info(getName() + "插件已被停用 ");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		onDisable();onEnable();
		sender.sendMessage("[SetServer]已重载");
		return true;
	}
}

