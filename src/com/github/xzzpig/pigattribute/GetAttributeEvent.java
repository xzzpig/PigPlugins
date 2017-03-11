package com.github.xzzpig.pigattribute;

import org.bukkit.entity.Player;

import com.github.xzzpig.pigapi.event.Event;

public class GetAttributeEvent<T> extends Event {
	private Player p;
	private Class<T> type;

	private T result;

	public GetAttributeEvent(Player p, Class<T> type) {
		this.p = p;
		this.type = type;
	}

	public Player getPlayer() {
		return p;
	}

	public Class<T> getType() {
		return type;
	}

	public T getResult() {
		return result;
	}

	public GetAttributeEvent<T> setResult(T r) {
		this.result = r;
		return this;
	}
}
