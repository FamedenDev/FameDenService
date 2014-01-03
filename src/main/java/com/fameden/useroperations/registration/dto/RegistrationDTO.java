package com.fameden.useroperations.registration.dto;

import com.fameden.common.dto.CommonAttributesDTO;

public class RegistrationDTO extends CommonAttributesDTO {

	private String userFullName;
	private String userPassword;

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}
