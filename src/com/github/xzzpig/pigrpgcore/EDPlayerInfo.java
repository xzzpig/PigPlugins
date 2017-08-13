package com.github.xzzpig.pigrpgcore;

import static com.github.xzzpig.pigutils.data.DataUtils.array2KVMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.xzzpig.pigutils.annoiation.NotNull;
import com.github.xzzpig.pigutils.annoiation.Nullable;
import com.github.xzzpig.pigutils.eddata.EventDriveData;

public class EDPlayerInfo {

	public static enum DamageType {
		Physical, Megical, Remote, Critical
	}

	private Player player;
	private EventDriveData edData;

	public EDPlayerInfo(@NotNull Player p) {
		this.player = p;
		edData = new EventDriveData(player);
	}

	public double getDamage(@Nullable Entity damagedEntity, @NotNull DamageType type) {
		return edData.get("Damage",
				array2KVMap(String.class, Object.class, "player", player, "entity", damagedEntity, "type", type),
				Double.class);
	}

	public double getPhysicalDamage(Entity damagedEntity) {
		return getDamage(damagedEntity, DamageType.Physical);
	}

	public double getMegicalDamage(Entity damagedEntity) {
		return getDamage(damagedEntity, DamageType.Megical);
	}

	public double getRemoteDamage(Entity damagedEntity) {
		return edData.get("Damage", array2KVMap(String.class, Object.class, "player", player, "entity", damagedEntity,
				"type", DamageType.Remote), Double.class);
	}

	public double getCriticalDamage(Entity damagedEntity) {
		return edData.get("Damage", array2KVMap(String.class, Object.class, "player", player, "entity", damagedEntity,
				"type", DamageType.Critical), Double.class);
	}

	public double getDefence(Entity damager, DamageType type) {
		return edData.get("Defence",
				array2KVMap(String.class, Object.class, "player", player, "entity", damager, "type", DamageType.Remote),
				Double.class);
	}

	public double getPhysicalDefence(Entity damager) {
		return getDefence(damager, DamageType.Physical);
	}

	public double getMegicalDefence(Entity damager) {
		return getDefence(damager, DamageType.Megical);
	}

	public double getRemoteDefence(Entity damager) {
		return getDefence(damager, DamageType.Remote);
	}

	public int getCriticalChance() {
		Integer chance = edData.get("CriticalChance", array2KVMap(String.class, Object.class, "player", player),
				Integer.class);
		if (chance == null)
			chance = 0;
		return chance < 0 ? 0 : chance > 100 ? 100 : chance;
	}

	public int getEvasionChance(DamageType type) {
		Integer chance = edData.get("EvasionChance",
				array2KVMap(String.class, Object.class, "player", player, "type", type), Integer.class);
		if (chance == null)
			chance = 0;
		return chance < 0 ? 0 : chance > 100 ? 100 : chance;
	}

	public boolean canUse(ItemStack item, String usage) {
		return edData.get("canUse",
				array2KVMap(String.class, Object.class, "item", item, "usage", usage, "player", player),
				Boolean.class) == Boolean.FALSE ? false : true;
	}

	public boolean canUse(ItemStack item) {
		boolean b = canUse(item, null);
		return b;
	}

	public static boolean trigger(int max, int chance) {
		return (Math.random() * max < chance);
	}
}
