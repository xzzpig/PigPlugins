package com.github.xzzpig.lord;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;

public class Help {
	public static TCommandHelp Lord = new TCommandHelp("Lord", "Lord的主命令", "/lord help");

	static {
		Lord.addSubCommandHelp("notice","发布一个临时(刷屏)公告",null,"[内容]");
	}
}
