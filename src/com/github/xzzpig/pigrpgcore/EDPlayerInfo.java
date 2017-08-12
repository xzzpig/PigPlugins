package com.github.xzzpig.pigrpgcore;

import static com.github.xzzpig.pigutils.data.DataUtils.array2KVMap;
import static com.github.xzzpig.pigutils.reflect.ClassUtils.checkThisConstructorArgs;

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
		checkThisConstructorArgs(p);
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
		int chance = edData.get("CriticalChance", Integer.class);
		return chance < 0 ? 0 : chance > 100 ? 100 : chance;
	}

	public boolean canUse(ItemStack item) {
		return edData.get("canUse", array2KVMap(String.class, Object.class, "item", item), Boolean.class);
	}
}
