package com.fameden.useroperations.viewprofile.dto;

import com.fameden.common.dto.CommonAttributesDTO;

public class ViewProfileDTO extends CommonAttributesDTO {

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
