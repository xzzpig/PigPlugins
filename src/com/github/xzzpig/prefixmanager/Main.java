package com.github.xzzpig.prefixmanager;

import java.io.File;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xzzpig.pigapi.PigData;
import com.github.xzzpig.pigapi.bukkit.TCommandHelp;
import com.github.xzzpig.pigapi.bukkit.TConfig;
import com.github.xzzpig.pigapi.bukkit.TPrefix;
import com.github.xzzpig.pigapi.event.Event;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		this.getLogger().info(this.getName() + this.getDescription().getVersion() + "插件已被加载");
		this.saveDefaultConfig();
		this.loadConfig();
		this.getServer().getPluginManager()
		.registerEvents(PrefixListener.self, this);
		Event.registListener(PrefixListener.self);
		com.github.xzzpig.pigapi.plugin.Vars.chatformat = com.github.xzzpig.pigapi.plugin.Vars.chatformat
				.replaceAll("</world/>", "</worldprefix/>");
	}

	private void loadConfig() {
		Vars.dataFile = new File(this.getDataFolder(), "data.pigdata");
		try {
			Vars.dataFile.createNewFile();
			Vars.data = new PigData(Vars.dataFile);
		} catch (Exception e) {
			Vars.data = new PigData();
		}
		Vars.config = TConfig.getConfigFile(this.getName(), "config.yml");
		for (String key : Vars.config.getConfigurationSection(
				"prefixmanager.group").getKeys(false))
			Vars.data.set("group." + key,
					Vars.config.getString("prefixmanager.group." + key));
		for (String key : Vars.config.getConfigurationSection(
				"prefixmanager.world").getKeys(false))
			Vars.data.set("world." + key,
					Vars.config.getString("prefixmanager.world." + key));
		// Debuger.setIsDebug(Main.class, true);
		// Debuger.print("\n"+Vars.data.getPrintString());
		Vars.data.saveToFile(Vars.dataFile);
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		this.getLogger().info(this.getName() + "插件已被停用 ");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		String arg0 = "help";
		if (args.length > 0)
			arg0 = args[0];
		if (arg0.equalsIgnoreCase("help"))
			for (TCommandHelp ch : Help.PM.getSubCommandHelps())
				try {
					ch.getHelpMessage(this.getName()).send((Player) sender);
				} catch (Exception e) {
					sender.sendMessage(ch.toString());
				}
		else if (arg0.equalsIgnoreCase("group")) {
			String arg1 = "help";
			if (args.length > 1)
				arg1 = args[1];
			if (arg1.equalsIgnoreCase("help"))
				for (TCommandHelp ch : Help.PM.getSubCommandHelp("group")
						.getSubCommandHelps())
					try {
						ch.getHelpMessage(this.getName()).send((Player) sender);
					} catch (Exception e) {
						sender.sendMessage(ch.toString());
					}
			else if (arg1.equalsIgnoreCase("set")) {
				try {
					String group = args[2];
					String prefix = args[3];
					Vars.data.set("group." + group, prefix);
					sender.sendMessage("[PrefixManager]已将"+group+"的前缀设为"+prefix);				
				} catch (Exception e) {
					sender.sendMessage("[PrefixManager]参数[组]或[前缀]不可为空");
					return true;
				}
				Voids.buildAllPrefix();
				Voids.saveData();
			} else if (arg1.equalsIgnoreCase("move")){
				try {
					String group = args[2];
					String player = sender.getName();
					if (args.length > 3)
						player = args[3];
					else if (!(sender instanceof Player))
						sender.sendMessage("[PrefixManager]控制台 参数<玩家>不可为空");
					Vars.data.set("player." + player + ".group", group);
					sender.sendMessage("[PrefixManager]已将"+player+"的前缀组设为"+group);				
				} catch (Exception e) {
					sender.sendMessage("[PrefixManager]参数[组]不可为空");
					return true;
				}
				Voids.buildAllPrefix();
				Voids.saveData();
			} else if (arg1.equalsIgnoreCase("remove")) {
				String player = sender.getName();
				if (args.length > 2)
					player = args[2];
				else if (!(sender instanceof Player)) {
					sender.sendMessage("[PrefixManager]控制台 参数<玩家>不可为空");
					return true;
				}
				Vars.data.remove("player." + player + ".group");
				TPrefix.removePrefix(player, "PMGroup");
				sender.sendMessage("[PrefixManager]已将"+player+"的前缀组设为 无 ");				
				Voids.buildAllPrefix();
				Voids.saveData();
			}
		} else if (arg0.equalsIgnoreCase("world")) {
			String arg1 = "help";
			if (args.length > 1)
				arg1 = args[1];
			if (arg1.equalsIgnoreCase("help"))
				for (TCommandHelp ch : Help.PM.getSubCommandHelp("world")
						.getSubCommandHelps())
					try {
						ch.getHelpMessage(this.getName()).send((Player) sender);
					} catch (Exception e) {
						sender.sendMessage(ch.toString());
					}
			else if (arg1.equalsIgnoreCase("set")) {
				try {
					String prefix = args[2];
					String world;
					if (args.length > 3)
						world = args[3];
					else if (sender instanceof Player)
						world = ((Player) sender).getWorld().getName();
					else {
						sender.sendMessage("[PrefixManager]控制台 参数<世界>不可为空");
						return true;
					}
					Vars.data.set("world." + world, prefix);
					sender.sendMessage("[PrefixManager]已将"+world+"的前缀设为"+prefix);				
				} catch (Exception e) {
					sender.sendMessage("[PrefixManager]参数[组]或[前缀]不可为空");
					return true;
				}
				Voids.buildAllPrefix();
				Voids.saveData();
			}
		} else if (arg0.equalsIgnoreCase("player")) {
			String arg1 = "help";
			if (args.length > 1)
				arg1 = args[1];
			if (arg1.equalsIgnoreCase("help"))
				for (TCommandHelp ch : Help.PM.getSubCommandHelp("player")
						.getSubCommandHelps())
					try {
						ch.getHelpMessage(this.getName()).send((Player) sender);
					} catch (Exception e) {
						sender.sendMessage(ch.toString());
					}
			else if (arg1.equalsIgnoreCase("set")) {
				try {
					String prefix = args[2];
					String player = sender.getName();
					if (args.length > 3)
						player = args[3];
					else if (!(sender instanceof Player))
						sender.sendMessage("[PrefixManager]控制台 参数<玩家>不可为空");
					Vars.data.set("player." + player + ".prefix", prefix);
					sender.sendMessage("[PrefixManager]已将"+player+"的前缀设为"+prefix);				
				} catch (Exception e) {
					sender.sendMessage("[PrefixManager]参数[前缀]不可为空");
					return true;
				}
				Voids.buildAllPrefix();
				Voids.saveData();
			} else if (arg1.equalsIgnoreCase("remove")) {
				String player = sender.getName();
				if (args.length > 2)
					player = args[2];
				else if (!(sender instanceof Player))
					sender.sendMessage("[PrefixManager]控制台 参数<玩家>不可为空");
				Vars.data.remove("player." + player + ".prefix");
				sender.sendMessage("[PrefixManager]已将"+player +"的前缀设为 无");
				Voids.buildAllPrefix();
				Voids.saveData();
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
			String alias, String[] args) {
		return Help.PM.getTabComplete(this.getName(), sender, command, alias, args);
	}
}
