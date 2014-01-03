package com.fameden.common.constants;

import com.fameden.common.exception.BreachingSingletonException;

public final class CommonConstants {

	private CommonConstants() throws BreachingSingletonException {
		throw new BreachingSingletonException(CommonConstants.class.getName());
	}

	public static final String PADDING = "FAMEDEN";

	public static final String FAILURE = "FAILURE";
	public static final String SUCCESS = "SUCCESS";
	public static final String IN_PROCESS = "IN_PROCESS";
	public static final String ACTIVE = "Y";
	public static final String INACTIVE = "N";
	public static final String REQUEST_NOT_GENERATED = "Request was not generated";
	public static final String GENERIC_EXCEPTION = "Something went wrong while processing the request";
	public static final String FAMEDEN_INFO_EMAIL_ADDRESS = "fameden.info@gmail.com";
	public static final String FAMEDEN_ORG_NAME = "FameDen Inc.";

	public static final String INVALID_REQUEST_TYPE = "Request Type provided is either empty or invalid";
	public static final String INVALID_EMAIL_ADDRESS = "Email Address provided is either empty or invalid";
	public static final String EMPTY_PASSWORD = "Password provided is empty";
	public static final String USER_DO_NOT_EXIST = "The user entered do not exists";
	public static final String EMPTY_VERIFICATION_CODE = "The verification code provided is empty";

	public static final String EMAIL_TEMPLATE_FROM_PLACE_HOLDER = "from";

	public static final String EMAIL_TEMPLATES_PROPERTY = "emailTemplates";
	public static final String EMAIL_TEMPLATES_PATH = "/WEB-INF/classes/com/fameden/emailformats";

	public static final String EMAIL_TEMPLATE_FOLDER_LOCATION = System
			.getProperty(EMAIL_TEMPLATES_PROPERTY);

	public static final int CODE_LENGTH = 10;
	public static final String EMAIL_TEMPLATE_FULLNAME_PLACE_HOLDER = "fullName";

	public static final String UNSUPPORTED_REQUEST_TYPE = "OOPs!!! We dont support this request type right now.";

}
