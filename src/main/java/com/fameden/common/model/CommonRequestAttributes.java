package com.fameden.common.model;

/*
 * This class contains common attributes that a FameDen Request 
 * will contain.
 */
public class CommonRequestAttributes {
	/*
	 * Each request sent from client should have userEmailAddress
	 */
	private String userEmailAddress;
	/*
	 * This attribute stores the type of request requested by the user
	 */
	private String requestType;

	public String getUserEmailAddress() {
		return userEmailAddress;
	}

	public void setUserEmailAddress(String userEmailAddress) {
		this.userEmailAddress = userEmailAddress;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
}
