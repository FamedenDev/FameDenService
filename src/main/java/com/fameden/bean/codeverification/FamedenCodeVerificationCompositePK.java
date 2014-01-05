package com.fameden.bean.codeverification;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fameden.bean.user.FamedenUserBean;

/*
 * This is the composite embeddable key that will be used in code verification table.
 * here verification type and fameden external user id is used as comopiste PK. 
*/

@Embeddable
public class FamedenCodeVerificationCompositePK implements Serializable {

	private static final long serialVersionUID = 1L;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "famdenExternalUserId")
	private FamedenUserBean famedenUserBean;
	private String verificationType;

	public FamedenUserBean getFamedenUserBean() {
		return famedenUserBean;
	}

	public void setFamedenUserBean(FamedenUserBean famedenUserBean) {
		this.famedenUserBean = famedenUserBean;
	}

	public String getVerificationType() {
		return verificationType;
	}

	public void setVerificationType(String verificationType) {
		this.verificationType = verificationType;
	}

}
