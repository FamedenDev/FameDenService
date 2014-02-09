package com.fameden.bean.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FAMEDEN_USER_ADDRESS")
public class FamedenUserAddressBean implements Serializable {

	private static final long serialVersionUID = -3455978118944864112L;
	@Id
	@GeneratedValue(generator = "famedenUserInfoId_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "famedenUserInfoId_seq", name = "famedenUserInfoId_seq")
	@Column(name = "fameden_User_Address_Id")
	private int famedenUserAddressId;
	private String addressType;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String country;
	private String pinCode;
	private String contactNumber;
	@ManyToOne
    @JoinColumn(name="famden_user_info_id", 
            insertable=false, updatable=false, 
            nullable=false)
	private FamedenUserInfoBean famedenUserInfoBean;

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public int getFamedenUserAddressId() {
		return famedenUserAddressId;
	}

	public void setFamedenUserAddressId(int famedenUserAddressId) {
		this.famedenUserAddressId = famedenUserAddressId;
	}

	public FamedenUserInfoBean getFamedenUserInfoBean() {
		return famedenUserInfoBean;
	}

	public void setFamedenUserInfoBean(FamedenUserInfoBean famedenUserInfoBean) {
		this.famedenUserInfoBean = famedenUserInfoBean;
	}

}
