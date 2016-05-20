package com.github.xzzpig.expbank;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;

public class Help {
	public static final TCommandHelp EB = new TCommandHelp("eb", "ExpBank的主命令",
			"输入/eb help 查看帮助");

	static {
		EB.addSubCommandHelp("save", "保存经验", "/eb save", "");
		EB.addSubCommandHelp("take", "提取经验", "/eb take", "");
	}
}
