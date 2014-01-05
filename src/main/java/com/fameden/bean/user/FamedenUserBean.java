package com.fameden.bean.user;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fameden.bean.request.FamedenRequestBean;

/*
 * This hibernate bean represents the table that will store the user details.
 * The data that will be stored will be very high level information related to
 * the user. 
 * This table contains info like primary emailAddress of the user, user's mobile no.,
 * whether the user is verified and active, when the user account was created or updated.
 * It also links the request Id to get the information of the location where the user requested
 * from and also request Id is used as email verification code to verify user's emailaddress.
 * 
 */

@Entity
@Table(name = "FAMEDEN_USERS")
public class FamedenUserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "famdenExternalUserId_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "famdenExternalUserId_seq", name = "famdenExternalUserId_seq")
	private int famdenExternalUserId;
	private String emailAddress;
	private String mobileNumber;
	private Date updateDate;
	private Date creationDate;
	private String isVerified;
	private String active;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "requestID")
	private FamedenRequestBean famedenRequestBean;

	public int getFamdenExternalUserId() {
		return famdenExternalUserId;
	}

	public void setFamdenExternalUserId(int famdenExternalUserId) {
		this.famdenExternalUserId = famdenExternalUserId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(String isVerified) {
		this.isVerified = isVerified;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public FamedenRequestBean getFamedenRequestBean() {
		return famedenRequestBean;
	}

	public void setFamedenRequestBean(FamedenRequestBean famedenRequestBean) {
		this.famedenRequestBean = famedenRequestBean;
	}

}
