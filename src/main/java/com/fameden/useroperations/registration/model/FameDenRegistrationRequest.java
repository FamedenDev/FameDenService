package com.fameden.useroperations.registration.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fameden.common.model.CommonRequestAttributes;

@XmlRootElement
@XmlType
public class FameDenRegistrationRequest extends CommonRequestAttributes {

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
