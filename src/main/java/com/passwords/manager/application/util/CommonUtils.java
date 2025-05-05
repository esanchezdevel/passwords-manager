package com.passwords.manager.application.util;

public class CommonUtils {

	public static boolean isEmpty(String text) {
		return text == null || text.length() == 0;
	}

	public static boolean isEmptyObject(Object obj) {
		return obj == null;
	}
}
