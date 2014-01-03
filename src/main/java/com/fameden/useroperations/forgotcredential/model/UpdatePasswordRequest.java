package com.fameden.useroperations.forgotcredential.model;

import com.fameden.common.model.CommonRequestAttributes;

public class UpdatePasswordRequest extends CommonRequestAttributes {

	private String password;
	private String verificationCode;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

}
