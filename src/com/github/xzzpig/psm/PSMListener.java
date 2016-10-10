package com.github.xzzpig.psm;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.java_websocket.WebSocket;

import com.github.xzzpig.pigapi.json.JSONObject;
import com.github.xzzpig.psm.server.WebServer;

public class PSMListener implements Listener{
	public static PSMListener self = new PSMListener();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent event){
		for(WebSocket conn:WebServer.server.connections()){
			if(!WebServer.isLogin(conn))
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
			conn.send(ret.toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerLogoff(PlayerQuitEvent event){
		for(WebSocket conn:WebServer.server.connections()){
			if(!WebServer.isLogin(conn))
				continue;
			JSONObject ret = new JSONObject();
			ret.accumulate("command", "onlinePlayers");
			Player[] players = Bukkit.getOnlinePlayers();
			ret.accumulate("num", players.length-1);
			List<String> name = new ArrayList<String>();
			for(int i=0;i<players.length;i++){
				name.add(players[i].getName());
			}
			name.remove(event.getPlayer().getName());
			ret.accumulate("players",name.toArray(new String[0]));
			conn.send(ret.toString());
		}
	}
}
