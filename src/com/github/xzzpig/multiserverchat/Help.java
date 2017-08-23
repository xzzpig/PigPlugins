package com.github.xzzpig.multiserverchat;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;
import com.github.xzzpig.pigapi.bukkit.TCommandHelp.CommandInstance;

public class Help {
	public static TCommandHelp MultiServerChat = new TCommandHelp("multiserverchat", "MultiServerChat的主命令",
			"/msc help");
	public static TCommandHelp MultiServerChat_Switch = MultiServerChat
			.addSubCommandHelp("switch", "进入/退出跨服聊天频道", null, "<player>").setCommandRunner(Help::switch_);

	public static TCommandHelp MultiServerChat_Join = MultiServerChat
			.addSubCommandHelp("join", "进入跨服聊天频道", null, "<player>").setCommandRunner(Help::join);
	public static TCommandHelp MultiServerChat_Exit = MultiServerChat
			.addSubCommandHelp("exit", "退出跨服聊天频道", null, "<player>").setCommandRunner(Help::exit);

	public static TCommandHelp MultiServerChat_Shield = MultiServerChat
			.addSubCommandHelp("shield", "屏蔽玩家[player]的跨服聊天", null, "[player]").setCommandRunner(Help::shiled);
	public static TCommandHelp MultiServerChat_UnShield = MultiServerChat
			.addSubCommandHelp("unshield", "解除屏蔽玩家[player]的跨服聊天", null, "[player]").setCommandRunner(Help::unshiled);
	public static TCommandHelp MultiServerChat_Mute = MultiServerChat
			.addSubCommandHelp("mute", "禁言玩家[player] <time>秒/解除禁言", "time单位为秒,不填为永久", "[player] <time>")
			.setCommandRunner(Help::mute);

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
			if (!cmd.sender.hasPermission("multiserverchat.join.other")) {
				cmd.sender.sendMessage("[MultiServerChat]你没有权限(multiserverchat.join.other)");
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
			if (!cmd.sender.hasPermission("multiserverchat.exit.other")) {
				cmd.sender.sendMessage("[MultiServerChat]你没有权限(multiserverchat.exit.other)");
				return true;
			}
		}
		Main.instance.exitPlayer(p);
		cmd.sender.sendMessage("[MultiServerChat]已将玩家" + p + "移出到跨服聊天频道");
		return true;
	}

	public static boolean shiled(CommandInstance cmd) {
		String p = null;
		if (cmd.args.length >= 2)
			p = cmd.args[1];
		if (p == null) {
			cmd.sendMsg("&4你不输入要屏蔽的人是想屏蔽自己么?");
			return false;
		}
		if (!cmd.sender.hasPermission("multiserverchat.shiled")) {
			cmd.sender.sendMessage("[MultiServerChat]你没有权限(multiserverchat.shiled)");
			return true;
		}
		Main.instance.shiledPlayer(cmd.sender.getName(), p);
		cmd.sender.sendMessage("[MultiServerChat]已屏蔽玩家" + p + "的跨服聊天");
		return true;
	}

	public static boolean unshiled(CommandInstance cmd) {
		String p = null;
		if (cmd.args.length >= 2)
			p = cmd.args[1];
		if (p == null) {
			return false;
		}
		if (!cmd.sender.hasPermission("multiserverchat.shiled")) {
			cmd.sender.sendMessage("[MultiServerChat]你没有权限(multiserverchat.shiled)");
			return true;
		}
		Main.instance.unshiledPlayer(cmd.sender.getName(), p);
		cmd.sender.sendMessage("[MultiServerChat]将接受玩家" + p + "的跨服聊天频道");
		return true;
	}

	public static boolean mute(CommandInstance cmd) {
		String p = null;
		if (cmd.args.length >= 2)
			p = cmd.args[1];
		if (p == null) {
			return false;
		}
		if (!cmd.sender.hasPermission("multiserverchat.mute")) {
			cmd.sender.sendMessage("[MultiServerChat]你没有权限(multiserverchat.mute)");
			return true;
		}
		long time = System.currentTimeMillis();
		if (Main.instance.muteMap.containsKey(p)) {
			time = 0;
			cmd.sendMsg("已解除玩家" + p + "的禁言");
		} else {
			if (cmd.args.length >= 3) {
				try {
					time += Long.parseLong(cmd.args[2]) * 1000;
				} catch (Exception e) {
					cmd.sendMsg("&4<time>应该为数字");
					return false;
				}
				cmd.sendMsg("&2" + p + "&3已被禁言至" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time)));
			} else {
				time = -1;
				cmd.sendMsg("&2" + p + "&3已被永久禁言(op可解禁)");
			}
		}
		Main.instance.mutePlayer(p, time);
		return true;
	}
}
