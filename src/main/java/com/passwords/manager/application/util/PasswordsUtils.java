package com.passwords.manager.application.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordsUtils {
	
	public static String ofuscate(String text) {
		return text.replaceAll(".", "*");
	}

	public static String hashText(String text) {
		return BCrypt.hashpw(text, BCrypt.gensalt(12));
	}
}
