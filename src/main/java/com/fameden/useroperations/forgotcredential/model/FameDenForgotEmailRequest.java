package com.fameden.useroperations.forgotcredential.model;

import com.fameden.common.model.CommonRequestAttributes;

public class FameDenForgotEmailRequest extends CommonRequestAttributes {

	private String userMobileNumber;

	public String getUserMobileNumber() {
		return userMobileNumber;
	}

	public void setUserMobileNumber(String userMobileNumber) {
		this.userMobileNumber = userMobileNumber;
	}

}
