package com.fameden.useroperations.login.dto;

import com.fameden.common.dto.CommonAttributesDTO;

public class LoginDTO extends CommonAttributesDTO {

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
