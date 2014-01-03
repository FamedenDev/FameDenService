package com.fameden.bean.user;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Embeddable
public class FamedenUserMappingCompositePK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "famdenExternalUserId")
	private FamedenUserBean famedenUserBean;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "famdenInternalUserId")
	private FamedenUserKeysBean famedenUserKeysBean;

	public FamedenUserBean getFamedenUser() {
		return famedenUserBean;
	}

	public void setFamedenUser(FamedenUserBean famedenUserBean) {
		this.famedenUserBean = famedenUserBean;
	}

	public FamedenUserKeysBean getFamedenUserKeys() {
		return famedenUserKeysBean;
	}

	public void setFamedenUserKeys(FamedenUserKeysBean famedenUserKeysBean) {
		this.famedenUserKeysBean = famedenUserKeysBean;
	}

}
