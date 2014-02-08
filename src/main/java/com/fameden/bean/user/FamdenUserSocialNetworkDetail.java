package com.fameden.bean.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FAMEDEN_SOCIAL_NETWORK_DETAIL")
public class FamdenUserSocialNetworkDetail implements Serializable {

	private static final long serialVersionUID = -2844433969066285738L;

	@Id
	@GeneratedValue(generator = "famedenSocialNetworkDetailId_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "famedenSocialNetworkDetailId_seq", name = "famedenSocialNetworkDetailId_seq")
	@Column(name = "fameden_Social_Network_Detail_id")
	private int famedenSocialNetworkDetailId;
	private String socialNetworkName;
	private String publicToken;
	private String privateToken;

	public int getFamedenSocialNetworkDetailId() {
		return famedenSocialNetworkDetailId;
	}

	public void setFamedenSocialNetworkDetailId(int famedenSocialNetworkDetailId) {
		this.famedenSocialNetworkDetailId = famedenSocialNetworkDetailId;
	}

	public String getSocialNetworkName() {
		return socialNetworkName;
	}

	public void setSocialNetworkName(String socialNetworkName) {
		this.socialNetworkName = socialNetworkName;
	}

	public String getPublicToken() {
		return publicToken;
	}

	public void setPublicToken(String publicToken) {
		this.publicToken = publicToken;
	}

	public String getPrivateToken() {
		return privateToken;
	}

	public void setPrivateToken(String privateToken) {
		this.privateToken = privateToken;
	}
}
