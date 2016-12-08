package com.github.xzzpig.lord.equip;

import org.bukkit.ChatColor;

import com.github.xzzpig.pigapi.bukkit.event.StringMatcherEvent;
import com.github.xzzpig.pigapi.event.EventHandler;
import com.github.xzzpig.pigapi.event.Listener;

public class EquipAPIListener implements Listener{
	public static final EquipAPIListener instance = new EquipAPIListener();
	
	@EventHandler
	public void onStringBuild(StringMatcherEvent event){
		if(!event.getData().containsKey("equip")) return;
		Equipment equip = (Equipment) event.getData().get("equip");
		if(equip!=null){
			
		}
		String str = event.getSouce();
		str = str.replace('&', ChatColor.COLOR_CHAR);
		event.setSouce(str);
	}
}
