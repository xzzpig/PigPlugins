package com.github.xzzpig.areaapi;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BukkitListener implements Listener {

	public static final BukkitListener instance = new BukkitListener();

	private BukkitListener() {
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onAreaSelect(PlayerInteractEvent event) {
		if (!event.hasBlock())
			return;
		if (!event.hasItem())
			return;
		if (event.getItem().getTypeId() != AreaAPI.selectTool)
			return;
		Player player = event.getPlayer();
		if (!player.hasPermission("areaapi.toolselect")) {
			player.sendMessage("&6[AreaAPI]&4你没有使用Tool选择区域的权限(areaapi.toolselect)".replace('&', ChatColor.COLOR_CHAR));
			return;
		}
		int id = -1;
		if (event.getAction() == Action.LEFT_CLICK_BLOCK)
			id = 0;
		else if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
			id = 1;
		if (id == -1)
			return;
		event.setCancelled(true);
		Location loc = event.getClickedBlock().getLocation();
		AreaAPI.setPlayerSelectedLoc(player.getName(), id, loc);
		player.sendMessage(ChatColor.GOLD + "[AreaAPI]" + ChatColor.RESET + "已将位置{" + loc.getWorld().getName() + ","
				+ loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "}选为记录点" + id);
	}
}
