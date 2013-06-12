package com.kaosh.calllog;

public class Utils {
	
	public static String durationToString (int duration) {
		int s = duration;
		int m = s / 60;
		int h = m / 60;
		s %= 60;
		m %= 60;
		return String.format("%02d:%02d:%02d", h, m, s);
	}
	
	public static String costToString (float cost) {
		return String.format("$%.2f", cost);
	}
}
