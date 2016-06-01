package com.github.xzzpig.batedamage.listener;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.xzzpig.batedamage.Main;
import com.github.xzzpig.pigapi.bukkit.TConfig;

public class BDListener implements Listener {
	private static List<String> except = TConfig.getConfigFile("BateDamage",
			"config.yml").getStringList("batedamage.no_count_entity");
	public static FileConfiguration data = TConfig.getConfigFile("BateDamage",
			"data.yml");

	@EventHandler
	public void onKill(EntityDeathEvent event) {
		if (except.contains(event.getEntityType().name()))
			return;
		if (event.getEntity().getKiller() == null)
			return;
		Player player = event.getEntity().getKiller();
		long account = data.getLong(player.getName());
		data.set(player.getName(), account + 1);
		TConfig.saveConfig("BateDamage", data, "data.yml");
		Main.buildPermisiom(player);
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		Main.buildPermission();

	}

	@EventHandler
	public void onPlayerDeath(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		long account = data.getLong(player.getName());
		data.set(player.getName(),
				Main.get2x(Main.accountToBate(account) - 1) - 2);
		TConfig.saveConfig("BateDamage", data, "data.yml");
		Main.buildPermisiom(player);
	}
}
