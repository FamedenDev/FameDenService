package com.fameden.useroperations.forgotcredential.dto;

import com.fameden.common.dto.CommonAttributesDTO;

public class ForgotCredentialDTO extends CommonAttributesDTO {

	private String userMobileNumber;

	private String verificationCode;

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getUserMobileNumber() {
		return userMobileNumber;
	}

	public void setUserMobileNumber(String userMobileNumber) {
		this.userMobileNumber = userMobileNumber;
	}

}
