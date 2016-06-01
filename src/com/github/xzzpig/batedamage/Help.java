package com.github.xzzpig.batedamage;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;

public class Help {
	public static TCommandHelp BD = new TCommandHelp("bade", "BateData的主命令",
			"/bade help    -获取帮助");
	static {
		BD.addSubCommandHelp("set", "设置战力", "/bade set [战力] <目标>", "[战力] <目标>");
		BD.addSubCommandHelp("add", "增加战力", "/bade add [战力] <目标>", "[战力] <目标>");
		BD.addSubCommandHelp("remove", "减少战力", "/bade remove [战力] <目标>",
				"[战力] <目标>");
		 BD.addSubCommandHelp("get", "查询战力", "/bade get <目标>", "<目标>");

	}
}
