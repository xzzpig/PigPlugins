package com.github.xzzpig.multiserverchat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xzzpig.pigutils.event.EventHandler;
import com.github.xzzpig.pigutils.event.Listener;
import com.github.xzzpig.pigutils.file.ExtendFile;
import com.github.xzzpig.pigutils.json.JSONObject;
import com.github.xzzpig.pigutils.pack.Package;
import com.github.xzzpig.pigutils.pack.socket.eventdrive.EDPackageSocketClient;
import com.github.xzzpig.pigutils.pack.socket.eventdrive.PackageSocketCloseEvent;
import com.github.xzzpig.pigutils.pack.socket.eventdrive.PackageSocketOpenEvent;
import com.github.xzzpig.pigutils.pack.socket.eventdrive.PackageSocketPackageEvent;

public class Main extends JavaPlugin {

	FileConfiguration config;
	Thread thread;
	EDPackageSocketClient client;
	Timer timer = new Timer();

	private static class Config {
		static String pigmcnetcenter_ip;
		static int pigmcnetcenter_port;
		static int serverinfo_id;
		static String serverinfo_name;
		static String chatconfig_format;
		static Map<String, String> chatconfig_worldalias;
		static String authkey;
		static List<Integer> chatfilter_accept, chatfilter_deny;
	}

	public static Main instance;

	@Override
	public void onEnable() {
		instance = this;
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		saveDefaultConfig();
		initConfig();
		clientListener = new ClientListener();
		chatListener = new ChatListener();
		thread = new Thread(this::connectServer);
		thread.start();
		Bukkit.getPluginManager().registerEvents(chatListener, this);
	}

