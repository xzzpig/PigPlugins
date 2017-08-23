package com.github.xzzpig.multiserverchat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;
import com.github.xzzpig.pigapi.bukkit.TCommandHelp.CommandInstance;

public class Help {
	public static TCommandHelp MultiServerChat = new TCommandHelp("multiserverchat", "MultiServerChat的主命令",
			"/msc help");
	public static TCommandHelp MultiServerChat_Switch = MultiServerChat
			.addSubCommandHelp("switch", "进入/退出跨服聊天频道", null, "<player>").setCommandRunner(Help::switch_);

	public static TCommandHelp MultiServerChat_Join = MultiServerChat.addSubCommandHelp("join", "进入", null, "<player>")
			.setCommandRunner(Help::join);
	public static TCommandHelp MultiServerChat_Exit = MultiServerChat.addSubCommandHelp("join", "进入", null, "<player>")
			.setCommandRunner(Help::exit);

	@SuppressWarnings("deprecation")
	public static boolean switch_(CommandInstance cmd) {
		String p = null;
		if (cmd.args.length >= 2)
			p = cmd.args[1];
		if (p == null) {
			if (cmd.sender instanceof Player)
				p = cmd.sender.getName();
			else {
				return false;
			}
		}
		if (p.equalsIgnoreCase(cmd.sender.getName())) {
			if (!cmd.sender.hasPermission("multiserverchat.switch.other")) {
				cmd.sender.sendMessage("[MultiServerChat]你没有权限(multiserverchat.switch.other)");
				return true;
			}
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getName().equalsIgnoreCase(p)) {
				player.chat("*");
				cmd.sendMsg("已更改玩家" + p + "跨服聊天频道");
				return true;
			}
		}
		cmd.sender.sendMessage("[MultiServerChat]玩家" + p + "未在线");
		return true;
	}

	public static boolean join(CommandInstance cmd) {
		String p = null;
		if (cmd.args.length >= 2)
			p = cmd.args[1];
		if (p == null) {
			if (cmd.sender instanceof Player)
				p = cmd.sender.getName();
			else {
				return false;
			}
		}
		if (p.equalsIgnoreCase(cmd.sender.getName())) {
			if (!cmd.sender.hasPermission("multiserverchat.switch.other")) {
				cmd.sender.sendMessage("[MultiServerChat]你没有权限(multiserverchat.switch.other)");
				return true;
			}
		}
		Main.instance.joinPlayer(p);
		cmd.sender.sendMessage("[MultiServerChat]已将玩家" + p + "加入到跨服聊天频道");
		return true;
	}

	public static boolean exit(CommandInstance cmd) {
		String p = null;
		if (cmd.args.length >= 2)
			p = cmd.args[1];
		if (p == null) {
			if (cmd.sender instanceof Player)
				p = cmd.sender.getName();
			else {
				return false;
			}
		}
		if (p.equalsIgnoreCase(cmd.sender.getName())) {
			if (!cmd.sender.hasPermission("multiserverchat.switch.other")) {
				cmd.sender.sendMessage("[MultiServerChat]你没有权限(multiserverchat.switch.other)");
				return true;
			}
		}
		Main.instance.exitPlayer(p);
		cmd.sender.sendMessage("[MultiServerChat]已将玩家" + p + "移出到跨服聊天频道");
		return true;
	}
}
