package com.github.xzzpig.pigattribute;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.github.xzzpig.pigapi.event.Event;

public class Attribute {
	private static Map<Player, Attribute> map = new HashMap<>();

	public static Attribute getFrom(Player p) {
		return map.containsKey(p) ? map.get(p) : map.put(p, new Attribute(p));
	}

	private Player p;

	private Attribute(Player p) {
		this.p = p;
	}

	public <T> T getAttribute(String name, Class<T> type) {
		GetAttributeEvent<T> eve = new GetAttributeEvent<>(p, type);
		Event.callEvent(eve);
		return eve.getResult();
	}

	public Player getPlayer() {
		return p;
	}
}
