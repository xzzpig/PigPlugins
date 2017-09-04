package com.github.xzzpig.areaapi;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Location;

import com.github.xzzpig.areaapi.event.AreaDeleteEvent;
import com.github.xzzpig.areaapi.event.AreaLoadEvent;
import com.github.xzzpig.pigutils.event.Event;

public class AreaAPI {
	static final List<Area> areas = new LinkedList<>();

	static final Map<String, Location> playerSelectedLocMap = new HashMap<>();

	public static int selectTool;

	public static synchronized Stream<String> allAreaStream() {
		return Arrays.asList(Main.getInstance().getDataFolder().listFiles()).stream()
				.map(file -> new File(file, "data.json")).filter(File::exists).map(File::getParentFile)
				.map(File::getName);
	}

	public static boolean deleteArea(Area area) {
		return deleteArea(area.name);
	}

	public static boolean deleteArea(String name) {
		AreaDeleteEvent event = new AreaDeleteEvent(getArea(name));
		Event.callEvent(event);
		if (event.isCanceled()) {
			Main.getInstance().getLogger().info("Area(" + name + ")删除失败(被AreaDeleteEvent取消)");
			return false;
		}
		File dataDir = new File(Main.getInstance().getDataFolder(), name);
		if (deleteDir(dataDir)) {
			areas.removeAll(areas.stream().filter(area -> area.name.equalsIgnoreCase(name)).collect(ArrayList::new,
					ArrayList::add, ArrayList::addAll));
			Main.getInstance().getLogger().info("Area(" + name + ")删除成功");
			return true;
		}
		Main.getInstance().getLogger().info("Area(" + name + ")删除失败");
		return false;
	}

	private static boolean deleteDir(File dir) {
		for (File sub : dir.listFiles()) {
			boolean deleted = true;
			if (sub.isDirectory())
				deleted = deleteDir(dir);
			else
				deleted = sub.delete();
			if (!deleted) {
				Main.getInstance().getLogger().warning("删除Area时文件" + sub.getAbsolutePath() + "删除失败");
				return false;
			}
		}
		return dir.delete();
	}

	public static synchronized Area getArea(String name) {
		Optional<Area> oarea = areas.stream().filter(area -> area.name.equalsIgnoreCase(name)).findAny();
		if (oarea.isPresent())
			return oarea.get();
		Main.getInstance().getLogger().info("开始加载Area(" + name + ")");
		Area area = new Area(name);
		AreaLoadEvent event = new AreaLoadEvent(area);
		Event.callEvent(event);
		if (!event.isCanceled()) {
			areas.add(area);
			Main.getInstance().getLogger().info("Area(" + name + ")已加载");
		}
		return area;
	}

	public static List<Area> getLoadedAreas() {
		return areas;
	}

	public static Location getPlayerSelectedLoc(String player, int id) {
		if (!playerSelectedLocMap.containsKey(player + "_" + id))
			return null;
		return playerSelectedLocMap.get(player + "_" + id);
	}

	public static synchronized boolean isAreaExist(String name) {
		File areadDataFile = new File(Main.getInstance().getDataFolder(), name);
		if (!areadDataFile.exists())
			return false;
		return new File(areadDataFile, "data.json").exists();
	}

	public static synchronized String[] listAllArea() {
		return allAreaStream().toArray(String[]::new);
	}

	public static void setPlayerSelectedLoc(String player, int id, Location loc) {
		playerSelectedLocMap.put(player + "_" + id, loc);
	}

	public static void reload(String area) {
		if (area == null || area.equals(""))
			Main.getInstance().reload();
		else {
			AreaAPI.areas.removeIf(a -> a.name.equalsIgnoreCase(area));
			AreaAPI.getArea(area);
		}
	}
}
