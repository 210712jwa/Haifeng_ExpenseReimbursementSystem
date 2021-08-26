package com.revature.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
* @author HowToDoInJava
* @see https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
*/
public class PasswordHashing {
	private static PasswordHashing passwordHashing;
	private PasswordHashing() {

	}

	static {
		passwordHashing = new PasswordHashing();
	}

	public static PasswordHashing getInstance() {
		return passwordHashing;
	}
	

	public String getSecurePassword(String passwordToHash) {
		String generatedPassword = null;
		try {

			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(passwordToHash.getBytes());

			byte[] bytes = md.digest();

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	

	
	
}
