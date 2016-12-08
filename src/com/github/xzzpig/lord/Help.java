package com.github.xzzpig.lord;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;

public class Help {
	public static TCommandHelp Lord = new TCommandHelp("Lord", "Lord的主命令", "/lord help");
	static {
		Lord.addSubCommandHelp("notice", "发布一个临时(刷屏)公告", null, "[内容]");
		TCommandHelp Lord_Shop = Lord.addSubCommandHelp("shop", "商店命令", "/lord shop help", null);
		Lord_Shop.addSubCommandHelp("open", "打开商店", "", "<Shop> <Player>");
	}
}
