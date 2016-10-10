package com.github.xzzpig.psm;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;

public class Help {
	public static TCommandHelp PigServerManager = new TCommandHelp("pigservermanager", "PigServerManager的主命令", "/psm help");

	static {
		PigServerManager.addSubCommandHelp("addid", "添加新ID","","[ID] <密码>");
		PigServerManager.addSubCommandHelp("psmlist", "获取连接的管理器列表","","");
	}
}
