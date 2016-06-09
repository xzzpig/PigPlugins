package com.github.xzzpig.prefixmanager;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.xzzpig.pigapi.bukkit.event.StringMatcherEvent;

public class PrefixListener implements Listener,
com.github.xzzpig.pigapi.event.Listener {
	public static PrefixListener self = new PrefixListener();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Voids.buildAllPrefix();
	}

	@com.github.xzzpig.pigapi.event.EventHandler
	public void onStringMatch(StringMatcherEvent event) {
		LivingEntity entity = event.getEntity();
		String worldPrefix = Vars.data.getString("world."
				+ entity.getWorld().getName());
		if (worldPrefix == null)
			worldPrefix = "";
		event.setSouce(event.getSouce().replaceAll("</worldprefix/>",worldPrefix));
	}
}
