package com.github.xzzpig.lord;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;

public class Help {
	public static TCommandHelp Lord = new TCommandHelp("Lord", "Lord的主命令", "/lord help");
	static {
		Lord.addSubCommandHelp("notice", "发布一个临时(刷屏)公告", null, "[内容]");
		TCommandHelp Lord_Shop = Lord.addSubCommandHelp("shop", "商店相关命令", "/lord shop help", null);
		Lord_Shop.addSubCommandHelp("open", "打开商店", "", "<Shop> <Player>");
		TCommandHelp Lord_Market = Lord.addSubCommandHelp("market", "交易相关命令", "/lord market help",null);
		Lord_Market.addSubCommandHelp("open", "打开交易市场", "", "");
		Lord_Market.addSubCommandHelp("create", "上架物品", "手中拿着要上架的商品输入", "<Price> <exp>");
	}
}
