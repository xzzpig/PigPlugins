package com.github.xzzpig.multiserverchat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;
import com.github.xzzpig.pigapi.bukkit.TCommandHelp.CommandInstance;

public class Help {
	public static TCommandHelp MultiServerChat = new TCommandHelp("multiserverchat", "MultiServerChat的主命令",
			"/msc help");
	public static TCommandHelp MultiServerChat_Switch = MultiServerChat
			.addSubCommandHelp("switch", "进入/退出跨服聊天频道", null, "<player>")
			.setCommandRunner(Help::command_MultiServerChat_Switch);

	@SuppressWarnings("deprecation")
	public static boolean command_MultiServerChat_Switch(CommandInstance cmd) {
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
				return true;
			}
		}
		cmd.sender.sendMessage("[MultiServerChat]玩家" + p + "未在线");
		return true;
	}
}
