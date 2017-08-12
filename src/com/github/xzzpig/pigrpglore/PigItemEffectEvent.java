package com.github.xzzpig.pigrpglore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.xzzpig.pigutils.annoiation.NotNull;
import com.github.xzzpig.pigutils.event.Event;

public class PigItemEffectEvent extends Event {
	String effectTime;
	ItemStack item;
	Player player;
	List<String> lores;

	public PigItemEffectEvent(@NotNull ItemStack item, Player p, String effectTime) {
		this.item = item;
		this.player = p;
		this.effectTime = effectTime;
		this.lores = item.getItemMeta().getLore();
		if(this.lores==null)
			this.lores = new ArrayList<>();
	}

	public @NotNull List<String> getLores(){
		return this.lores;
	}
	
	public String getEffectTime() {
		return effectTime;
	}

	public ItemStack getItem() {
		return item;
	}

	public Player getPlayer() {
		return player;
	}
}
