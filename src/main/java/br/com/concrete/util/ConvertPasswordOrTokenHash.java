package br.com.concrete.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class ConvertPasswordOrTokenHash {

	public static String convertPasswordOrTokenHash (String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		byte messageDigestByte[] = messageDigest.digest(password.getBytes("UTF-8"));
		 
		StringBuilder stringBuilderHex = new StringBuilder();
		for (byte b : messageDigestByte) {
		  stringBuilderHex.append(String.format("%02x", 0xFF & b));
		}
		String passwordOrTokenHash = stringBuilderHex.toString();
		
		return passwordOrTokenHash;
	}
	
	public static Boolean isEqualPasswordOrTokenHash (String passwordOrTokenBD, String passwordOrTokenRest) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		String b = convertPasswordOrTokenHash(passwordOrTokenRest);
		
		if (MessageDigest.isEqual(passwordOrTokenBD.getBytes(), b.getBytes())) {
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
}
