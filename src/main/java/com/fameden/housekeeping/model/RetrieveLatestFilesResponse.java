package com.fameden.housekeeping.model;

import com.fameden.common.model.CommonResponseAttributes;

public class RetrieveLatestFilesResponse extends CommonResponseAttributes {

	private byte[] remoteTnCFile;

	private byte[] aboutUsFile;

	public byte[] getRemoteTnCFile() {
		return remoteTnCFile;
	}

	public void setRemoteTnCFile(byte[] remoteTnCFile) {
		this.remoteTnCFile = remoteTnCFile;
	}

	public byte[] getAboutUsFile() {
		return aboutUsFile;
	}

	public void setAboutUsFile(byte[] aboutUsFile) {
		this.aboutUsFile = aboutUsFile;
	}

}
