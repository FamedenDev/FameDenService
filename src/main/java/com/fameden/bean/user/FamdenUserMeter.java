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
@Table(name = "FAMEDEN_USER_METER")
public class FamdenUserMeter implements Serializable {

	private static final long serialVersionUID = 8312550596435406621L;
	@Id
	@GeneratedValue(generator = "famedenUserMeterId_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "famedenUserMeterId_seq", name = "famedenUserMeterId_seq")
	@Column(name = "fameden_User_Meter_Id")
	private int famedenUserMeterId;
	private double famedenAttr1;
	private double famedenAttr2;
	private double famedenAttr3;

	public int getFamedenUserMeterId() {
		return famedenUserMeterId;
	}

	public void setFamedenUserMeterId(int famedenUserMeterId) {
		this.famedenUserMeterId = famedenUserMeterId;
	}

	public double getFamedenAttr1() {
		return famedenAttr1;
	}

	public void setFamedenAttr1(double famedenAttr1) {
		this.famedenAttr1 = famedenAttr1;
	}

	public double getFamedenAttr2() {
		return famedenAttr2;
	}

	public void setFamedenAttr2(double famedenAttr2) {
		this.famedenAttr2 = famedenAttr2;
	}

	public double getFamedenAttr3() {
		return famedenAttr3;
	}

	public void setFamedenAttr3(double famedenAttr3) {
		this.famedenAttr3 = famedenAttr3;
	}

}
