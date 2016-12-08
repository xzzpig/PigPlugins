package com.github.xzzpig.lord.player;

import java.util.Map;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.github.xzzpig.lord.Vars;

public class PlayerInfoListener implements Listener {
	public static final PlayerInfoListener instance = new PlayerInfoListener();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		double damage = event.getFinalDamage();
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			PlayerInfo damagerinfo = PlayerInfo.getBy(damager.getName());
			damage += damagerinfo.getDamage_P();
			Map<String, Integer> cri = damagerinfo.getCritical();
			int i = Vars.random.nextInt(100);
			int prob = cri.get("prob");
			if (i <= prob) {
				damage += cri.get("damage_p");
			}
			double lifesteal = damagerinfo.getLifeSteal();
			Damageable damageable = damager;
			double dh = damageable.getHealth() + lifesteal;
			double dmh = damageable.getMaxHealth();
			if (dh > dmh) {
				dh = dmh;
			}
			damageable.setHealth(dh);
		}
		PlayerInfo playerInfo = PlayerInfo.getBy(player.getName());
		damage -= playerInfo.getArmor_P();
		damage = damage > 0 ? damage : 0;
		double eva = playerInfo.getEvasion();
		double i = Vars.random.nextDouble();
		if (i < eva) {
			damage = 0;
		}
		playerInfo.fresh();
		event.setDamage(damage);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerStaticInfoFormat(PlayerInfoFormatEvent event) {
		Map<String, Object> data = event.getData();
		if (data == null || !data.containsKey("command"))
			return;
		String command = (String) data.get("command");
		switch (command) {
		case "zhiye":
			String player = ((PlayerInfo) data.get("info")).getPlayerName();
			data.put("result", Vars.playerStaticInfo.getString(player + ".zhiye", "NONE"));
			break;
		}
	}
}
