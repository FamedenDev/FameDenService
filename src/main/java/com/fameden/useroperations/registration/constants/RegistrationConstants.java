package com.fameden.useroperations.registration.constants;

import com.fameden.common.exception.BreachingSingletonException;

public final class RegistrationConstants {

	private RegistrationConstants() throws BreachingSingletonException {
		throw new BreachingSingletonException(RegistrationConstants.class.getName());
	}

	public static final String REQUEST_TYPE = "REGISTRATION";

	public static final String EMPTY_MOBILE_NUMBER = "Mobile Number provided is empty";
	public static final String EMPTY_FULL_NAME = "Full Name provided is empty";
	public static final String USER_OR_MOBILE_ALREADY_EXIST = "The email address/mobile number provided already exisits";
	public static final String EMAIL_VERIFICATION_SUBJECT = "Email Verification";
	public static final String EMAIL_VERIFICATION_TEMPLATE_FILE = "welcomeLoginEmail";
	public static final String EMAIL_VERIFICATION_GROUP = "welcomeloginemail group";
	
	public static final String EMAIL_TEMPLATE_URL_PLACE_HOLDER = "URL";
	
	public static final String VERIFICATION_PREFIX_URL = "http://localhost:8080/FameDenBackendWS/EmailAddressVerification?emailAddressVerificationCode=";

}
