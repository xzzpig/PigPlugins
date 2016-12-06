package com.github.xzzpig.lord;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xzzpig.lord.equip.EquipAPIListener;
import com.github.xzzpig.lord.equip.Equipment;
import com.github.xzzpig.lord.player.PlayerInfoListener;
import com.github.xzzpig.lord.zhiye.ZhiyeBukkitListener;
import com.github.xzzpig.pigapi.bukkit.TCommandHelp;
import com.github.xzzpig.pigapi.bukkit.TConfig;
import com.github.xzzpig.pigapi.bukkit.TMessage;
import com.github.xzzpig.pigapi.event.Event;

public class Main extends JavaPlugin {
	private static int notice_index, notice_size;

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String arg0 = "help";
		try {
			arg0 = args[0];
		} catch (Exception e) {
		}
		if (arg0.equalsIgnoreCase("help")) {
			if (arg0.equalsIgnoreCase("help")) {
				for (TCommandHelp sub : Help.Lord.getSubCommandHelps()) {
					sub.getHelpMessage("Lord").send(sender);
				}
				return true;
			}
		} else if (arg0.equalsIgnoreCase("notice")) {
			if (!sender.hasPermission("lord.admin.command.notice")) {
				sender.sendMessage("[Lord]" + ChatColor.RED + "你没有权限执行该命令");
				return true;
			}
			String arg1 = null;
			try {
				arg1 = args[1];
			} catch (Exception e) {
				sender.sendMessage("[Lord]" + ChatColor.RED + "[内容]不可为空");
				Help.Lord.getSubCommandHelp("notice").getHelpMessage(getName()).send(sender);
				return true;
			}
			TMessage message = TMessage.getBy(arg1, true);
			for (Player p : Bukkit.getOnlinePlayers()) {
				new Thread(() -> {
					for (int i = 0; i < Config.Lord_Notice_command_times; i++) {
						message.send(p);
						try {
							Thread.sleep(Config.Lord_Notice_command_delay);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
			return true;
		}
		return false;
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		getLogger().info(getName() + "插件已被停用 ");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		saveDefaultConfig();
		Vars.config = TConfig.getConfigFile(getName(), "config.yml");
		Config.read(Vars.config);
		notice_size = Config.Lord_Notice_content.size();
		if (notice_size != 0)
			Bukkit.getScheduler().runTaskTimer(this, () -> {
				String content = "";
				try {
					if (notice_index == notice_size)
						notice_index = 0;
					content = Config.Lord_Notice_content.get(notice_index);
					notice_index++;
					content = new String(content.getBytes(), "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				TMessage message = TMessage.getBy(content, true);
				for (Player p : Bukkit.getOnlinePlayers()) {
					message.send(p);
				}
			}, 0, Config.Lord_Notice_time * 20);
		Event.registListener(EquipAPIListener.instance);
		Bukkit.getPluginManager().registerEvents(ZhiyeBukkitListener.instance, this);
		Bukkit.getPluginManager().registerEvents(PlayerInfoListener.instance, this);
		Equipment.loadConfig();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return Help.Lord.getTabComplete(getName(), sender, command, alias, args);
	}
}
