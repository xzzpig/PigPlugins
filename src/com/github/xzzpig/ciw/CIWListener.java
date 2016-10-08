package com.github.xzzpig.ciw;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.Inventory;

public class CIWListener implements Listener{
	public static final CIWListener self = new CIWListener();
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		World from = event.getFrom(),to = event.getPlayer().getWorld();
		if(Vars.cw.contains(to.getName())&&(!Vars.cw.contains(from.getName()))){
			player.setGameMode(GameMode.CREATIVE);
			Vars.json.put(player.getName(), player.getInventory());
			player.sendMessage("[CIW]你已切换到创造模式");
		} else if(Vars.cw.contains(from.getName())&&(!Vars.cw.contains(to.getName()))){
			player.setGameMode(GameMode.SURVIVAL);
			player.getInventory().clear();
			if (Vars.json.keySet().contains(player.getName())) {
				player.getInventory().addItem(((Inventory)Vars.json.get(player.getName())).getContents());				
			}
			Vars.json.remove(player.getName());
			player.sendMessage("[CIW]你已切换到生存模式");
		}
	}
}
