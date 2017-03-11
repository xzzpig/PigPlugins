package com.github.xzzpig.pigattribute;

import org.bukkit.entity.Player;

import com.github.xzzpig.pigapi.event.Event;

public class GetAttributeEvent<T> extends Event {
	private Player p;
	private Class<T> type;
	private String name;

	private T result;

	public GetAttributeEvent(Player p, String name, Class<T> type) {
		this.p = p;
		this.type = type;
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public GetAttributeEvent(Player p, String name, T defaultValue) {
		this.p = p;
		this.type = (Class<T>) defaultValue.getClass();
		this.name = name;
		result = defaultValue;
	}

	public Player getPlayer() {
		return p;
	}

	public Class<T> getType() {
		return type;
	}

	public String getAttributeName() {
		return name;
	}

	public T getResult() {
		return result;
	}

	public GetAttributeEvent<T> setResult(T r) {
		this.result = r;
		return this;
	}
}
