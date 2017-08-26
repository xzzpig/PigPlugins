package com.github.xzzpig.areaapi.event;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.xzzpig.areaapi.Area;
import com.github.xzzpig.pigutils.annoiation.NotNull;

public class PlayerMoveOutAreaEvent extends AreaEvent {

	protected Player p;
	protected PlayerMoveEvent event;
	public PlayerMoveOutAreaEvent(@NotNull Player player,@NotNull Area area,PlayerMoveEvent event) {
		super(area);
		this.p = player;
	}

	public Player getPlayer() {
		return p;
	}
	
	public PlayerMoveEvent getPlayerMoveEvent(){
		return event;
	}
}
