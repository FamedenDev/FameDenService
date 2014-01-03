package com.fameden.useroperations.forgotcredential.constants;

import com.fameden.common.exception.BreachingSingletonException;

public class ForgotCredentialConstants {
	private ForgotCredentialConstants() throws BreachingSingletonException {
		throw new BreachingSingletonException(
				ForgotCredentialConstants.class.getName());
	}

	public static final String FORGOT_PASSWORD_REQUEST_TYPE = "FORGOTPASSWORD";
	public static final String UPDATE_PASSWORD_REQUEST_TYPE = "UPDATEPASSWORD";
		
	public static final String FORGOT_PASSWORD_SUBJECT = "Forgot Password";
	public static final String FORGOT_PASSWORD_TEMPLATE_FILE = "forgotPasswordEmail";

	public static final String FORGOT_PASSWORD_GROUP = "forgotPasswordEmail group";
	
	public static final String EMAIL_TEMPLATE_CODE_PLACE_HOLDER = "code";
	
	
}
