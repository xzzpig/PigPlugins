package com.github.xzzpig.pigzhiye;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;
import com.github.xzzpig.pigapi.bukkit.TCommandHelp.CommandInstance;
import com.github.xzzpig.pigapi.bukkit.TMessage;
import com.github.xzzpig.pigzhiye.zhiye.ZhiYe;

public class Help {
	public static TCommandHelp PigZhiye = new TCommandHelp("pigzhiye", "PigZhiye的主命令", "/pzy help");

	public static TCommandHelp pzy_reload = PigZhiye.addSubCommandHelp("reload", "重新加载职业配置文件", null, null)
			.setCommandRunner(Help::pzy_reload).setPermission("pigzhiye.command.reload");
	public static TCommandHelp pzy_saveall = PigZhiye.addSubCommandHelp("saveall", "立即保存职业配置文件", null, null)
			.setCommandRunner(Help::pzy_saveall).setPermission("pigzhiye.command.saveall");
	public static TCommandHelp pzy_list = PigZhiye.addSubCommandHelp("list", "列出所有职业", null, null)
			.setCommandRunner(Help::pzy_list).setPermission("pigzhiye.command.list");

	public static TCommandHelp pzy_manage = PigZhiye.addSubCommandHelp("manage", "对职业配置进行管理", "/pzy manage help", null);
	public static TCommandHelp pzy_manage_create = pzy_manage.addSubCommandHelp("create", "新建职业", null, "[name]")
			.setCommandRunner(Help::pzy_manage_create).addLimit(TCommandHelp.fixVar)
			.setPermission("pigzhiye.command.manage.create");
	public static TCommandHelp pzy_manage_delete = pzy_manage.addSubCommandHelp("delete", "删除职业", null, "[name]")
			.setCommandRunner(Help::pzy_manage_delete).addLimit(TCommandHelp.fixVar)
			.setPermission("pigzhiye.command.manage.delete");
	public static TCommandHelp pzy_manage_info = pzy_manage.addSubCommandHelp("info", "显示职业的详细信息", null, "[name]")
			.setCommandRunner(Help::pzy_manage_info).addLimit(TCommandHelp.fixVar)
			.setPermission("pigzhiye.command.manage.info");
	public static TCommandHelp pzy_manage_set = pzy_manage
			.addSubCommandHelp("set", "给职业配置添加属性", null, "[name] [key] [value]").setCommandRunner(Help::pzy_manage_set)
			.addLimit(TCommandHelp.fixVar).setPermission("pigzhiye.command.manage.set");
	public static TCommandHelp pzy_manage_remove = pzy_manage
			.addSubCommandHelp("remove", "给职业配置删除属性", null, "[name] [key]").setCommandRunner(Help::pzy_manage_remove)
			.addLimit(TCommandHelp.fixVar).setPermission("pigzhiye.command.manage.remove");

	public static boolean pzy_reload(CommandInstance ci) {
		ZhiYe.reloadAll();
		ci.sendMsg("职业的配置文件已重载");
		return true;
	}

	public static boolean pzy_saveall(CommandInstance ci) {
		ZhiYe.saveAll();
		ci.sendMsg("职业的配置文件已全部保存");
		return true;
	}

	public static boolean pzy_list(CommandInstance ci) {
		ci.sendMsg("职业列表:");
		ZhiYe.getStream().forEach(
				e -> new TMessage("-" + e.getKey()).tooltip(e.getValue().getData().toString()).send(ci.sender));
		return true;
	}

	public static boolean pzy_manage_create(CommandInstance ci) {
		String name = ci.args[2];
		ZhiYe zy = ZhiYe.getBy(name);
		if (zy.save())
			ci.sendMsg("&2" + zy + "的配置文件已创建");
		else
			ci.sendMsg("&4" + zy + "的配置文件创建失败");
		return true;
	}

	public static boolean pzy_manage_delete(CommandInstance ci) {
		String name = ci.args[2];
		ZhiYe zy = ZhiYe.getBy(name);
		if (zy.delete())
			ci.sendMsg("&2" + zy + "的配置文件已被删除");
		else
			ci.sendMsg("&4" + zy + "的配置文件已删除失败");
		return true;
	}

	public static boolean pzy_manage_info(CommandInstance ci) {
		String name = ci.args[2];
		ZhiYe zy = ZhiYe.getBy(name);
		ci.sendMsg(name + "的详细信息:");
		zy.getData().keySet().forEach(key -> ci.sender.sendMessage(" " + key + ":" + zy.getData().get(key)));
		return true;
	}

	public static boolean pzy_manage_set(CommandInstance ci) {
		String name = ci.args[2];
		String key = ci.args[3];
		String value = ci.args[4];
		ZhiYe zy = ZhiYe.getBy(name);
		Object v = value;
		try {
			v = Integer.valueOf(value);
		} catch (Exception e) {
			if (value.equalsIgnoreCase("true"))
				v = true;
			else if (value.equalsIgnoreCase("false"))
				v = false;
		}
		zy.getData().put(key, v);
		zy.save();
		ci.sendMsg("已将" + name + "的" + key + "设为" + v);
		return true;
	}

	public static boolean pzy_manage_remove(CommandInstance ci) {
		String name = ci.args[2];
		String key = ci.args[3];
		ZhiYe zy = ZhiYe.getBy(name);
		zy.getData().remove(key);
		zy.save();
		ci.sendMsg("已将" + name + "的" + key + "设为移除");
		return true;
	}
}
