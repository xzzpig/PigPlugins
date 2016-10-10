package com.github.xzzpig.psm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.java_websocket.WebSocket;

import com.github.xzzpig.pigapi.TTime;
import com.github.xzzpig.pigapi.json.JSONObject;
import com.github.xzzpig.psm.server.WebServer;

public class LogAppender extends AbstractAppender {
	
	protected LogAppender() {
		super("PigServerManager", null, null);
		start();
	}

	@Override
	public void append(LogEvent arg0) {
		JSONObject json = new JSONObject();
		json.accumulate("command", "log");
		
		//替换第一个符合正则的数据
		//StringBuilder sb = new StringBuilder();
		String log = arg0.getMessage().getFormat().replace(new String(new byte[] { 27 }), "").replace("[m", "");
		Pattern pattern = Pattern.compile("(\\[)(\\d+)(;)(\\d+)(;)(\\d+)(m)");
		Matcher matcher = pattern.matcher(log);
		log = matcher.replaceAll("");
		//.replace("[0;33;22m", "").replace("[m", "").replace("[0;37;1m", "").replace("[0;35;1m","");
		/*
		while(log.length()>0){
			if(log.startsWith("[0;")){
				log = log.substring(log.indexOf("m")+1);
			}
			sb.append(log.charAt(0));
			log = log.substring(1);
		}
		log = sb.toString().replace("[0;31;1m","");
		 * */
		json.accumulate("log",log);
		json.accumulate("level", arg0.getLevel());
		json.accumulate("time", TTime.getHour() + ":" + TTime.getMinute() + ":" + TTime.getSecond());
		Vars.log.add(json);
		if(Vars.log.size()>Vars.max_log_size)
			Vars.log.poll();
		for (WebSocket client : WebServer.server.connections()) {
			try {
				if (WebServer.datas.get(client).getBoolean("Login")) {
					client.send(json.toString());
				}
			} catch (Exception e) {
			}
		}
	}
}