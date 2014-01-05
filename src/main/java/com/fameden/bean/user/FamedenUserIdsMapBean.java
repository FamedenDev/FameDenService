package com.fameden.bean.user;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * This hibernate bean is responsible of storing the mapping of
 * external and internal user id.
 * This table is the only place where we can get the mapping of external and internal 
 * user ids.
 */

@Entity
@Table(name = "FAMEDEN_USER_MAPPING")
public class FamedenUserIdsMapBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private FamedenUserMappingCompositePK famedenUserMappingCompositePK;

	public FamedenUserMappingCompositePK getFamedenUserMappingCompositePK() {
		return famedenUserMappingCompositePK;
	}

	public void setFamedenUserMappingCompositePK(
			FamedenUserMappingCompositePK famedenUserMappingCompositePK) {
		this.famedenUserMappingCompositePK = famedenUserMappingCompositePK;
	}

}
