package com.github.xzzpig.pigzhiye.zhiye;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.github.xzzpig.pigapi.event.Event;
import com.github.xzzpig.pigapi.json.JSONException;
import com.github.xzzpig.pigapi.json.JSONObject;
import com.github.xzzpig.pigapi.json.JSONTokener;
import com.github.xzzpig.pigzhiye.Main;
import com.github.xzzpig.pigzhiye.zhiye.event.ZhiYeDeleteEvent;
import com.github.xzzpig.pigzhiye.zhiye.event.ZhiYeSaveEvent;

public class ZhiYe {

	private static Map<String, ZhiYe> map = new HashMap<>();

	private JSONObject data;

	private ZhiYe(JSONObject data) {
		this.data = data;
	}

	private ZhiYe(String name) {
		File datadir = new File(Main.self.getDataFolder(), "zhiye");
		if (!datadir.exists() || !datadir.isDirectory()) {
			datadir.mkdirs();
		}
		File file = new File(datadir, name + ".json");
		if (file.exists() && file.isFile())
			try {
				this.data = new JSONObject(new JSONTokener(new FileInputStream(file)));
			} catch (JSONException | FileNotFoundException e) {
				e.printStackTrace();
				this.data = new JSONObject();
			}
		else
			this.data = new JSONObject();
		this.data.put("name", name);
	}

	public JSONObject getData() {
		return data;
	}

	public boolean save() {
		File datadir = new File(Main.self.getDataFolder(), "zhiye");
		if (!datadir.exists() || !datadir.isDirectory()) {
			datadir.mkdirs();
		}
		File file = new File(datadir, data.getString("name") + ".json");
		ZhiYeSaveEvent event = new ZhiYeSaveEvent(this, file);
		Event.callEvent(event);
		if(event.isCanceled())
			return false;
		try {
			data.write(new FileWriter(file));
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean delete() {
		File datadir = new File(Main.self.getDataFolder(), "zhiye");
		if (!datadir.exists() || !datadir.isDirectory()) {
			datadir.mkdirs();
		}
		File file = new File(datadir, data.getString("name") + ".json");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		ZhiYeDeleteEvent event = new ZhiYeDeleteEvent(this, file);
		Event.callEvent(event);
		if(event.isCanceled())
			return false;
		if (file.delete()) {
			map.remove(getName());
			return true;
		} else
			return false;
	}

	public String getName() {
		return data.optString("name", null);
	}

	public static ZhiYe getBy(String name) {
		if (map.containsKey(name)) {
			map.get(name);
		}
		return map.put(name, new ZhiYe(name));
	}

	public static void saveAll() {
		map.values().stream().forEach(ZhiYe::save);
	}

	public static void reloadAll() {
		map = new HashMap<>();
		File dir = new File(Main.self.getDataFolder(), "zhiye");
		for (String fname : dir.list()) {
			ZhiYe.getBy(fname.replaceAll(".json", ""));
		}
	}
	
	public static Stream<Entry<String, ZhiYe>> getStream(){
		return map.entrySet().stream();
	}
}
