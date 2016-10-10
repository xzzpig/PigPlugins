package com.github.xzzpig.psm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.java_websocket.WebSocket;

import com.github.xzzpig.pigapi.PigData;
import com.github.xzzpig.pigapi.bukkit.TCommandHelp;
import com.github.xzzpig.pigapi.bukkit.TConfig;
import com.github.xzzpig.psm.server.WebServer;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		saveDefaultConfig();
		getLogger().info("WebSocket服务器已运行");
		Vars.config = TConfig.getConfigFile(getName(),"config.yml");
		Vars.debug = Vars.config.getBoolean("PigServerManager.debug",false);
		Vars.console_only = Vars.config.getBoolean("PigServerManager.console_only",true);
		Vars.dataFile = new File(getDataFolder(),"data.pd");
		Vars.log = new LinkedList<>();
		Vars.max_log_size = Vars.config.getInt("PigServerManager.max_log_size",100);
		Vars.port = Vars.config.getInt("PigServerManager.port",10727);
		new WebServer(Vars.port).start();
		try {
			Vars.data = new PigData(Vars.dataFile);
		} catch (FileNotFoundException e) {
			Vars.data = new PigData();
			try {
				Vars.dataFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	    Logger logger = (Logger)LogManager.getRootLogger();
	    logger.addAppender(new LogAppender());
		getServer().getPluginManager().registerEvents(PSMListener.self,this);
	    //Debuger.print(getServer().getLogger().getFilter()+"");
		Debuger.print("data.pg内容:\n"+Vars.data.getPrintString());
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		getLogger().info(getName() + "插件已被停用 ");
		try {
			for(WebSocket client:WebServer.server.connections()){
				try {
					client.close();					
				} catch (Exception e) {
				}
			}
			WebServer.server.stop();
			getLogger().info("WebSocket服务器已被停止");
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().info("WebSocket服务器停止失败");
		} 
		Vars.data.saveToFile(Vars.dataFile);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String arg0 = "help";
		try {
			arg0 = args[0];
		} catch (Exception e) {}
		if (arg0.equalsIgnoreCase("help")) {
			if (arg0.equalsIgnoreCase("help")) {
				for(TCommandHelp sub:Help.PigServerManager.getSubCommandHelps()){
					sub.getHelpMessage("PSM").send(sender);
				}
				return true;
			}
		} else if (arg0.equalsIgnoreCase("addid")) {
			if(Vars.console_only && !(sender instanceof ConsoleCommandSender)){
				sender.sendMessage("[PSM]User只能在控制台创建");
				return true;
			}
			if(!sender.hasPermission("pigservermanager.admin")){
				sender.sendMessage("[PSM]你没权限执行该命令");
				return true;
			}
			String id,pass;
			try {
				id = args[1];
			} catch (Exception e) {
				sender.sendMessage("[PSM][ID]不能为空");
				return true;
			}
			try {
				pass = args[2];
			} catch (Exception e) {
				pass = "null";
			}
			if(Vars.data.contianKey("user."+id)){
				sender.sendMessage("[PSM]该ID已存在");
				return true;
			}
			Vars.data.set("user."+id+".pass", pass);
			Vars.data.saveToFile(Vars.dataFile);
			sender.sendMessage("你成功创建了新ID\nID:"+id+"\nPass:"+pass);
			return true;
		}else if (arg0.equalsIgnoreCase("psmlist")) {
			if(!sender.hasPermission("pigservermanager.admin")){
				sender.sendMessage("[PSM]你没权限执行该命令");
				return true;
			}
			StringBuffer stringBuffer =new StringBuffer("[PSM]连接的管理器列表:");
			for(WebSocket ws:WebServer.server.connections()){
				stringBuffer.append("\n  ").append(WebServer.getID(ws));
			}
			sender.sendMessage(stringBuffer.toString());
			return true;
		}
		return false;
	}
}

