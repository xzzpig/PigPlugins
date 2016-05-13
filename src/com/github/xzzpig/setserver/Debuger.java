
package com.github.xzzpig.setserver;

import org.bukkit.entity.Player;

import com.github.xzzpig.pigapi.bukkit.TPlayer;


public class Debuger {
	public static long time;

	public static void print(Object s) {
		if (Main.debug== false)
			return;
		System.out.println("\n****************\n" + s + "\n****************");
		for (Player p : TPlayer.getAllPlayers()) {
			if (p.isOp())
				p.sendMessage(s + "");
		}
	}

	public static void timeStart() {
		time = System.nanoTime();
	}

	public static void timeStop(String s) {
		Debuger.print(s + (System.nanoTime() - time));
	}
}
