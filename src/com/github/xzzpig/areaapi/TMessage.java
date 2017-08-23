package com.github.xzzpig.areaapi;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.FancyMessage;

public class TMessage {

	static class MessagePart {

		public String clickActionData = null;
		public String clickActionName = null;
		public ChatColor color = null;
		public String hoverActionData = null;
		public String hoverActionName = null;
		public ChatColor[] styles = null;
		public final String text;

		public MessagePart(final String text) {
			this.text = text;
		}

		public JsonWriter writeJson(final JsonWriter json) throws IOException {
			json.beginObject().name("text").value(text);
			if (color != null) {
				json.name("color").value(color.name().toLowerCase());
			}
			if (styles != null) {
				for (final ChatColor style : styles) {
					json.name(style == ChatColor.UNDERLINE ? "underlined" : style.name().toLowerCase()).value(true);
				}
			}
			if (clickActionName != null && clickActionData != null) {
				json.name("clickEvent").beginObject().name("action").value(clickActionName).name("value")
						.value(clickActionData).endObject();
			}
			if (hoverActionName != null && hoverActionData != null) {
				json.name("hoverEvent").beginObject().name("action").value(hoverActionName).name("value")
						.value(hoverActionData).endObject();
			}
			return json.endObject();
		}

	}

	private static Plugin hd;

	private static String version = null;

	static {
		for (int a = 1; a <= 8; a++) {
			for (int b = 1; b <= 9; b++) {
				String testversion = "v1_" + a + "_R" + b;
				try {
					Class.forName("org.bukkit.craftbukkit." + testversion + ".entity.CraftPlayer");
					version = testversion;
					System.out.println("MC版本为" + version + ",FM将以TellRaw方式发送");
					break;
				} catch (Exception e) {
				}
			}
		}
		if (version == null)
			System.out.println("版本获取失败,FM将以普通方式发送");
	}

	@SuppressWarnings("deprecation")
	public static TMessage getBy(ItemStack is) {
		String name = is.getItemMeta().getDisplayName();
		if (name == null)
			name = is.getType().name() + "(" + is.getType().getId() + ")";
		TMessage fm = new TMessage(ChatColor.GOLD + "  " + name);
		ItemMeta im = is.getItemMeta();
		String tip = ChatColor.RESET + "  物品名称:" + im.getDisplayName() + "\n" + ChatColor.GRAY + "  类型:" + is.getType()
				+ "(" + is.getTypeId() + ")" + "\n" + ChatColor.GREEN + "  数量:" + is.getAmount() + "\n"
				+ ChatColor.YELLOW + "  LORE:";
		if (im.getLore() != null)
			for (String lore : im.getLore())
				tip = tip + "\n    " + ChatColor.RESET + lore;
		else
			tip = tip + ChatColor.RED + "无";
		tip = tip + "\n" + ChatColor.YELLOW + "  附魔:";
		Iterator<Entry<Enchantment, Integer>> ir = is.getEnchantments().entrySet().iterator();
		while (ir.hasNext()) {
			Entry<Enchantment, Integer> e = ir.next();
			tip = tip + "\n    " + ChatColor.BLUE + e.getKey().getName() + ":" + e.getValue();
		}
		if (is.getEnchantments().isEmpty())
			tip = tip + ChatColor.RED + "无";
		fm.tooltip(tip);
		return fm;
	}

	public static TMessage getBy(String str, boolean highlight) {
		return getBy(new TMessage(""), str, highlight);
	}

	public static TMessage getBy(TMessage fm, String str, boolean highlight) {
		str = str.replaceAll("&", "§").replace('^', '醃');
		String[] strs = str.split("醃");
		if (strs.length == 1) {
			return fm.then(str);
		}
		for (int i = 0; i < strs.length; i++) {
			String sub = strs[i];
			if (i % 2 == 0) {
				fm.then(sub);
			} else {
				String[] subs = sub.split("#");
				StringBuilder tooltip = new StringBuilder(subs[0]);
				for (int j = 1; j < subs.length; j++) {
					String sub2 = subs[j];
					if (sub2.startsWith("s"))
						fm.suggest(sub2.substring(1).startsWith("/") ? sub2.substring(1) : "/" + sub2.substring(1));
					else if (sub2.startsWith("c"))
						fm.command(sub2.substring(1).startsWith("/") ? sub2.substring(1) : "/" + sub2.substring(1));
					else if (sub2.startsWith("f"))
						fm.file(sub2.substring(1));
					else if (sub2.startsWith("l"))
						fm.link(sub2.substring(1));
					else
						tooltip.append("#" + sub2);
					if (highlight)
						fm.style(ChatColor.UNDERLINE);
				}
				fm.tooltip(tooltip.toString());
			}
		}
		// boolean istip = false;
		// int i = 1;
		// for (String s : strs) {
		// if (s.equalsIgnoreCase("")) {
		// }
		// if (s.contains("#s")) {
		// fm.suggest(s.split("#s")[1]);
		// s = s.replaceAll("#s" + s.split("#s")[1], "");
		// }
		// if (s.contains("#c")) {
		// fm.command(s.split("#c")[1]);
		// s = s.replaceAll("#c" + s.split("#c")[1], "");
		// }
		// if (!istip) {
		// if (highlight && i != strs.length && (!strs[i].equalsIgnoreCase("")))
		// s = "§l§n" + rePlaceColor(s) + ChatColor.RESET;
		// fm.then(s);
		// istip = true;
		// i++;
		// } else {
		// fm.tooltip(s);
		// istip = false;
		// i++;
		// }
		// }
		return fm;
	}

