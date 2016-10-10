package com.github.xzzpig.psm;

import java.io.File;
import java.util.LinkedList;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.xzzpig.pigapi.PigData;
import com.github.xzzpig.pigapi.json.JSONObject;

public class Vars {
	public static FileConfiguration config;
	public static boolean debug,console_only;
	public static File dataFile;
	public static PigData data;
	public static LinkedList<JSONObject> log;
	public static int max_log_size,port;
}