	public void connectServer() {
		getLogger().info("正在连接PigMCNetCenter(" + Config.pigmcnetcenter_ip + ":" + Config.pigmcnetcenter_port + ")");
		client = new EDPackageSocketClient(Config.pigmcnetcenter_ip, Config.pigmcnetcenter_port);
		client.regListener(clientListener);
		client.start();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (client.isStarted())
					return;
				getLogger().info("与PigMCNetCenter连接超时,将在10s后重试");
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						connectServer();
					}
				}, 10000);
			}
		}, 10000);
	}

	public class ClientListener implements Listener {
		@EventHandler
		public void onStart(PackageSocketOpenEvent event) {
			getLogger().info("PigMCNetCenter连接成功");
			JSONObject authdata = new JSONObject().put("authkey", Config.authkey).put("id", Config.serverinfo_id);
			System.err.println(authdata);
			client.send(new Package("MCChatClientAuthPackage", authdata.toString()));
		}

		@EventHandler
		public void onClose(PackageSocketCloseEvent event) {
			getLogger().info("与PigMCNetCenter断开连接,将在10s后重试");
			client = null;
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					connectServer();
				}
			}, 10000);
		}

		@SuppressWarnings("deprecation")
		@EventHandler
		public void onServerChatPackage(PackageSocketPackageEvent event) {
			if (!event.getPackage().getType().equals("ServerChatPackage"))
				return;
			JSONObject json = new JSONObject(event.getPackage().getStringData());
			if (!json.optString("type").equalsIgnoreCase("ChatMsg"))
				return;
			int serverID = json.getInt("serverID");
			String msg = json.getString("msg");
			if ((serverID == Config.serverinfo_id) || (Config.chatfilter_accept.contains(serverID))
					|| (Config.chatfilter_accept.contains(-1) && !Config.chatfilter_deny.contains(serverID))) {
				System.out.println(msg);
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (!shiledMap.containsKey(p.getName())
							|| (!shiledMap.get(p.getName()).contains(json.optString("player"))))
						p.sendMessage(msg);
				}
			}
		}

		@EventHandler
		public void onServerChatJoinPackage(PackageSocketPackageEvent event) {
			if (!event.getPackage().getType().equals("ServerChatPackage"))
				return;
			JSONObject json = new JSONObject(event.getPackage().getStringData());
			if (!json.optString("type").equalsIgnoreCase("PlayerJoinMulti"))
				return;
			String player = json.optString("player");
			mulitChatPlayers.add(player);
		}

		@EventHandler
		public void onServerChatExitPackage(PackageSocketPackageEvent event) {
			if (!event.getPackage().getType().equals("ServerChatPackage"))
				return;
			JSONObject json = new JSONObject(event.getPackage().getStringData());
			if (!json.optString("type").equalsIgnoreCase("PlayerExitMulti"))
				return;
			String player = json.optString("player");
			mulitChatPlayers.remove(player);
		}

		@EventHandler
		public void onServerChatShieldPackage(PackageSocketPackageEvent event) {
			if (!event.getPackage().getType().equals("ServerChatPackage"))
				return;
			JSONObject json = new JSONObject(event.getPackage().getStringData());
			if (!json.optString("type").equalsIgnoreCase("PlayerShiledPlayer"))
				return;
			String player = json.optString("player");
			String target = json.optString("target");
			if (!shiledMap.containsKey(player))
				shiledMap.put(player, new ArrayList<>());
			shiledMap.get(player).add(target);
		}

		@EventHandler
		public void onServerChatUnShieldPackage(PackageSocketPackageEvent event) {
			if (!event.getPackage().getType().equals("ServerChatPackage"))
				return;
			JSONObject json = new JSONObject(event.getPackage().getStringData());
			if (!json.optString("type").equalsIgnoreCase("PlayerUnShiledPlayer"))
				return;
			String player = json.optString("player");
			String target = json.optString("target");
			if (!shiledMap.containsKey(player))
				return;
			if (target.equals("*"))
				shiledMap.get(player).clear();
			else
				shiledMap.get(player).remove(target);
		}

		@EventHandler
		public void onServerChatMutePackage(PackageSocketPackageEvent event) {
			if (!event.getPackage().getType().equals("ServerChatPackage"))
				return;
			JSONObject json = new JSONObject(event.getPackage().getStringData());
			if (!json.optString("type").equalsIgnoreCase("PlayerMute"))
				return;
			String player = json.optString("player");
			long time = json.optLong("time");
			muteMap.put(player, time);
		}
	}

	public List<String> mulitChatPlayers = new ArrayList<>();
	public Map<String, List<String>> shiledMap = new HashMap<>();
	public Map<String, Long> muteMap = new HashMap<>();

	ChatListener chatListener;
	ClientListener clientListener;

	public class ChatListener implements org.bukkit.event.Listener {
		@org.bukkit.event.EventHandler(priority = EventPriority.HIGHEST)
		public void onChat(AsyncPlayerChatEvent event) {
			if (client == null || !client.isStarted())
				return;
			String player = event.getPlayer().getName();
			if (event.getMessage().equals("*")) {
				if (mulitChatPlayers.contains(player)) {
					exitPlayer(player);
					event.getPlayer().sendMessage("[" + getName() + "]你已退出跨服聊天频道,输入*重新进入");
					getLogger().info(player + "已退出跨服聊天频道");
				} else {
					joinPlayer(player);
					getLogger().info(player + "已进入跨服聊天频道");
					event.getPlayer().sendMessage("[" + getName() + "]你已进入跨服聊天频道,输入*退出");
				}
				event.setCancelled(true);
				return;
			}
			if (!mulitChatPlayers.contains(event.getPlayer().getName()))
				return;
			if (muteMap.containsKey(player)) {
				long time = muteMap.get(player);
				if (time == -1 || time > System.currentTimeMillis()) {
					if (time != -1)
						event.getPlayer().sendMessage("[MultiServerChat]你已被禁止跨服聊天至"
								+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time)));
					else {
						event.getPlayer().sendMessage("[MultiServerChat]你已被永久禁止跨服聊天(op可解禁)");
					}
					return;
				} else {
					muteMap.remove(player);
				}
			}
			event.setCancelled(true);
			JSONObject chatObj = new JSONObject();
			chatObj.put("serverID", Config.serverinfo_id);
			String msg = Config.chatconfig_format;
			msg = msg.replace("%Msg%", event.getMessage())
					.replace("%ColoredPlayer%", event.getPlayer().isOp() ? "&c%Player%&r" : "&2%Player%&r")
					.replace("%Player%", event.getPlayer().getName())
					.replace("%World%", Config.chatconfig_worldalias.get(event.getPlayer().getWorld().getName()))
					.replace("%ServerName%", Config.serverinfo_name).replace('&', ChatColor.COLOR_CHAR);
			chatObj.set("msg", msg);
			chatObj.set("type", "ChatMsg");
			chatObj.set("player", player);
			client.send(new Package("ServerChatPackage", chatObj.toString()));
		}
	}

	@SuppressWarnings("unchecked")
	private void initConfig() {
		config = this.getConfig();
		Config.pigmcnetcenter_ip = config.getString("mulitserverchat.pigmcnetcenter.ip", "localhost");
		Config.pigmcnetcenter_port = config.getInt("mulitserverchat.pigmcnetcenter.port", 10727);
		Config.serverinfo_id = config.getInt("mulitserverchat.serverinfo.id", 1);
		Config.serverinfo_name = config.getString("mulitserverchat.serverinfo.name", "First Server");
		Config.chatconfig_format = config.getString("mulitserverchat.chatconfig.format",
				"[%ServerName%] <[%World%] %Player%> %Msg%");
		Config.chatconfig_worldalias = new HashMap<String, String>();
		for (World w : getServer().getWorlds()) {
			Config.chatconfig_worldalias.put(w.getName(),
					config.getString("mulitserverchat.chatconfig.worldalias." + w.getName(), w.getName()));
		}
		Config.authkey = config.getString("mulitserverchat.authkey");
		Config.chatfilter_accept = config.getIntegerList("chatfilter.accept");
		Config.chatfilter_deny = config.getIntegerList("chatfilter.deny");
		ExtendFile muteFile = new ExtendFile(getDataFolder(), "mute.obj");
		if (muteFile.exists())
			muteMap = (Map<String, Long>) muteFile.readObject();
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		if (client != null) {
			client.unregListener(clientListener);
			client.stop();
		}
		ExtendFile muteFile = new ExtendFile(getDataFolder(), "mute.obj");
		muteFile.writeObject(muteMap);
		getLogger().info(getName() + "插件已被停用 ");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return Help.MultiServerChat.runCommand(Help.MultiServerChat.new CommandInstance(sender, command, label, args));
	}

	@SuppressWarnings("deprecation")
	public void joinPlayer(String player) {
		if (player.equalsIgnoreCase("*")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				joinPlayer(p.getName());
			}
			return;
		}
		mulitChatPlayers.add(player);
		JSONObject chatObj = new JSONObject();
		chatObj.put("serverID", Config.serverinfo_id);
		chatObj.set("type", "PlayerJoinMulti");
		chatObj.set("player", player);
		client.send(new Package("ServerChatPackage", chatObj.toString()));
	}

	@SuppressWarnings("deprecation")
	public void exitPlayer(String player) {
		if (player.equalsIgnoreCase("*")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				exitPlayer(p.getName());
			}
			return;
		}
		mulitChatPlayers.remove(player);
		JSONObject chatObj = new JSONObject();
		chatObj.put("serverID", Config.serverinfo_id);
		chatObj.set("type", "PlayerExitMulti");
		chatObj.set("player", player);
		client.send(new Package("ServerChatPackage", chatObj.toString()));
	}

	public void shiledPlayer(String player, String target) {
		JSONObject chatObj = new JSONObject();
		chatObj.set("type", "PlayerShiledPlayer");
		chatObj.set("player", player);
		chatObj.set("target", target);
		client.send(new Package("ServerChatPackage", chatObj.toString()));
	}

	public void unshiledPlayer(String player, String target) {
		JSONObject chatObj = new JSONObject();
		chatObj.set("type", "PlayerUnShiledPlayer");
		chatObj.set("player", player);
		chatObj.set("target", target);
		client.send(new Package("ServerChatPackage", chatObj.toString()));
	}

	public void mutePlayer(String player, long time) {
		JSONObject chatObj = new JSONObject();
		chatObj.set("type", "PlayerMute");
		chatObj.set("player", player);
		chatObj.set("time", time);
		client.send(new Package("ServerChatPackage", chatObj.toString()));
	}
}