package com.fameden.useroperations.editprofile.constants;

import com.fameden.common.exception.BreachingSingletonException;

public class EditProfileConstants {

	private EditProfileConstants() throws BreachingSingletonException {
		throw new BreachingSingletonException(
				EditProfileConstants.class.getName());
	}

	public static final String REQUEST_TYPE = "EDIT_PROFILE";
}
