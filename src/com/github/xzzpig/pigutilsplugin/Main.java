package com.github.xzzpig.pigutilsplugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xzzpig.pigutils.plugin.Plugin;
import com.github.xzzpig.pigutils.plugin.PluginManager;
import com.github.xzzpig.pigutils.plugin.java.JavaPluginLoader;
import com.github.xzzpig.pigutils.plugin.script.ScriptPluginLoader;

public class Main extends JavaPlugin {

	public static Main instance;
	public PluginManager manager;

	public FileConfiguration config;
	public String loadMsg, unloadMsg;

	@Override
	public void onEnable() {
		instance = this;
		config = getConfig();
		loadMsg = config.getString("message.load", "%plugin% is loaded");
		unloadMsg = config.getString("message.unload", "%plugin% is unloaded");
		manager = new PluginManager() {
			@Override
			protected void nodiyOtherSuccess(Plugin p) {
				super.nodiyOtherSuccess(p);
				getLogger().info(loadMsg.replace("%plugin%", p.getName()));
			}

			@Override
			protected void nodifyOtherUnload(Plugin p) {
				super.nodifyOtherUnload(p);
				getLogger().info(unloadMsg.replace("%plugin%", p.getName()));
			}
		};
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		saveDefaultConfig();
		loadJavaPlugin();
		loadScriptPlugin();
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载完成");
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		for (String plugin : manager.listPlugins())
			manager.unloadPlugin(plugin);
		getLogger().info(getName() + "插件已被停用 ");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return Help.PigUtilsPlugin.runCommand(Help.PigUtilsPlugin.new CommandInstance(sender, command, label, args));
	}

	private void loadJavaPlugin() {
		ConfigurationSection javaPluginConfig = config.getConfigurationSection("JavaPlugin");
		if (!javaPluginConfig.getBoolean("enable", true))
			return;
		getLogger().info("开始加载JavaPlugin");
		manager.register(new JavaPluginLoader());
		File dir = new File(getDataFolder(), javaPluginConfig.getString("localDir"));
		if (!dir.exists())
			dir.mkdirs();
		for (File file : dir.listFiles()) {
			try {
				manager.loadPlugin(file);
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().warning("Plugin load failed|" + file);
			}
		}
	}

	private void loadScriptPlugin() {
		ConfigurationSection pluginConfig = config.getConfigurationSection("ScriptPlugin");
		if (!pluginConfig.getBoolean("enable", true))
			return;
		getLogger().info("开始加载ScriptPlugin");
		manager.register(new ScriptPluginLoader());
		if (this.getClassLoader() instanceof URLClassLoader)
			ScriptPluginLoader.Classloader4ScriptManager.addParents((URLClassLoader) this.getClassLoader());
		for (String str : pluginConfig.getStringList("classPath")) {
			try {
				ScriptPluginLoader.Classloader4ScriptManager.addURLs(new URL(str));
			} catch (Exception e) {
				try {
					File f = new File(str);
					if (f.exists())
						ScriptPluginLoader.Classloader4ScriptManager.addURLs(f.toURI().toURL());
				} catch (Exception e2) {
				}
			}
		}
		File dir = new File(getDataFolder(), pluginConfig.getString("localDir"));
		if (!dir.exists())
			dir.mkdirs();
		for (File file : dir.listFiles()) {
			try {
				manager.loadPlugin(file.toURI().toURL());
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().warning("Plugin load failed|" + file);
			}
		}
		List<String> urls = pluginConfig.getStringList("URLPlugin.urls");
		if (urls == null)
			return;
		for (String url : urls) {
			URL u;
			try {
				u = new URL(url);
				manager.loadPlugin(u);
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().warning("Plugin load failed|" + url);
			}
		}
	}
}