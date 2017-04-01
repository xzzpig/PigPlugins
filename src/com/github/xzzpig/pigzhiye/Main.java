package com.github.xzzpig.pigzhiye;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xzzpig.pigzhiye.zhiye.ZhiYe;

public class Main extends JavaPlugin {
	public static Main self;

	@Override
	public void onEnable() {
		Main.self = this;
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		saveDefaultConfig();
		loadZhiYe();
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		saveZhiYe();
		getLogger().info(getName() + "插件已被停用 ");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return Help.PigZhiye.runCommand(Help.PigZhiye.new CommandInstance(sender, command, label, args));
	}

	public void loadZhiYe() {
		File dir = new File(this.getDataFolder(), "zhiye");
		for (String fname : dir.list()) {
			ZhiYe.getBy(fname.replaceAll(".json", ""));
		}
	}

	private void saveZhiYe() {
		ZhiYe.saveAll();
	}
}
