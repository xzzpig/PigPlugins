package com.github.xzzpig.areaapi;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.bukkit.ChatColor;

public class TString {
	public static final String s = "§";

	public static String Color(int colorid) {
		return ChatColor.getByChar(colorid + "") + "";
	}

	public static String Color(String colorid) {
		return ChatColor.getByChar(colorid) + "";
	}

	public static String getRandomCH(int len) {
		String ret = "";
		for (int i = 0; i < len; i++) {
			String str = null;
			int hightPos, lowPos; // 定义高低位
			Random random = new Random();
			hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
			lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
			byte[] b = new byte[2];
			b[0] = (new Integer(hightPos).byteValue());
			b[1] = (new Integer(lowPos).byteValue());
			try {
				str = new String(b, "GBk"); // 转成中文
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
			ret += str;
		}
		return ret;
	}

	public static String Prefix(String prefix) {
		return ChatColor.GOLD + "[" + prefix + "]" + ChatColor.RESET;
	}

	public static String Prefix(String prefix, int colorid) {
		return ChatColor.GOLD + "[" + prefix + "]" + Color(colorid);
	}

	public static String Prefix(String prefix, String colorid) {
		return Color(6) + "[" + prefix + "]" + Color(colorid);
	}

	public static String removeColor(String str) {
		return ChatColor.stripColor(str);
	}

	public static String[] splitColor(String str) {
		String[] result = new String[] { "", "" };
		while (str.startsWith("§")) {
			result[0] = result[0] + str.substring(0, 2);
			str = str.substring(2, str.length());
		}
		result[1] = str;
		return result;
	}

	public static String sub(String s, String pre, String suf) {
		int f = s.indexOf(pre);
		int e = s.indexOf(suf, f);
		return s.substring(f + pre.length(), e);
	}

	public static String toUnicodeString(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				sb.append("\\u" + Integer.toHexString(c));
			}
		}
		return sb.toString();
	}

	private TString() {
	}
}
