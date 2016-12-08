package com.github.xzzpig.lord.player;

import java.util.Map;

public interface IPlayerInfo {
	public double getArmor_M();
	public double getArmor_P();
	public Map<String, Integer> getCritical();
	public double getDamage_M();
	public double getDamage_P();
	public double getEvasion();
	public double getHealth();
	public double getMaxMP();
	public int getLevel();
	public double getLifeSteal();
	public double getMaxHealth();
	public int getSpeed();
	public String getZhiye();
}