	private FancyMessage fm;

	private List<MessagePart> messageParts;

	public TMessage() {
		this("");
	}

	public TMessage(String firstPartText) {
		messageParts = new ArrayList<MessagePart>();
		messageParts.add(new MessagePart(firstPartText));
		if (hd != null) {
			fm = HolographicDisplays.getNMSManager().newFancyMessage(firstPartText);
		}
	}

	public TMessage color(ChatColor color) {
		if (!color.isColor()) {
			throw new IllegalArgumentException(color.name() + " is not a color");
		}
		latest().color = color;
		if (fm != null)
			fm.color(color);
		return this;
	}

	public TMessage command(String command) {
		onClick("run_command", command);
		if (fm != null)
			fm.command(command);
		return this;
	}

	public TMessage file(String path) {
		onClick("open_file", path);
		if (fm != null)
			fm.file(path);
		return this;
	}

	private MessagePart latest() {
		return messageParts.get(messageParts.size() - 1);
	}

	public TMessage link(String url) {
		onClick("open_url", url);
		if (fm != null)
			fm.link(url);
		return this;
	}

	private void onClick(String name, String data) {
		MessagePart latest = latest();
		latest.clickActionName = name;
		latest.clickActionData = data;
	}

	private void onHover(String name, String data) {
		MessagePart latest = latest();
		latest.hoverActionName = name;
		latest.hoverActionData = data;
	}

	public void send(CommandSender sender) {
		if (sender instanceof Player)
			send((Player) sender);
		else {
			sender.sendMessage(toString());
		}
	}

	public void send(Player player) {
		if (version == null) {
			player.sendMessage(this.toString());
			return;
		}
		try {
			Object EntityPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer")
					.getMethod("getHandle").invoke(player);
			Object playerConnection = EntityPlayer.getClass().getField("playerConnection").get(EntityPlayer);
			Object IChatBaseComponent = Class.forName("net.minecraft.server." + version + ".ChatSerializer")
					.getMethod("a", String.class).invoke(null, toJSONString());
			Class<?> cls = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
			Class<?>[] paramTypes = { Class.forName("net.minecraft.server." + version + ".IChatBaseComponent") };
			Object[] params = { IChatBaseComponent }; // 方法传入的参数

			Constructor<?> con = cls.getConstructor(paramTypes); // 主要就是这句了
			Object PacketPlayOutChat = con.newInstance(params); // BatcherBase
																// 为自定义类
			Method sendPack = null;
			for (Method meth : playerConnection.getClass().getMethods()) {
				if (meth.getName().equalsIgnoreCase("sendPacket"))
					sendPack = meth;
			}
			sendPack.invoke(playerConnection, PacketPlayOutChat);
		} catch (Exception e) {
			// if (hd == null) {
			// hd = Bukkit.getPluginManager().getPlugin("HolographicDisplays");
			// if (hd != null)
			// Main.self.getLogger().info("尝试使用HolographicDisplays插件发送TM");
			// }
			// if (hd != null) {
			// if (fm != null) {
			// fm.send(player);
			// return;
			// }
			// fm = HolographicDisplays.getNMSManager().newFancyMessage("");
			// List<FancyMessage.MessagePart> ls = new ArrayList<>();
			// for (MessagePart mpt : messageParts) {
			// FancyMessage.MessagePart mp = new
			// FancyMessage.MessagePart(mpt.text);
			// mp.clickActionData = mpt.clickActionData;
			// mp.clickActionName = mpt.clickActionName;
			// mp.color = mpt.color;
			// mp.hoverActionData = mpt.hoverActionData;
			// mp.hoverActionName = mpt.clickActionName;
			// mp.styles = mpt.styles;
			// ls.add(mp);
			// }
			//
			// try {
			// Field f = fm.getClass().getDeclaredField("messageParts");
			// f.setAccessible(true);
			// f.set(fm, ls);
			// fm.send(player);
			// return;
			// } catch (NoSuchFieldException | SecurityException |
			// IllegalArgumentException
			// | IllegalAccessException e1) {
			// e1.printStackTrace();
			// }
			// }
			e.printStackTrace();
			version = null;
			System.err.println("FM发送错误,将以普通方式发送");
			send(player);
		}
	}

	public TMessage style(ChatColor... styles) {
		for (ChatColor style : styles) {
			if (!style.isFormat()) {
				this.color(style);
			}
		}
		latest().styles = styles;
		if (fm != null)
			fm.style(styles);
		return this;
	}

	public TMessage suggest(String command) {
		onClick("suggest_command", command);
		if (fm != null)
			fm.suggest(command);
		return this;
	}

	public TMessage then(Object obj) {
		messageParts.add(new MessagePart(obj.toString()));
		if (fm != null)
			fm.then(obj);
		return this;
	}

	public String toJSONString() {
		StringWriter stringWriter = new StringWriter();
		JsonWriter json = new JsonWriter(stringWriter);

		try {
			if (messageParts.size() == 1) {
				latest().writeJson(json);
			} else {
				json.beginObject().name("text").value("").name("extra").beginArray();
				for (MessagePart part : messageParts) {
					part.writeJson(json);
				}
				json.endArray().endObject();
			}

		} catch (IOException e) {
			throw new RuntimeException("invalid message");
		}
		return stringWriter.toString();
	}

	public TMessage tooltip(String text) {
		onHover("show_text", text);
		if (fm != null)
			fm.tooltip(text);
		return this;
	}

	@Override
	public String toString() {
		String message = "";
		for (MessagePart mp : messageParts)
			message = message + ChatColor.RESET + mp.text;
		return message;
	}
}
