package com.github.xzzpig.lord.equip;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import com.github.xzzpig.lord.player.zhiye.Zhiye;
import com.github.xzzpig.pigapi.bukkit.event.StringMatcherEvent;
import com.github.xzzpig.pigapi.event.EventHandler;
import com.github.xzzpig.pigapi.event.Listener;

public class EquipAPIListener implements Listener {
	public static final EquipAPIListener instance = new EquipAPIListener();

	@EventHandler
	public void onStringBuild(StringMatcherEvent event) {
		if (!event.getData().containsKey("equip"))
			return;
		Equipment equip = (Equipment) event.getData().get("equip");
		String str = event.getSouce();
		if (equip != null) {
			ConfigurationSection config = equip.getConfig();
			str=str
			.replaceAll("</Equip.Name/>", config.getString("Name",""))
			.replaceAll("</Equip.ID/>", config.getInt("ID")+"")
			.replaceAll("</Equip.Item/>", config.getInt("Item")+"")
			.replaceAll("</Equip.power.Damage_p/>", config.getInt("power.Damage_p")+"")
			.replaceAll("</Equip.power.Damage_m/>", config.getInt("power.Damage_m")+"")
			.replaceAll("</Equip.power.Armor_p/>", config.getInt("power.Armor_p")+"")
			.replaceAll("</Equip.power.Armor_m/>", config.getInt("power.Armor_m")+"")
			.replaceAll("</Equip.power.Health/>", config.getInt("power.Health")+"")
			.replaceAll("</Equip.power.MP/>", config.getInt("power.MP")+"")
			.replaceAll("</Equip.power.LifeSteal/>", config.getInt("power.LifeSteal")+"")
			.replaceAll("</Equip.power.Evasion/>", config.getInt("power.Evasion")+"")
			.replaceAll("</Equip.power.Critical.damage_w/>", config.getInt("power.Critical.damage_w")+"")
			.replaceAll("</Equip.power.Critical.damage_m/>", config.getInt("power.Critical.damage_m")+"")
			.replaceAll("</Equip.power.Critical.prob/>", config.getInt("power.Critical.prob")+"")
			.replaceAll("</Equip.power.Repel.level/>", config.getInt("power.Repel.level")+"")
			.replaceAll("</Equip.power.Repel.prob/>", config.getInt("power.Repel.prob")+"")
			.replaceAll("</Equip.power.Fire.time/>", config.getInt("power.Fire.time")+"")
			.replaceAll("</Equip.power.Fire.prob/>", config.getInt("power.Fire.prob")+"")
			.replaceAll("</Equip.power.Skill.Fireball.prob/>", config.getInt("power.Skill.Fireball.prob")+"")
			.replaceAll("</Equip.power.Skill.Fireball.cool/>", config.getInt("power.Skill.Fireball.cool")+"")
			.replaceAll("</Equip.power.Skill.Fireball.damage_m/>", config.getInt("power.Skill.Fireball.damage_m")+"")
			.replaceAll("</Equip.power.Skill.Ice.prob/>", config.getInt("power.Skill.Ice.prob")+"")
			.replaceAll("</Equip.power.Skill.Ice.cool/>", config.getInt("power.Skill.Ice.cool")+"")
			.replaceAll("</Equip.power.Skill.Ice.damage_m/>", config.getInt("power.Skill.Ice.damage_m")+"")
			.replaceAll("</Equip.power.Skill.Lightning.prob/>", config.getInt("power.Skill.Lightning.prob")+"")
			.replaceAll("</Equip.power.Skill.Lightning.cool/>", config.getInt("power.Skill.Lightning.cool")+"")
			.replaceAll("</Equip.power.Skill.Teleport.distance/>", config.getInt("power.Skill.Teleport.distance")+"")
			.replaceAll("</Equip.power.Skill.Teleport.prob/>", config.getInt("power.Skill.Teleport.prob")+"")
			.replaceAll("</Equip.power.Skill.Teleport.cool/>", config.getInt("power.Skill.Teleport.cool")+"")
			.replaceAll("</Equip.power.Skill.Arrow.prob/>", config.getInt("power.Skill.Arrow.prob")+"")
			.replaceAll("</Equip.power.Skill.Arrow.cool/>", config.getInt("power.Skill.Arrow.cool")+"")
			.replaceAll("</Equip.power.Skill.Arrow.damage_w/>", config.getInt("power.Skill.Arrow.damage_w")+"");
			String cmd = null;
			for (String c : config.getStringList("power.Command_t")) {
				if (cmd==null) {
					cmd = c;
				}else {
					cmd+=("|"+c);
				}
			}
			str= str.replaceAll("</Equip.power.Command_t/>", cmd);
			cmd = null;
			for (String c : config.getStringList("power.Command_f")) {
				if (cmd==null) {
					cmd = c;
				}else {
					cmd+=("|"+c);
				}
			}
			str= str
			.replaceAll("</Equip.power.Command_f/>", cmd)
			.replaceAll("</Equip.limit.Level/>", config.getInt("limit.Level")+"")
			.replaceAll("</Equip.limit.Zhiye/>", Zhiye.getBy(config.getInt("limit.Level")).getName())
			.replaceAll("</Equip.limit.Type/>", config.getString("limit.type",""));
			String per = null;
			for (String c : config.getStringList("limit.Permission")) {
				if (cmd==null) {
					per = c;
				}else {
					per+=("|"+c);
				}
			}
			str= str.replaceAll("</Equip.limit.Permission/>", per);
		}
		str = str.replace('&', ChatColor.COLOR_CHAR);
		event.setSouce(str);
	}
}
