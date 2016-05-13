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
		int level = player.getLevel();
		int levelmaxexp = TPlayer.toLevelExp(level);
		if(Main.levelexp.containsKey(level))
			levelmaxexp = Main.levelexp.get(level);
		int exptolevel = levelmaxexp - (int)player.getExp()*levelmaxexp;
		if(amount>=exptolevel){
			level=level+1;
			player.setLevel(level);
			amount=amount-exptolevel;
			levelmaxexp = TPlayer.toLevelExp(level);
			if(Main.levelexp.containsKey(level))
				levelmaxexp = Main.levelexp.get(level);
		}
		player.setExp((player.getExp()*(float)levelmaxexp+(float)amount)/((float)levelmaxexp));
		Debuger.print("Exp:+"+amount+"|"+(int)(levelmaxexp*player.getExp())+"/"+levelmaxexp+"("+(int)(player.getExp()*100)+"%)");
	}
}
