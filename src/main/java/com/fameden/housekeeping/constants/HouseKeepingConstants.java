package com.fameden.housekeeping.constants;

import com.fameden.common.exception.BreachingSingletonException;

public class HouseKeepingConstants {
	private HouseKeepingConstants() throws BreachingSingletonException {
		throw new BreachingSingletonException(
				HouseKeepingConstants.class.getName());
	}

	public static final String TNC_FILE_PROPERTY_NAME = "termsAndConditionsFile";
	public static final String TNC_FILE_PATH = "/WEB-INF/classes/com/fameden/files/TermsAndConditions.html";

	public static final String ABOUT_US_FILE_PROPERTY_NAME = "aboutUsFile";
	public static final String ABOUT_US_FILE_PATH = "/WEB-INF/classes/com/fameden/files/AboutUs.html";

	
	
	public static final String PUBLIC_RSA_KEY = "publicKey";
	public static final String PUBLIC_RSA_KEY_PATH = "/WEB-INF/classes/com/fameden/secureKeys/public.key";

	public static final String PRIVATE_RSA_KEY = "privateKey";
	public static final String PRIVATE_RSA_KEY_PATH = "/WEB-INF/classes/com/fameden/secureKeys/private.key";
}
