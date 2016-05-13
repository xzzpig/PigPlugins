package com.github.xzzpig.setserver;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import com.github.xzzpig.pigapi.bukkit.TPlayer;

public class SSListener implements Listener {
	@EventHandler
	public void onGetExp(PlayerExpChangeEvent event){
		Player player = event.getPlayer();
		int amount = event.getAmount();
		event.setAmount(0);
		int level = player.getLevel();
		int levelmaxexp = TPlayer.toLevelExp(level);
		if(Main.levelexp.containsKey(level))
			levelmaxexp = Main.levelexp.get(level);
		int exptolevel = levelmaxexp - (int)player.getExp()*levelmaxexp;
		if(amount>=exptolevel){
			Debuger.print("LevelUp!");
			level=level+1;
			player.setLevel(level);
			amount=amount-exptolevel;
			levelmaxexp = TPlayer.toLevelExp(level);
			if(Main.levelexp.containsKey(level))
				levelmaxexp = Main.levelexp.get(level);
		}
		int exp = (int) (player.getExp()*levelmaxexp);
		float pexp = (float)(exp+amount)/((float)levelmaxexp);
		player.setExp(pexp);
		Debuger.print("Exp:+"+amount+"|"+"Lv"+level+":"+(exp+amount)+"/"+levelmaxexp+"("+(int)(pexp*100)+"%)");
	}
}
