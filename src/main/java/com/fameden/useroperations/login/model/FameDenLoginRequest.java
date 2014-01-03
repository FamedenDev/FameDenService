package com.fameden.useroperations.login.model;

import com.fameden.common.model.CommonRequestAttributes;

public class FameDenLoginRequest extends CommonRequestAttributes {

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
