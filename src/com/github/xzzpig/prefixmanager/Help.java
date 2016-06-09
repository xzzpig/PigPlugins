package com.github.xzzpig.prefixmanager;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;

public class Help {
	public static TCommandHelp PM = new TCommandHelp("prefixmanager", "PrefixManager的主命令",
			"/pm help");

	static {
		TCommandHelp PM_Group = PM.addSubCommandHelp("group", "组前缀命令",
				"/pm group help", "");
		PM_Group.addSubCommandHelp("set","设置组前缀","","[组] [前缀]");
		PM_Group.addSubCommandHelp("move","将玩家移至某组","<玩家>默认为自己","[组] <玩家>");
		PM_Group.addSubCommandHelp("remove","去除玩家组前缀","<玩家>默认为自己","<玩家>");

		TCommandHelp PM_World = PM.addSubCommandHelp("world", "世界前缀命令",
				"/pm group help", "");
		PM_World.addSubCommandHelp("set","设置世界前缀","<世界>默认为当前世界(控制台不可省略)","[前缀] <世界>");

		TCommandHelp PM_Player = PM.addSubCommandHelp("player", "个人前缀命令",
				"/pm group help", "");
		PM_Player.addSubCommandHelp("set","设置个人前缀","<玩家>默认为自己","[前缀] <玩家>");
		PM_Player.addSubCommandHelp("remove","去除玩家个人前缀","<玩家>默认为自己","<玩家>");

	}
}
