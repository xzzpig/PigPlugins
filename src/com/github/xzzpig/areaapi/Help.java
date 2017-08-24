package com.github.xzzpig.areaapi;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.xzzpig.areaapi.Area.Space;
import com.github.xzzpig.areaapi.TCommandHelp.CommandInstance;

public class Help {

	public static TCommandHelp AreaAPI = new TCommandHelp("AreaAPI", "PigAPI的主命令", "/aa help");
	public static TCommandHelp AreaAPI_Add = AreaAPI
			.addSubCommandHelp("add", "将记录点0和记录点1间的区域加入到Area[name]中", null, "[name]")
			.setPermission("areaapi.command.add").addLimit(cmd -> limitArgsLength(cmd, 2)).setCommandRunner(Help::add);
	public static TCommandHelp AreaAPI_Create = AreaAPI.addSubCommandHelp("create", "新建Area[name]", null, "[name]")
			.setPermission("areaapi.command.create").addLimit(cmd -> limitArgsLength(cmd, 2))
			.setCommandRunner(Help::create);
	public static TCommandHelp AreaAPI_Data = AreaAPI.addSubCommandHelp("data", "显示Area的Data", null, "[name]")
			.setPermission("areaapi.command.data").addLimit(cmd -> limitArgsLength(cmd, 2))
			.setCommandRunner(Help::data);
	public static TCommandHelp AreaAPI_Delete = AreaAPI.addSubCommandHelp("delete", "删除Area[name]", null, "[name]")
			.setPermission("areaapi.command.delete").addLimit(cmd -> limitArgsLength(cmd, 2))
			.setCommandRunner(Help::delete);
	public static TCommandHelp AreaAPI_Exclude = AreaAPI
			.addSubCommandHelp("exclude", "将记录点0和记录点1间的区域从Area[name]中排除", null, "[name]")
			.setPermission("areaapi.command.exclude").addLimit(cmd -> limitArgsLength(cmd, 2))
			.setCommandRunner(Help::exclude);
	public static TCommandHelp AreaAPI_List = AreaAPI.addSubCommandHelp("list", "列出当前所在的Area", null, null)
			.setPermission("areaapi.command.list").addLimit(TCommandHelp.isPlayer).setCommandRunner(Help::list);
	public static TCommandHelp AreaAPI_ListAll = AreaAPI.addSubCommandHelp("listall", "列出所有Area", null, null)
			.setPermission("areaapi.command.listall").setCommandRunner(Help::listall);
	public static TCommandHelp AreaAPI_Select = AreaAPI.addSubCommandHelp("select", "选择当前所在位置为记录点[num]", null, "[num]")
			.setPermission("areaapi.command.select").addLimit(cmd -> limitArgsLength(cmd, 2))
			.setCommandRunner(Help::select).addLimit(TCommandHelp.isPlayer);

	public static boolean add(CommandInstance cmd) {
		Location loc0 = com.github.xzzpig.areaapi.AreaAPI.getPlayerSelectedLoc(cmd.sender.getName(), 0);
		if (loc0 == null) {
			cmd.sendMsg("&4请先选择记录点0");
			return true;
		}
		Location loc1 = com.github.xzzpig.areaapi.AreaAPI.getPlayerSelectedLoc(cmd.sender.getName(), 1);
		if (loc1 == null) {
			cmd.sendMsg("&4请先选择记录点1");
			return true;
		}
		String area = cmd.args[1];
		if (!com.github.xzzpig.areaapi.AreaAPI.isAreaExist(area)) {
			cmd.sendMsg("&4Area &2" + area + " &4不存在");
			return true;
		}
		Space space = com.github.xzzpig.areaapi.AreaAPI.getArea(area).addSpace(loc0, loc1);
		cmd.sendMsg("已将区域加入到Area &2" + area + " &r中");
		cmd.sender.sendMessage(space.toJSONObject2().toString(2).split("\n"));
		return true;
	}

