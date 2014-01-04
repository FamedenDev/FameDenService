package com.fameden.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fameden.common.constants.CommonConstants;

/**
 * 
 * @author Ravjot
 */
public class CommonValidations {

	public static boolean isStringEmpty(String data) {
		boolean result = false;
		if (data == null || data.trim().length() == 0) {
			result = true;
		}
		return result;
	}

	public static boolean isValidEmailAddress(String emailAddress) {
		boolean result = false;

		if (!isStringEmpty(emailAddress)) {

			Pattern pattern = Pattern.compile(CommonConstants.EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(emailAddress);
			result = matcher.matches();
		}

		return result;
	}
}
