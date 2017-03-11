package com.github.xzzpig.pigattribute;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PigAttributeListener implements Listener {
	public static final PigAttributeListener instance = new PigAttributeListener();

	private PigAttributeListener() {
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		double damage = event.getDamage();
		if (event.getDamager() instanceof Player) {
			damage += Attribute.getFrom((Player) event.getDamager()).getAttribute("damage", 0d);
		}
		damage -= Attribute.getFrom((Player) event.getEntity()).getAttribute("defence", 0d);
		event.setDamage(damage < 0 ? 0 : damage);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().setMaxHealth(Attribute.getFrom(event.getPlayer()).getAttribute("maxhealth", 20d));
	}
}