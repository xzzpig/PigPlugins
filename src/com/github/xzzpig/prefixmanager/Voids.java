package com.github.xzzpig.prefixmanager;

import org.bukkit.entity.Player;

import com.github.xzzpig.pigapi.bukkit.TPlayer;
import com.github.xzzpig.pigapi.bukkit.TPrefix;

public class Voids {
	public static void buildPrefix(Player player) {
		String group = Vars.data.getString("player." + player.getName()
				+ ".group");
		// System.out.println(group);
		if (group != null) {
			String prefix = Vars.data.getString("group." + group);
			if (prefix != null)
				TPrefix.setPrefix(player.getName(), prefix, "PMGroup");
			// System.out.print("G:"+group+"|"+prefix+"||");
		}
		String prefix = Vars.data.getString("player." + player.getName()
				+ ".prefix");
		if (prefix != null)
			TPrefix.setPrefix(player.getName(), prefix, "PMPlayer");
		// System.out.println("P:"+prefix);
	}

	public static void buildAllPrefix() {
		for (Player player : TPlayer.getAllPlayers())
			buildPrefix(player);
	}

	public static void saveData() {
		Vars.data.saveToFile(Vars.dataFile);
	}
}
