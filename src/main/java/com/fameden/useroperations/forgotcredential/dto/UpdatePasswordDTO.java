package com.fameden.useroperations.forgotcredential.dto;

import com.fameden.common.dto.CommonAttributesDTO;

public class UpdatePasswordDTO extends CommonAttributesDTO {

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
