package com.github.xzzpig.areaapi;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.github.xzzpig.pigutils.annoiation.ArraySize;
import com.github.xzzpig.pigutils.annoiation.NotNull;
import com.github.xzzpig.pigutils.json.JSONArray;
import com.github.xzzpig.pigutils.json.JSONObject;
import com.github.xzzpig.pigutils.json.JSONTokener;

public class Area {

	public static class Space {

		public static @ArraySize({ 3, 2 }) int[][] locs2xyz(@ArraySize(2) Location... locs) {
			int[][] xyz = new int[3][2];
			xyz[0] = sort(locs[0].getBlockX(), locs[1].getBlockX());
			xyz[1] = sort(locs[0].getBlockY(), locs[1].getBlockY());
			xyz[2] = sort(locs[0].getBlockZ(), locs[1].getBlockZ());
			return xyz;
		}

		private static @ArraySize(2) int[] sort(int i1, int i2) {
			int[] r = new int[2];
			if (i1 < i2) {
				r[0] = i1;
				r[1] = i2;
			} else {
				r[0] = i2;
				r[1] = i1;
			}
			return r;
		}

		String world;

		@ArraySize({ 3, 2 })
		int[][] xyz;

		public Space(JSONObject locJsonObject) {
			this.world = locJsonObject.getString("world");
			xyz = new int[3][2];
			xyz[0][0] = locJsonObject.getInt("x1");
			xyz[0][1] = locJsonObject.getInt("x2");
			xyz[1][0] = locJsonObject.getInt("y1");
			xyz[1][1] = locJsonObject.getInt("y2");
			xyz[2][0] = locJsonObject.getInt("z1");
			xyz[2][1] = locJsonObject.getInt("z2");
		}

		public Space(@ArraySize(2) Location... locs) {
			this.world = locs[0].getWorld().getName();
			xyz = locs2xyz(locs);
		}

		public boolean in(@NotNull Location loc) {
			return in(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		}

		public boolean in(@NotNull String world, @NotNull @ArraySize(3) int... xyz) {
			if (!world.equals(this.world))
				return false;
			if (xyz[0] < this.xyz[0][0])
				return false;
			if (xyz[1] < this.xyz[1][0])
				return false;
			if (xyz[2] < this.xyz[2][0])
				return false;
			if (xyz[0] > this.xyz[0][1])
				return false;
			if (xyz[1] > this.xyz[1][1])
				return false;
			if (xyz[2] > this.xyz[2][1])
				return false;
			return true;
		}

		/**
		 * @param space
		 * @return space是否在this中
		 */
		public boolean in(Space space) {
			Location[] locs = space.getLocations();
			if (!in(locs[0]) || !in(locs[1]))
				return false;
			return true;
		}

		public @ArraySize(2) Location[] getLocations() {
			Location[] locs = new Location[2];
			locs[0] = new Location(Bukkit.getWorld(world), xyz[0][0], xyz[1][0], xyz[2][0]);
			locs[1] = new Location(Bukkit.getWorld(world), xyz[0][1], xyz[1][1], xyz[2][1]);
			return locs;
		}

		public JSONObject toJSONObject() {
			JSONObject json = new JSONObject();
			json.put("world", world);
			json.put("x1", xyz[0][0]);
			json.put("x2", xyz[0][1]);
			json.put("y1", xyz[1][0]);
			json.put("y2", xyz[1][1]);
			json.put("z1", xyz[2][0]);
			json.put("z2", xyz[2][1]);
			return json;
		}

		public JSONObject toJSONObject2() {
			JSONObject json = new JSONObject();
			json.put("world", world);
			json.put("x", xyz[0][0]);
			json.put("y", xyz[1][0]);
			json.put("z", xyz[2][0]);
			json.put("a", xyz[0][1] - xyz[0][0]);
			json.put("b", xyz[1][1] - xyz[1][0]);
			json.put("c", xyz[2][1] - xyz[2][0]);
			return json;
		}

		@Override
		public String toString() {
			return toJSONObject2().toString();
		}

	}

	private JSONObject data;

	List<Space> excludeSpaceList;

	public final String name;

	List<Space> spaceList;

	Area(@NotNull String name) {
		if (name == null)
			throw new IllegalArgumentException(new NullPointerException("name can not be Null"));
		this.name = name;
		File dir = getDataDir();
		if (!dir.exists())
			dir.mkdirs();
		loadData();
	}

	public Space addExcludeSpace(@ArraySize(2) Location... locs) {
		if (locs.length != 2)
			throw new IllegalArgumentException("locs size should be 2");
		Space space = new Space(locs);
		if (excludeSpaceList.stream().noneMatch(s -> s.in(space)))
			excludeSpaceList.add(space);
		return space;
	}

	public Space addSpace(@ArraySize(2) Location... locs) {
		if (locs.length != 2)
			throw new IllegalArgumentException("locs size should be 2");
		Space space = new Space(locs);
		if (spaceList.stream().noneMatch(s -> s.in(space)))
			spaceList.add(space);
		return space;
	}

	public JSONObject getData() {
		try {
			saveData();
		} catch (IOException e) {
		}
		loadData();
		return data;
	}

	public File getDataDir() {
		return new File(Main.getInstance().getDataFolder(), name);
	}

	File getDataFile() {
		return new File(getDataDir(), "data.json");
	}

	public boolean in(Location loc) {
		if (excludeSpaceList.stream().anyMatch(s -> s.in(loc)))
			return false;
		if (spaceList.stream().allMatch(s -> s.in(loc)))
			return true;
		return false;
	}

	public void loadData() {
		File dataFile = getDataFile();
		if (!dataFile.exists())
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		FileReader reader = null;
		try {
			reader = new FileReader(dataFile);
			data = new JSONObject(new JSONTokener(reader));
		} catch (Exception e) {
			data = new JSONObject();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		loadSpaceList();
		loadExcludeSpaceList();
	}

	private void loadExcludeSpaceList() {
		if (excludeSpaceList == null)
			excludeSpaceList = new ArrayList<>();
		JSONArray spaces = data.optJSONArray("excludes");
		if (spaces == null)
			return;
		for (int i = 0; i < spaces.length(); i++)
			excludeSpaceList.add(new Space(spaces.getJSONObject(i)));
	}

	private void loadSpaceList() {
		if (spaceList == null)
			spaceList = new ArrayList<>();
		JSONArray spaces = data.optJSONArray("spaces");
		if (spaces == null)
			return;
		for (int i = 0; i < spaces.length(); i++)
			spaceList.add(new Space(spaces.getJSONObject(i)));
	}

	public void saveData() throws IOException {
		saveSpaceList();
		saveExcludeSpaceList();
		data.saveToFile(getDataFile());
	}

	private void saveExcludeSpaceList() {
		JSONArray spaces = new JSONArray();
		excludeSpaceList.stream().map(Space::toJSONObject).forEach(spaces::put);
		data.put("excludes", spaces);
	}

	private void saveSpaceList() {
		JSONArray spaces = new JSONArray();
		spaceList.stream().map(Space::toJSONObject).forEach(spaces::put);
		data.put("spaces", spaces);
	}
}
