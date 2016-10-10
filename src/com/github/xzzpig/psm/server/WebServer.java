package com.github.xzzpig.psm.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.github.xzzpig.pigapi.PigData;
import com.github.xzzpig.pigapi.json.JSONObject;
import com.github.xzzpig.psm.Debuger;
import com.github.xzzpig.psm.Vars;

public class WebServer extends WebSocketServer {
	public static WebServer server;

	public static HashMap<WebSocket, PigData> datas = new HashMap<>();

	public WebServer(int port) {
		super(new InetSocketAddress(port));
		server = this;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		datas.put(conn, new PigData().set("ID", conn.getRemoteSocketAddress().getHostString()));
		System.out.println("[PSM]"+getID(conn) + "已连接");
		for(WebSocket c:WebServer.server.connections()){
			if(!WebServer.isLogin(c))
				continue;
			JSONObject ret = new JSONObject();
			ret.accumulate("command", "onlinePlayers");
			Player[] players = Bukkit.getOnlinePlayers();
			ret.accumulate("num", players.length);
			List<String> name = new ArrayList<String>();
			for(int i=0;i<players.length;i++){
				name.add(players[i].getName());
			}
			ret.accumulate("players",name.toArray(new String[0]));
			c.send(ret.toString());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("[PSM]"+getID(conn) + "已断开");
		datas.remove(conn);
		for(WebSocket c:WebServer.server.connections()){
			if(!WebServer.isLogin(c))
				continue;
			JSONObject ret = new JSONObject();
			ret.accumulate("command", "onlinePlayers");
			Player[] players = Bukkit.getOnlinePlayers();
			ret.accumulate("num", players.length);
			List<String> name = new ArrayList<String>();
			for(int i=0;i<players.length;i++){
				name.add(players[i].getName());
			}
			ret.accumulate("players",name.toArray(new String[0]));
			c.send(ret.toString());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onMessage(WebSocket conn, String message) {
		Debuger.print(conn.getRemoteSocketAddress().getHostName() + ":" + message);

		JSONObject json, ret = new JSONObject();
		try {
			json = new JSONObject(message);
		} catch (Exception e) {
			return;
		}
		String command = json.optString("command");
		switch (command) {
		case "isLogin":
			ret.accumulate("command", "loginResponse");
			ret.accumulate("login", datas.get(conn).getBoolean("Login"));
			conn.send(ret.toString());
			break;
		case "login":
			String id = json.optString("id"), pass = json.optString("pass","null");
			if (id.equalsIgnoreCase("")) {
				ret.accumulate("command", "loginResponse");
				ret.accumulate("login", false);
				ret.accumulate("reason", "ID不可为空");
				conn.send(ret.toString());
				return;
			} else if (!Vars.data.contianKey("user." + id)) {
				ret.accumulate("command", "loginResponse");
				ret.accumulate("login", false);
				ret.accumulate("reason", "无此ID");
				conn.send(ret.toString());
				return;
			}
			if(pass.equalsIgnoreCase(Vars.data.getString("user."+id+".pass")+"")){
				ret.accumulate("command", "loginResponse");
				ret.accumulate("login", true);
				datas.get(conn).set("Login", true);
				datas.get(conn).set("ID",id);
			} else {
				ret.accumulate("command", "loginResponse");
				ret.accumulate("login", false);
				ret.accumulate("reason", "密码错误");
				datas.get(conn).set("Login", false);
			}
			conn.send(ret.toString());
			break;
		case "command":
			if(!isLogin(conn))
				return;
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(),json.optString("cmd"));
			System.out.println("[PSM]"+datas.get(conn).getString("ID")+"执行了命令: /"+json.optString("cmd"));
			break;
		case "oldLog":
			if(!isLogin(conn))
				return;
			for (JSONObject j : Vars.log) {
				conn.send(j.toString());
			}
			break;
		case "onlinePlayers":
			if(!isLogin(conn))
				return;
			ret.accumulate("command", "onlinePlayers");
			Player[] players = Bukkit.getOnlinePlayers();
			ret.accumulate("num", players.length);
			List<String> name = new ArrayList<String>();
			for(int i=0;i<players.length;i++){
				name.add(players[i].getName());
			}
			ret.accumulate("players",name.toArray(new String[0]));
			conn.send(ret.toString());
			break;
		case "PSMList":
			if(!isLogin(conn))
				return;
			ret.accumulate("command", "PSMList");
			ret.accumulate("num", server.connections().size());
			List<String> psmlist = new ArrayList<String>();
			for(WebSocket socket:server.connections()){
				psmlist.add(getID(socket));
			}
			ret.accumulate("ids",psmlist.toArray(new String[0]));
			conn.send(ret.toString());
			break;
		default:
			break;
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		if(conn!=null)
		System.out.println(conn.getRemoteSocketAddress().getHostName() + "发生错误:" + ex);
		else
			System.out.println("WebSocket服务器发生错误:" + ex);
		
	}
	
	public static boolean isLogin(WebSocket ws){
		if(!datas.containsKey(ws))
			return false;
		return(datas.get(ws).getBoolean("Login"));
	}
	public static String getID(WebSocket ws){
		return(datas.get(ws).getString("ID"));
	}
}
