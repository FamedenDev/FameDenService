package com.fameden.useroperations.login.constants;

import com.fameden.common.exception.BreachingSingletonException;

public class LoginConstatns {

	private LoginConstatns() throws BreachingSingletonException {
		throw new BreachingSingletonException(LoginConstatns.class.getName());
	}

	public static final String REQUEST_TYPE = "LOGIN";
	public static final String USER_IS_NOT_VERIFIED = "The user's email address is not verified";
	public static final String USER_INFO_DO_NOT_EXIST = "The user information does not exists";
	public static final String INCORRECT_PASSWORD = "Incorrect username or password";

}
