
package com.github.xzzpig.psm;

import org.bukkit.entity.Player;

import com.github.xzzpig.pigapi.TString;
import com.github.xzzpig.pigapi.bukkit.TPlayer;


public class Debuger {
	public static void print(Object s) {
		if (Vars.debug== false)
			return;
		System.out.println(TString.toString(s));
		for (Player p : TPlayer.getAllPlayers()) {
			if (p.isOp())
				p.sendMessage(TString.toString(s));
		}
	}
}
