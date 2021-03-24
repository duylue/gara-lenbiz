package com.lendbiz.gara.utils;

public class StringUtil {

	public static boolean isEmty(String input) {
		if (input == null || input.isBlank())
			return true;
		return false;
	}
}
