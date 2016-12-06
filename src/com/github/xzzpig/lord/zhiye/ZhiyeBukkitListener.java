package com.github.xzzpig.lord.zhiye;

import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.github.xzzpig.lord.player.IPlayerInfo;
import com.github.xzzpig.lord.player.PlayerInfoFormatEvent;

public class ZhiyeBukkitListener implements Listener{
	public static final ZhiyeBukkitListener instance = new ZhiyeBukkitListener();
	
	@SuppressWarnings("unchecked")
	@EventHandler(priority =EventPriority.LOW)
	public void onPlayerInfoFormat(PlayerInfoFormatEvent event){
		Map<String, Object> data = event.getData();
		if(data == null||!data.containsKey("command"))return;
		String command = (String) data.get("command");
		if(command.equalsIgnoreCase("zhiye"))return;
		if(command.equalsIgnoreCase("level"))return;
		if(command.equalsIgnoreCase("health"))return;
		Zhiye zhiye = Zhiye.getBy(((IPlayerInfo)data.get("info")).getZhiye());
		double d = 0;
		int i = 0;
		switch (command) {
		case "armor_m":
			d = (double) data.get("result");
			d+=zhiye.getAttribute().getArmor_M();
			data.put("result",d);
			break;
		case "armor_p":
			d = (double) data.get("result");
			d+=zhiye.getAttribute().getArmor_P();
			data.put("result",d);
		case "damage_m":
			d = (double) data.get("result");
			d+=zhiye.getAttribute().getDamage_M();
			data.put("result",d);
		case "damage_p":
			d = (double) data.get("result");
			d+=zhiye.getAttribute().getDamage_P();
			data.put("result",d);
		case "evasion":
			d = (double) data.get("result");
			d+=zhiye.getAttribute().getEvasion();
			data.put("result",d);
		case "hunger":
			d = (double) data.get("result");
			d+=zhiye.getAttribute().getHunger();
			data.put("result",d);
		case "lifesteal":
			d = (double) data.get("result");
			d+=zhiye.getAttribute().getLifeSteal();
			data.put("result",d);
		case "maxhealth":
			d = (double) data.get("result");
			d+=zhiye.getAttribute().getMaxHealth();
			data.put("result",d);
		case "speed":
			i = (int) data.get("result");
			i+=zhiye.getAttribute().getSpeed();
			data.put("result",i);
		case "critical":
			Map<String,Integer> 
			map = (Map<String, Integer>) data.get("result"),
			map2 = zhiye.getAttribute().getCritical();
			int 
			prob = map.get("prob")+map2.get("prob"),
			damage_p = map.get("damage_p")+map2.get("damage_p"),
			damage_m = map.get("damage_m")+map2.get("damage_m");
			map.put("prob", prob);
			map.put("damage_p", damage_p);
			map.put("damage_m", damage_m);
		}
	}
}
