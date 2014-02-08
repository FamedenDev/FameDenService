package com.fameden.useroperations.viewprofile.constants;

import com.fameden.common.exception.BreachingSingletonException;

public class ViewProfileConstants {

	private ViewProfileConstants() throws BreachingSingletonException {
		throw new BreachingSingletonException(
				ViewProfileConstants.class.getName());
	}

	public static final String REQUEST_TYPE = "VIEW_PROFILE";
	public static final String REQUESTING_USER_DOES_NOT_EXIST = "The requesting user do not exists";
	public static final String VIEW_PROFILE_USER_DOES_NOT_EXIST = "The user's profile requested do not exists";
}
