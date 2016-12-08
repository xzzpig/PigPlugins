package com.github.xzzpig.lord.player.zhiye;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.xzzpig.lord.Vars;
import com.github.xzzpig.lord.player.IPlayerInfo;
import com.github.xzzpig.lord.player.PlayerInfo;
import com.github.xzzpig.lord.player.PlayerInfoFormatEvent;

public class ZhiyeBukkitListener implements Listener {
	public static final ZhiyeBukkitListener instance = new ZhiyeBukkitListener();

	@SuppressWarnings("unchecked")
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInfoFormat(PlayerInfoFormatEvent event) {
		Map<String, Object> data = event.getData();
		if (data == null || !data.containsKey("command"))
			return;
		String command = (String) data.get("command");
		if (command.equalsIgnoreCase("zhiye"))
			return;
		if (command.equalsIgnoreCase("level"))
			return;
		if (command.equalsIgnoreCase("health"))
			return;
		Zhiye zhiye = Zhiye.getBy(((IPlayerInfo) data.get("info")).getZhiye());
		double d = 0;
		int i = 0;
		switch (command) {
		case "armor_m":
			d = (double) data.get("result");
			d += zhiye.getAttribute().getArmor_M();
			data.put("result", d);
			break;
		case "armor_p":
			d = (double) data.get("result");
			d += zhiye.getAttribute().getArmor_P();
			data.put("result", d);
			break;
		case "damage_m":
			d = (double) data.get("result");
			d += zhiye.getAttribute().getDamage_M();
			data.put("result", d);
			break;
		case "damage_p":
			d = (double) data.get("result");
			d += zhiye.getAttribute().getDamage_P();
			data.put("result", d);
			break;
		case "evasion":
			d = (double) data.get("result");
			d += zhiye.getAttribute().getEvasion();
			data.put("result", d);
			break;
		case "maxmp":
			d = (double) data.get("result");
			d += zhiye.getAttribute().getMaxMP();
			data.put("result", d);
			break;
		case "lifesteal":
			d = (double) data.get("result");
			d += zhiye.getAttribute().getLifeSteal();
			data.put("result", d);
			break;
		case "maxhealth":
			d = (double) data.get("result");
			d += zhiye.getAttribute().getMaxHealth();
			data.put("result", d);
			break;
		case "speed":
			i = (int) data.get("result");
			i += zhiye.getAttribute().getSpeed();
			data.put("result", i);
			break;
		case "critical":
			Map<String, Integer> map = (Map<String, Integer>) data.get("result"),
					map2 = zhiye.getAttribute().getCritical();
			int prob = map.get("prob") + map2.get("prob"), damage_p = map.get("damage_p") + map2.get("damage_p"),
					damage_m = map.get("damage_m") + map2.get("damage_m");
			map.put("prob", prob);
			map.put("damage_p", damage_p);
			map.put("damage_m", damage_m);
			break;
		case "permission":
			List<String> pers = (List<String>) data.get("result");
			pers.addAll(zhiye.getPermission());
			break;
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerInfo info = PlayerInfo.getBy(player.getName());
		String zhiye = info.getZhiye();
		if (zhiye == null || zhiye.equalsIgnoreCase("NONE")) {
			zhiye = Zhiye.getRandomDefaultZhiye().getName();
			Vars.playerStaticInfo.set(player.getName() + ".zhiye", zhiye);
			player.sendMessage("[Lord]你的随机选取职业为:" + zhiye);
		}
		info.fresh();
	}
}
