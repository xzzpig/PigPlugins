package com.github.xzzpig.lord.market;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.xzzpig.lord.Help;
import com.github.xzzpig.lord.Vars;
import com.github.xzzpig.pigapi.bukkit.TCommandHelp;
import com.github.xzzpig.pigapi.bukkit.TMessage;
import com.github.xzzpig.pigapi.json.JSONObject;

public class MarketCommand {

	/**
	 * /lord market
	 */
	@SuppressWarnings("deprecation")
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String arg1 = "help";
		try {
			arg1 = args[1];
		} catch (Exception e) {
		}
		if (arg1.equalsIgnoreCase("help")) {
			for (TCommandHelp sub : Help.Lord.getSubCommandHelp("market").getAllSubs()) {
				sub.getHelpMessage("Lord").send(sender);
			}
			return true;
		}
		Player player = null;
		if (!(sender instanceof Player)) {
			sender.sendMessage("[Lord]该命令只能由玩家执行");
			return true;
		} else {
			player = (Player) sender;
		}
		if (arg1.equalsIgnoreCase("open")) {
			if(!player.hasPermission("lord.default.command.market.open")){
				player.sendMessage(ChatColor.RED+"[Lord]你没有权限使用此命令");
				return true;
			}
			player.openInventory(Market.getMarketInventory(player, 0));
			return true;
		} else if (arg1.equalsIgnoreCase("create")) {
			if(!player.hasPermission("lord.default.command.market.open")){
				player.sendMessage(ChatColor.RED+"[Lord]你没有权限使用此命令");
				return true;
			}
			String arg2 = "0", arg3 = "0";
			try {
				arg2 = args[2];
				arg3 = args[3];
			} catch (Exception e) {
			}
			int price = 0, exp = 0;
			try {
				price = Integer.parseInt(arg2);
			} catch (Exception e) {
			}
			try {
				exp = Integer.parseInt(arg3);
			} catch (Exception e) {
			}
			int i = 0;
			while (true) {
				if (!Vars.marketitemstacks.getKeys(false).contains("" + i)) {
					break;
				}
			}
			ItemStack item = player.getItemInHand();
			if (item == null || item.getTypeId() == 0) {
				TMessage.getBy("[Lord]上架的物品不可为空", false).send(player);
				return true;
			}
			Vars.marketitemstacks.set(i + "", item);
			JSONObject market = new JSONObject();
			market.put("id", player.getName());
			market.put("item", i);
			market.put("price", price);
			market.put("exp", exp);
			Vars.marketdata.append("markets", market);
			
			return true;
		}
		return false;
	}
}
