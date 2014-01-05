package com.fameden.bean.codeverification;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * This hibernate bean class represents the table where the verification code will be stored.
 * The verification code can be for forgot password, verification of mobile numbers.
 * The columns that the table consists of are external user id, request type and the verification code.
 * The external user id and the request type creates a composite primary key.
 * For each user and request type only one record will be stored.
*/


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
