package com.github.xzzpig.ciw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;
import com.github.xzzpig.pigapi.bukkit.TConfig;
import com.github.xzzpig.pigapi.json.JSONObject;
import com.github.xzzpig.pigapi.json.JSONTokener;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		saveDefaultConfig();
		Vars.configuration = TConfig.getConfigFile(getName(), "config.yml");
		Vars.cw = Vars.configuration.getStringList("ciw.enable_world");
		try {
			Vars.data = new File(getDataFolder(),"data.json");
			if(!Vars.data.exists()){
				Vars.data.createNewFile();
			}
			Vars.json = new JSONObject(new JSONTokener(new FileInputStream(Vars.data)));
		} catch (Exception e) {
			e.printStackTrace();
			Vars.json = new JSONObject();
		}
		
		getServer().getPluginManager().registerEvents(CIWListener.self,this);
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		getLogger().info(getName() + "插件已被停用 ");
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(Vars.data);
			outputStream.write(Vars.json.toString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String arg0 = "help";
		try {
			arg0 = args[0];
		} catch (Exception e) {
		}
		if (arg0.equalsIgnoreCase("help")) {
			if (arg0.equalsIgnoreCase("help")) {
				for (TCommandHelp sub : Help.CIW.getSubCommandHelps()) {
					sub.getHelpMessage("CIW").send(sender);
				}
				return true;
			}
		}
		return false;
	}
}