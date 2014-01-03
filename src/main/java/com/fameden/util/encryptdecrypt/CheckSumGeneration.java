package com.fameden.util.encryptdecrypt;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CheckSumGeneration {

	@SuppressWarnings("resource")
	public static String generateChecksumOfFile(String filePath) {
		String checksum = null;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA1");

			FileInputStream fis = new FileInputStream(filePath);
			byte[] dataBytes = new byte[1024];

			int nread = 0;

			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			;

			byte[] mdbytes = md.digest();
			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			checksum = sb.toString();
		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}

		return checksum;
	}
}