	public static boolean exclude(CommandInstance cmd) {
		Location loc0 = com.github.xzzpig.areaapi.AreaAPI.getPlayerSelectedLoc(cmd.sender.getName(), 0);
		if (loc0 == null) {
			cmd.sendMsg("&4请先选择记录点0");
			return true;
		}
		Location loc1 = com.github.xzzpig.areaapi.AreaAPI.getPlayerSelectedLoc(cmd.sender.getName(), 1);
		if (loc1 == null) {
			cmd.sendMsg("&4请先选择记录点1");
			return true;
		}
		String area = cmd.args[1];
		if (!com.github.xzzpig.areaapi.AreaAPI.isAreaExist(area)) {
			cmd.sendMsg("&4Area &2" + area + " &4不存在");
			return true;
		}
		Space space = com.github.xzzpig.areaapi.AreaAPI.getArea(area).addExcludeSpace(loc0, loc1);
		cmd.sendMsg("已将区域从Area &2" + area + " &r中排除");
		cmd.sender.sendMessage(space.toJSONObject2().toString(2).split("\n"));
		return true;
	}

	public static boolean create(CommandInstance cmd) {
		String name = cmd.args[1];
		if (com.github.xzzpig.areaapi.AreaAPI.isAreaExist(name)) {
			cmd.sendMsg("&4Area &2" + name + " &4已存在");
			return true;
		}
		try {
			com.github.xzzpig.areaapi.AreaAPI.getArea(name);
			cmd.sendMsg("Area &2" + name + " &r创建成功");
		} catch (Exception e) {
			e.printStackTrace();
			cmd.sendMsg("Area &2" + name + " &r创建失败(" + e.getMessage() + ")");
		}
		return true;
	}

	public static boolean data(CommandInstance cmd) {
		String name = cmd.args[1];
		if (!com.github.xzzpig.areaapi.AreaAPI.isAreaExist(name)) {
			cmd.sendMsg("&4Area &2" + name + " &4不存在");
			return true;
		}
		cmd.sendMsg("&3Area &2" + name + " &3Data:");
		Arrays.asList(com.github.xzzpig.areaapi.AreaAPI.getArea(name).getData().toString(2).split("\n"))
				.forEach(cmd.sender::sendMessage);
		return true;
	}

	public static boolean delete(CommandInstance cmd) {
		String name = cmd.args[1];
		if (!com.github.xzzpig.areaapi.AreaAPI.isAreaExist(name)) {
			cmd.sendMsg("&4Area &2" + name + " &4不存在");
			return true;
		}
		if (com.github.xzzpig.areaapi.AreaAPI.deleteArea(name)) {
			cmd.sendMsg("Area &2" + name + " &r删除成功");
		} else
			cmd.sendMsg("Area &2" + name + " &r删除失败");
		return true;
	}

	private static TMessage limitArgsLength(CommandInstance cmd, int length) {
		if (cmd.args.length >= length)
			return null;
		return new TMessage("[PigAPI]").color(ChatColor.GOLD).then("本命令参数长度不得小于" + length).color(ChatColor.RED);
	}

	public static boolean listall(CommandInstance cmd) {
		cmd.sendMsg("&3所有Area列表:");
		com.github.xzzpig.areaapi.AreaAPI.allAreaStream().forEach(cmd.sender::sendMessage);
		return true;
	}

	static String loc2String(Location loc) {
		return "{" + loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ()
				+ "}";
	}

	public static boolean select(CommandInstance cmd) {
		int id = 0;
		try {
			id = Integer.parseInt(cmd.args[1]);
		} catch (Exception e) {
			cmd.sendMsg("&4[num]应是数字");
			return false;
		}
		Player player = (Player) cmd.sender;
		Location loc = player.getLocation();
		com.github.xzzpig.areaapi.AreaAPI.setPlayerSelectedLoc(player.getName(), id, loc);
		cmd.sendMsg("已将位置" + loc2String(loc) + "选为记录点" + id);
		return true;
	}

	public static boolean list(CommandInstance cmd) {
		Player p = (Player) cmd.sender;
		Location loc = p.getLocation();
		cmd.sendMsg("当前位置Area列表:");
		Arrays.asList(com.github.xzzpig.areaapi.AreaAPI.listAllArea()).stream()
				.map(com.github.xzzpig.areaapi.AreaAPI::getArea).filter(area -> area.in(loc))
				.map(area -> "  " + area.name).forEach(cmd.sender::sendMessage);
		return true;
	}
}
