package com.fameden.useroperations.viewprofile.model;

import com.fameden.common.model.CommonRequestAttributes;

public class FameDenViewProfileRequest extends CommonRequestAttributes {

	private String requestingUserId;
	private String viewProfileUserId;

	public String getRequestingUserId() {
		return requestingUserId;
	}

	public void setRequestingUserId(String requestingUserId) {
		this.requestingUserId = requestingUserId;
	}

	public String getViewProfileUserId() {
		return viewProfileUserId;
	}

	public void setViewProfileUserId(String viewProfileUserId) {
		this.viewProfileUserId = viewProfileUserId;
	}

}
