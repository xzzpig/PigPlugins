package com.github.xzzpig.lord.player;

import java.util.Map;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerInfoFormatEvent extends Event implements Cancellable{
    private static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {
        return handlers;
    }
    private boolean cancel;
    
    private Map<String,Object> data;

    public PlayerInfoFormatEvent(Map<String,Object> data) {
        this.data = data;
    }

    public Map<String,Object> getData() {
        return data;
    }

    @Override
	public HandlerList getHandlers() {
        return handlers;
    }

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancel =arg0;
	}
}
