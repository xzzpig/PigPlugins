package com.github.xzzpig.lord.shop;

import java.util.ArrayList;
import java.util.List;

import com.github.xzzpig.lord.Vars;

public class Shop {
	public static List<String> getShopList() {
		return new ArrayList<>(Vars.config.getConfigurationSection("Lord.shop").getKeys(false));
	}
	public int eid, price, exp, honor;

	public String name, permission;

	public Shop(String name) {
		this.name = name;
		eid = Vars.config.getInt("Lord.shop." + name + ".EID", 1);
		price = Vars.config.getInt("Lord.shop." + name + ".Price", 0);
		permission = Vars.config.getString("Lord.shop." + name + ".Permission");
		exp = Vars.config.getInt("Lord.shop." + name + ".Exp", 0);
		honor = Vars.config.getInt("Lord.shop." + name + ".Honor", 0);
	}
}
