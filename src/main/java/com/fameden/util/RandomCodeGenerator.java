package com.fameden.util;

import java.util.Random;

import com.fameden.common.constants.CommonConstants;

public class RandomCodeGenerator {

	static Random rnd = new Random();

	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(CommonConstants.AB.charAt(rnd.nextInt(CommonConstants.AB
					.length())));
		return sb.toString();
	}

}
