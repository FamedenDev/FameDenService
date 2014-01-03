package com.fameden.housekeeping.model;

import com.fameden.common.model.CommonRequestAttributes;

public class RetrieveLatestFilesRequest extends CommonRequestAttributes {

	private String clientTnCFileChecksum;

	private String aboutUsFileChecksum;

	public String getClientTnCFileChecksum() {
		return clientTnCFileChecksum;
	}

	public void setClientTnCFileChecksum(String clientTnCFileChecksum) {
		this.clientTnCFileChecksum = clientTnCFileChecksum;
	}

	public String getAboutUsFileChecksum() {
		return aboutUsFileChecksum;
	}

	public void setAboutUsFileChecksum(String aboutUsFileChecksum) {
		this.aboutUsFileChecksum = aboutUsFileChecksum;
	}

}
