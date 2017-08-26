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
		File dataDir = new File(Main.getInstance().getDataFolder(), name);
		if (deleteDir(dataDir)) {
			areas.removeAll(areas.stream().filter(area -> area.name.equalsIgnoreCase(name)).collect(ArrayList::new,
					ArrayList::add, ArrayList::addAll));
			return true;
		}
		return false;
	}

	private static boolean deleteDir(File dir) {
		for (File sub : dir.listFiles()) {
			boolean deleted = true;
			if (sub.isDirectory())
				deleted = deleteDir(dir);
			else
				deleted = sub.delete();
			if (!deleted)
				return false;
		}
		return dir.delete();
	}

	public static synchronized Area getArea(String name) {
		Optional<Area> oarea = areas.stream().filter(area -> area.name.equalsIgnoreCase(name)).findAny();
		if (oarea.isPresent())
			return oarea.get();
		Area area = new Area(name);
		areas.add(area);
		Main.getInstance().getLogger().info("Area(" + name + ")已加载");
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
