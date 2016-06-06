package com.github.xzzpig.batedamage;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xzzpig.batedamage.listener.BDListener;
import com.github.xzzpig.pigapi.TData;
import com.github.xzzpig.pigapi.bukkit.TCommandHelp;
import com.github.xzzpig.pigapi.bukkit.TConfig;

public class Main extends JavaPlugin {
	public static HashMap<Integer, List<String>> permissions = new HashMap<Integer, List<String>>();

	public static TData perdata = new TData();
	
	public static Class<AcountBeatCalculate> CalculateClass = null;

	public static long accountToBate(long account) {
		if(CalculateClass!=null){
			try {
				Object cal = CalculateClass.newInstance();
				if(cal instanceof AcountBeatCalculate){
					return ((AcountBeatCalculate) cal).accountToBate(account);					
				}
			} catch (InstantiationException | IllegalAccessException e) {
				Debuger.print(e);
			}			
		}
		long i = 0;
		if(account<=1023)
			while(get2x(i)-2<account)
				i++;
		else
			i= (account-1023)/1024+11;
		return i;
	}
	public static long beatToaccount(long bate){
		if(CalculateClass!=null){
			try {
				Object cal = CalculateClass.newInstance();
				if(cal instanceof AcountBeatCalculate){
					return ((AcountBeatCalculate) cal).beatToaccount(bate);					
				}
			} catch (InstantiationException | IllegalAccessException e) {
				Debuger.print(e);
			}			
		}
		if(bate<11)
			return get2x(bate)-2;
		else {
			return (bate-9)*1024-2;
		}
	}

	public static void buildPermisiom(Player player) {
		PermissionAttachment aper = (PermissionAttachment) (perdata
				.getObject(player.getName()));
		if (aper != null)
			aper.remove();
		long account = BDListener.data.getLong(player.getName());
		PermissionAttachment pers = player.addAttachment(Bukkit
				.getPluginManager().getPlugin("BateDamage"));
		for (long i = 1; i <= accountToBate(account); i++) {
			if (!permissions.containsKey(i))
				continue;
			for (String per : permissions.get(i)) {
				pers.setPermission(per, true);
			}
		}
		perdata.setObject(player.getName(), pers);
	}

	@SuppressWarnings("deprecation")
	public static void buildPermission() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			buildPermisiom(player);
		}
	}

	public static long get2x(long x) {
		long y = 1;
		for (long i = 0; i < x; i++) {
			y = 2 * y;
		}
		return y;
	}

	public static void removePermission() {
		for (Object per : perdata.getObjects().values()) {
			if (per instanceof PermissionAttachment)
				((PermissionAttachment) per).remove();
		}
		perdata.getObjects().clear();

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		String arg0 = "help";
		if (args.length > 0)
			arg0 = args[0];
		if (arg0.equalsIgnoreCase("help")) {
			for (TCommandHelp comm : Help.BD.getAllSubs()) {
				if (sender instanceof Player)
					comm.getHelpMessage(getName()).send((Player) sender);
				else
					sender.sendMessage(comm.getHelpMessage(getName())
							.toString());
			}
			return true;
		}
		if (!sender.hasPermission("batedamage.admin")) {
			sender.sendMessage("[BateDamage]你没有权限执行该命令");
			return true;
		}
		if (arg0.equalsIgnoreCase("set")) {
			long bate;
			try {
				bate = Integer.valueOf(args[1]);
			} catch (Exception e) {
				sender.sendMessage("[BateDamage]参数 [战力] 不可为空 或 非整数");
				return true;
			}
			String player;
			if (args.length > 2)
				player = (args[2]);
			else
				try {
					player = ((Player) sender).getName();
				} catch (Exception e) {
					sender.sendMessage("[BateDamage]控制台不可省略参数 <目标>");
					return true;
				}
			BDListener.data.set(player,beatToaccount(bate));
			TConfig.saveConfig("BateDamage", BDListener.data, "data.yml");
			sender.sendMessage("[BateDamage]" + player + "的战力已设置为" + bate);
			buildPermission();
			return true;
		} else if (arg0.equalsIgnoreCase("add")) {
			long bate;
			try {
				bate = Integer.valueOf(args[1]);
			} catch (Exception e) {
				sender.sendMessage("[BateDamage]参数 [战力] 不可为空 或 非整数");
				return true;
			}
			String player;
			if (args.length > 2)
				player = (args[2]);
			else
				try {
					player = ((Player) sender).getName();
				} catch (Exception e) {
					sender.sendMessage("[BateDamage]控制台不可省略参数 <目标>");
					return true;
				}
			BDListener.data
					.set(player,
							beatToaccount(accountToBate(BDListener.data.getLong(player))
									+ bate));
			TConfig.saveConfig("BateDamage", BDListener.data, "data.yml");
			sender.sendMessage("[BateDamage]" + player + "的战力已增加" + bate);
			buildPermission();
			return true;
		} else if (arg0.equalsIgnoreCase("remove")) {
			long bate;
			try {
				bate = Integer.valueOf(args[1]);
			} catch (Exception e) {
				sender.sendMessage("[BateDamage]参数 [战力] 不可为空 或 非整数");
				return true;
			}
			String player;
			if (args.length > 2)
				player = (args[2]);
			else
				try {
					player = ((Player) sender).getName();
				} catch (Exception e) {
					sender.sendMessage("[BateDamage]控制台不可省略参数 <目标>");
					return true;
				}
			long ac = beatToaccount(accountToBate(BDListener.data.getLong(player))
					- bate);
			if (ac < 0)
				ac = 0;
			BDListener.data.set(player, ac);
			TConfig.saveConfig("BateDamage", BDListener.data, "data.yml");
			sender.sendMessage("[BateDamage]" + player + "的战力已减少" + bate);
			buildPermission();
			return true;
		}
		else if (arg0.equalsIgnoreCase("get")) {
			String target = sender.getName();
			if(args.length>1)
				target = args[1];
			else if(!(sender instanceof Player)){
				sender.sendMessage("[BateDamage]控制台不可省略参数 <目标>");
				return true;
			}
			long account = BDListener.data.getLong(target);
			long bate = accountToBate(account);
			sender.sendMessage("[BateDamage]" + target + "的战力为" +bate+"(杀敌数:"+account+"|距下级:"+(beatToaccount(bate+1)-account)+")");
			return true;
		}
		return false;
	}

	// 插件停用函数
	@Override
	public void onDisable() {
		removePermission();
		getLogger().info(getName() + "插件已被停用");
	}

	@Override
	public void onEnable() {
		getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
		saveDefaultConfig();
		FileConfiguration config = TConfig.getConfigFile("BateDamage",
				"config.yml");
		try {
			for (String bate : TConfig.getConfigPath("BateDamage",
					"config.yml", "batedamage.permission")) {
				permissions.put(Integer.valueOf(bate),
						config.getStringList("batedamage.permission." + bate));
			}
		} catch (Exception e) {
		}
		getServer().getPluginManager().registerEvents(new BDListener(), this);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
			String alias, String[] args) {
		return Help.BD.getTabComplete(getName(), sender, command, alias, args);
	}
}
