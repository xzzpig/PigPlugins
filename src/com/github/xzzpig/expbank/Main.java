package com.github.xzzpig.expbank;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;
import com.github.xzzpig.pigapi.bukkit.TConfig;
import com.github.xzzpig.pigapi.bukkit.TExp;

public class Main extends JavaPlugin {
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		String arg0 = Messages.getString("Main.0"); //$NON-NLS-1$
		if (args.length > 0)
			arg0 = args[0];
		if (arg0.equalsIgnoreCase(Messages.getString("Main.1"))) { //$NON-NLS-1$
			for (TCommandHelp com : Help.EB.getAllSubs()) {
				if (sender instanceof Player)
					com.getHelpMessage(Messages.getString("Main.2")).send((Player) sender); //$NON-NLS-1$
				else
					sender.sendMessage(com.getHelpMessage(Messages.getString("Main.3")).toString()); //$NON-NLS-1$
			}
			return true;
		} else if (arg0.equalsIgnoreCase(Messages.getString("Main.4"))) { //$NON-NLS-1$
			if (!(sender instanceof Player)) {
				sender.sendMessage(Messages.getString("Main.5")); //$NON-NLS-1$
				return true;
			}
			Player player = (Player) sender;
			int exp = TExp.getTotalExperience(player);
			FileConfiguration config = TConfig.getConfigFile(Messages.getString("Main.6"), //$NON-NLS-1$
					Messages.getString("Main.7")); //$NON-NLS-1$
			int savedexp = config.getInt(Messages.getString("Main.8") + player.getName(), 0); //$NON-NLS-1$
			config.set(Messages.getString("Main.9") + player.getName(), exp + savedexp); //$NON-NLS-1$
			TConfig.saveConfig(Messages.getString("Main.10"), config, Messages.getString("Main.11")); //$NON-NLS-1$ //$NON-NLS-2$
			player.setExp(0);
			player.setLevel(0);
			sender.sendMessage(Messages.getString("Main.12") + exp + Messages.getString("Main.13") //$NON-NLS-1$ //$NON-NLS-2$
					+ (exp + savedexp) + Messages.getString("Main.14")); //$NON-NLS-1$
			return true;
		} else if (arg0.equalsIgnoreCase(Messages.getString("Main.15"))) { //$NON-NLS-1$
			if (!(sender instanceof Player)) {
				sender.sendMessage(Messages.getString("Main.16")); //$NON-NLS-1$
				return true;
			}
			Player player = (Player) sender;
			FileConfiguration config = TConfig.getConfigFile(Messages.getString("Main.17"), //$NON-NLS-1$
					Messages.getString("Main.18")); //$NON-NLS-1$
			int savedexp = config.getInt(Messages.getString("Main.19") + player.getName(), 0); //$NON-NLS-1$
			TExp.setTotalExperience(player,
					savedexp + TExp.getTotalExperience(player));
			config.set(Messages.getString("Main.20") + player.getName(), 0); //$NON-NLS-1$
			TConfig.saveConfig(Messages.getString("Main.21"), config, Messages.getString("Main.22")); //$NON-NLS-1$ //$NON-NLS-2$
			sender.sendMessage(Messages.getString("Main.23") + savedexp); //$NON-NLS-1$
			return true;
		}
		return false;
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		getLogger().info(getName() + Messages.getString("Main.24")); //$NON-NLS-1$
	}

	@Override
	public void onEnable() {
		getLogger().info(getName() + getDescription().getVersion() + Messages.getString("Main.25")); //$NON-NLS-1$
		saveDefaultConfig();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
			String alias, String[] args) {
		return Help.EB.getTabComplete(Messages.getString("Main.26"), sender, command, alias, args); //$NON-NLS-1$
	}
}
