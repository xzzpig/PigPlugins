package com.github.xzzpig.pigutilsplugin;

import com.github.xzzpig.pigutilsplugin.TCommandHelp.CommandInstance;

public class Help {
	public static TCommandHelp PigUtilsPlugin = new TCommandHelp("pigutilsplugin", "PigUtilsPlugin的主命令", "/pup help");
	public static TCommandHelp PigUtilsPlugin_Reload = PigUtilsPlugin
			.addSubCommandHelp("reload", "重载插件", null, "<plugin>").setCommandRunner(Help::reload)
			.setPermission("pigutilsplugin.reload");
	public static TCommandHelp PigUtilsPlugin_List = PigUtilsPlugin.addSubCommandHelp("list", "列出所有插件", null, null)
			.setCommandRunner(Help::list).setPermission("pigutilsplugin.list");

	public static boolean reload(CommandInstance cmd) {
		if (cmd.args.length < 2 || cmd.args[1].equals("*")) {
			Main.instance.onDisable();
			Main.instance.reloadConfig();
			Main.instance.onEnable();
			return true;
		}
		String plugin = cmd.args[1];
		Main.manager.reloadPlugin(plugin);
		return true;
	}

	public static boolean list(CommandInstance cmd) {
		cmd.sendMsg("插件列表:");
		for (String plugin : Main.manager.listPlugins()) {
			cmd.sender.sendMessage("  " + plugin);
		}
		return true;
	}
}
