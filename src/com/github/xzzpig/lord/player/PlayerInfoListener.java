package com.github.xzzpig.lord.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerInfoListener implements Listener {
	public static final PlayerInfoListener instance = new PlayerInfoListener();

	@EventHandler()
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		double damage = event.getFinalDamage();
		if (event.getEntity() instanceof Player) {
			Player damager = (Player) event.getDamager();
			PlayerInfo damagerinfo = PlayerInfo.getBy(damager.getName());
			damage += damagerinfo.getDamage_P();
		}
		PlayerInfo playerInfo = PlayerInfo.getBy(player.getName());
		damage -= playerInfo.getArmor_P();
		event.setDamage(damage);
	}
}
