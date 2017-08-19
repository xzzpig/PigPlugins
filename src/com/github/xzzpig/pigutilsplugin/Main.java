package com.github.xzzpig.pigutilsplugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.stream.Stream;

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
	public static PluginManager manager;

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
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		Stream.of(manager.listPlugins()).forEach(manager::unloadPlugin);
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
			manager.loadPlugin(file);
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
		File dir = new File(getDataFolder(), pluginConfig.getString("localDir"));
		if (!dir.exists())
			dir.mkdirs();
		for (File file : dir.listFiles()) {
			try {
				manager.loadPlugin(file.toURI().toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
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
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
}
