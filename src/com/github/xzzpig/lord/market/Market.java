package com.github.xzzpig.lord.market;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.xzzpig.lord.Vars;
import com.github.xzzpig.pigapi.json.JSONArray;
import com.github.xzzpig.pigapi.json.JSONObject;

public class Market {
	public ItemStack item;
	public String id;
	public int itemid, price, exp;

	public ItemStack getMarketItem() {
		ItemStack is = new ItemStack(item);
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		lore.add("购买所需:");
		lore.add("  Price:" + price);
		lore.add("  Exp:" + price);
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	public static Market[] getMarkets() {
		JSONArray market_array = Vars.marketdata.getJSONArray("markets");
		Market[] markets = new Market[market_array.length()];
		for (int i = 0; i < markets.length; i++) {
			Market m = new Market();
			JSONObject mo = market_array.getJSONObject(i);
			m.id = mo.getString("id");
			m.exp = mo.getInt("exp");
			m.price = mo.getInt("price");
			m.itemid = mo.getInt("item");
			m.item = Vars.marketitemstacks.getItemStack(m.itemid + "");
			markets[i] = m;
		}
		return markets;
	}

	public static Inventory getMarketInventory(InventoryHolder holder, int page) {
		Market[] ms = getMarkets();
		Inventory inv = Bukkit.createInventory(holder, 6 * 9, "Lord Market|Page " + (page + 1));
		for (int i = 0; i < 5 * 9; i++) {
			int loc = page * 5 * 9 + i;
			if (loc > ms.length)
				break;
			inv.addItem(ms[loc].getMarketItem());
		}
		return inv;
	}
}
