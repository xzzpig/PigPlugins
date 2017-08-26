package com.github.xzzpig.areaapi;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.xzzpig.areaapi.event.PlayerMoveInAreaEvent;
import com.github.xzzpig.areaapi.event.PlayerMoveOutAreaEvent;
import com.github.xzzpig.pigutils.event.Event;

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

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onAreaIn(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		AreaAPI.allAreaStream().map(AreaAPI::getArea).filter(area -> area.in(player.getLocation()))
				.filter(area -> ((!area.tmpMap.containsKey("inPlayerList"))
						|| !((List<String>) area.tmpMap.get("inPlayerList")).contains(player.getName())))
				.forEach(area -> onInArea(area, player, event));
		AreaAPI.getLoadedAreas().parallelStream().filter(area -> !area.in(player.getLocation()))
				.filter(area -> area.tmpMap.containsKey("inPlayerList"))
				.filter(area -> ((List<String>) area.tmpMap.get("inPlayerList")).contains(player.getName()))
				.forEach(area -> onOutArea(area, player, event));
	}

	private void onInArea(Area area, Player player, PlayerMoveEvent moveEvent) {
		if (!area.tmpMap.containsKey("inPlayerList"))
			area.tmpMap.put("inPlayerList", new ArrayList<String>());
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) area.tmpMap.get("inPlayerList");
		list.add(player.getName());
		PlayerMoveInAreaEvent event = new PlayerMoveInAreaEvent(player, area, moveEvent);
		Event.callEvent(event);
		if (event.isCanceled() || moveEvent.isCancelled()) {
			moveEvent.setCancelled(true);
			event.setCanceled(true);
			list.remove(player.getName());
		}
	}

	private void onOutArea(Area area, Player player, PlayerMoveEvent moveEvent) {
		if (!area.tmpMap.containsKey("inPlayerList"))
			return;
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) area.tmpMap.get("inPlayerList");
		PlayerMoveOutAreaEvent event = new PlayerMoveOutAreaEvent(player, area, moveEvent);
		Event.callEvent(event);
		if (event.isCanceled() || moveEvent.isCancelled()) {
			moveEvent.setCancelled(true);
			event.setCanceled(true);
		} else {
			list.remove(player.getName());
			if (list.size() == 0)
				area.tmpMap.remove("inPlayerList");
		}
	}
}
