package com.fameden.bean.codeverification;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FAMEDEN_VERIFICATION")
public class FamedenCodeVerificationBean implements Serializable {

	private static final long serialVersionUID = 8220636283439922734L;
	@EmbeddedId
	private FamedenCodeVerificationCompositePK compositePK;
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public FamedenCodeVerificationCompositePK getCompositePK() {
		return compositePK;
	}

	public void setCompositePK(FamedenCodeVerificationCompositePK compositePK) {
		this.compositePK = compositePK;
	}

}
