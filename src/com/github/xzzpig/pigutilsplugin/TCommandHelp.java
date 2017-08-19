package com.github.xzzpig.pigutilsplugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TCommandHelp {
	public class CommandInstance {
		public String[] args;
		public Command command;
		public String label;
		public CommandSender sender;

		public CommandInstance(CommandSender sender, Command command, String label, String[] args) {
			this.sender = sender;
			this.command = command;
			this.label = label;
			this.args = args;
		}

		public CommandInstance sendMsg(String msg) {
			msg = "&6[" + command.getName() + "]&r" + msg;
			msg = msg.replaceAll("&", ChatColor.COLOR_CHAR + "");
			sender.sendMessage(msg);
			return this;
		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer(label + "");
			for (String arg : args) {
				sb.append(" ").append(arg);
			}
			return sb.toString();
		}
	}

	public interface CommandLimit {
		/**
		 * @param ins
		 * @return 禁止原因,null为允许,否则禁止
		 */
		public TMessage canRun(CommandInstance ins);
	}

	@FunctionalInterface
	public interface CommandRunner {
		public boolean run(CommandInstance instance);
	}

	public static TCommandHelp valueOf(TCommandHelp basichelp, String command) {
		String[] cmds = command.split(" ");
		for (int i = cmds.length - 1; i >= 0; i--) {
			String cmd = cmds[0];
			for (int i2 = 1; i2 <= i; i2++) {
				cmd = cmd + " " + cmds[i2];
			}
			for (TCommandHelp ch : basichelp.getAllSubs())
				if (ch.toString().equalsIgnoreCase(cmd))
					return ch;
		}
		return basichelp;
	}

	private String command, describe, useage, var;
	private CommandRunner commandRunner;

	private List<CommandLimit> limits = new ArrayList<>();

	private List<TCommandHelp> subs = new ArrayList<TCommandHelp>();

	private TCommandHelp uphelp;

	public static final CommandLimit isPlayer = ci -> {
		if (ci.sender instanceof Player)
			return null;
		else {
			return new TMessage("[" + ci.label + "]").then("此命令").color(ChatColor.GRAY).style(ChatColor.UNDERLINE)
					.tooltip(ci.toString()).then("只能有玩家执行");
		}
	};

	public TCommandHelp(String command, String describe, String useage) {
		this.command = command;
		this.describe = describe;
		this.useage = useage;
	}

	private TCommandHelp(String command, String describe, String useage, String var, TCommandHelp uphelp) {
		if (command == null)
			command = "error";
		this.command = command;
		if (describe == null)
			describe = "无";
		this.describe = describe;
		if (useage == null)
			useage = "无";
		this.useage = useage;
		if (var == null)
			var = "";
		this.var = var;
		this.uphelp = uphelp;
	}

	public TCommandHelp addLimit(CommandLimit limit) {
		limits.add(limit);
		return this;
	}

	public TCommandHelp addSubCommandHelp(String command, String describe, String useage, String var) {
		TCommandHelp sub = new TCommandHelp(this.command + " " + command, describe, useage, var, this);
		subs.add(sub);
		return sub;
	}

	public List<TCommandHelp> getAllSubs() {
		List<TCommandHelp> sublist = new ArrayList<TCommandHelp>();
		for (TCommandHelp pre : this.subs) {
			sublist.add(pre);
			List<TCommandHelp> sub = pre.getAllSubs();
			if (sub != null) {
				for (TCommandHelp sub2 : sub) {
					if (!sublist.contains(sub2))
						sublist.add(sub2);
				}
			}
		}
		return sublist;
	}

	public String getCommand() {
		return command;
	}

	public String getDescribe() {
		return describe;
	}

	public TCommandHelp getFinalUpHelp() {
		TCommandHelp ch = this;
		while (ch.uphelp != null)
			ch = ch.uphelp;
		return ch;
	}

	public TMessage getHelpMessage(String pluginname) {
		TMessage help = new TMessage(TString.Prefix(pluginname, 3) + "/").tooltip("/" + pluginname + " help")
				.suggest("/" + pluginname + " help");
		String parts[] = this.toStrings();
		String com = "";
		TCommandHelp ch = null;
		for (String arg : parts) {
			com = com + arg;
			ch = TCommandHelp.valueOf(this.getFinalUpHelp(), com);
			help.then(arg).suggest("/" + com).tooltip(ChatColor.GREEN + ch.command + " " + var + "\n" + ChatColor.BLUE
					+ ch.describe + "\n" + ChatColor.GRAY + ch.useage).then(" ");
			com = com + " ";
		}
		if (ch != null)
			help.then(ch.getVar() + " ");
		help.then(ChatColor.BLUE + " -" + describe).tooltip(ChatColor.GRAY + useage)
				.then("  " + ChatColor.GREEN + "" + ChatColor.UNDERLINE + "点我").suggest("/" + command + " " + var)
				.tooltip("快速匹配命令\n" + "/" + command + " " + var);
		return help;
	}

	public TCommandHelp getSubCommandHelp(String command) {
		for (TCommandHelp c : subs) {
			if (command.equalsIgnoreCase(c.toString()))
				return c;
			if (c.toStrings()[c.toStrings().length - 1].equalsIgnoreCase(command))
				return c;
		}
		return this;
	}

	public TCommandHelp[] getSubCommandHelps() {
		return subs.toArray(new TCommandHelp[0]);
	}

	public List<String> getTabComplete(String pluginname, CommandSender sender, Command command, String alias,
			String[] args) {
		// Debuger.print(command.getName()+"|"+alias+"|"+Arrays.toString(args));
		List<String> tab = new ArrayList<String>();
		String cmd = command.getName();
		for (String arg : args) {
			cmd = cmd + " " + arg;
		}
		if (cmd.endsWith(" "))
			cmd = cmd.substring(0, cmd.length() - 1);
		for (TCommandHelp help : TCommandHelp.valueOf(this, cmd).getSubCommandHelps()) {
			tab.add(help.toStrings()[help.toStrings().length - 1]);
		}
		List<String> tab2 = new ArrayList<String>();
		for (String str : tab) {
			if (str.contains(args[args.length - 1])) {
				tab2.add(str);
			}
		}
		if (!tab2.isEmpty())
			tab = tab2;
		for (String str : tab)
			TCommandHelp.valueOf(this, cmd).getSubCommandHelp(str).getHelpMessage(pluginname).send(sender);
		if (tab.isEmpty())
			tab.add(TCommandHelp.valueOf(this, cmd).getVar());
		return tab;
	}

	public String getUseage() {
		return useage;
	}

	public String getVar() {
		return var;
	}

	public boolean runCommand(CommandInstance ci) {
		return this.getFinalUpHelp().runCommand(ci, 0);
	}

	private boolean runCommand(CommandInstance ci, int i) {
		TCommandHelp sub = this;
		try {
			sub = this.getSubCommandHelp(ci.args[i]);
		} catch (Exception e) {
		}
		if (sub == this) {
			for (CommandLimit commandLimit : limits) {
				TMessage msg = commandLimit.canRun(ci);
				if (msg != null) {
					msg.send(ci.sender);
					return true;
				}
			}
			if (commandRunner == null || commandRunner.run(ci) == false) {
				if (this.getSubCommandHelps().length != 0)
					for (TCommandHelp sub2 : this.getSubCommandHelps()) {
						sub2.getHelpMessage(ci.command.getLabel()).send(ci.sender);
					}
				else
					this.getHelpMessage(ci.command.getLabel()).send(ci.sender);
				return true;
			} else
				return true;
		} else {
			return sub.runCommand(ci, i + 1);
		}
	}

	public TCommandHelp setCommandRunner(CommandRunner r) {
		this.commandRunner = r;
		return this;
	}

	public TCommandHelp setPermission(String permission) {
		limits.add(ci -> {
			if (!ci.sender.hasPermission(permission)) {
				return new TMessage("[" + ci.label + "]").then("你").color(ChatColor.GREEN).style(ChatColor.UNDERLINE)
						.tooltip(ci.sender.getName()).then("没有该").color(ChatColor.RED).then("权限").color(ChatColor.BLUE)
						.style(ChatColor.UNDERLINE).tooltip(permission).then("执行").color(ChatColor.RED).then("此命令")
						.color(ChatColor.GRAY).style(ChatColor.UNDERLINE).tooltip(this.toDeepString());
			}
			return null;
		});
		return this;
	}

	public String toDeepString() {
		String str = this.command + " " + var;
		TCommandHelp up = this.uphelp;
		while (up != null && up != this) {
			str = up.command + " " + str;
			up = up.uphelp;
		}
		return str;
	}

	@Override
	public String toString() {
		return command;
	}

	public String[] toStrings() {
		return command.split(" ");
	}
}
