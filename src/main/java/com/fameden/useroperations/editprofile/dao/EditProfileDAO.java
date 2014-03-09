package com.fameden.useroperations.editprofile.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fameden.bean.user.FamdenUserMeter;
import com.fameden.bean.user.FamdenUserSocialNetworkDetail;
import com.fameden.bean.user.FamedenUserAddressBean;
import com.fameden.bean.user.FamedenUserBean;
import com.fameden.bean.user.FamedenUserInfoBean;
import com.fameden.common.dao.DatabaseConfig;
import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.useroperations.editprofile.dto.EditProfileDTO;
import com.fameden.useroperations.viewprofile.dao.ViewProfileDAO;
import com.fameden.useroperations.viewprofile.model.FameDenUserAddress;
import com.fameden.util.encryptdecrypt.RSAAlgorithmImpl;

public class EditProfileDAO {

	Logger logger = LoggerFactory.getLogger(ViewProfileDAO.class);

	private static EditProfileDAO SINGLETON;

	public static EditProfileDAO getInstance()
			throws BreachingSingletonException {
		if (SINGLETON == null)
			SINGLETON = new EditProfileDAO();

		return SINGLETON;
	}

	private EditProfileDAO() throws BreachingSingletonException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(
					EditProfileDAO.class.getName());
		}
	}

	@SuppressWarnings("unchecked")
	public FamedenUserInfoBean updateUserProfileInfo(EditProfileDTO dto)
			throws Exception {
		FamedenUserInfoBean userInfo = null;
		try {
			Session session = DatabaseConfig.getSessionFactory().openSession();

			session.beginTransaction();

			Criteria crit = session.createCriteria(FamedenUserInfoBean.class);

			crit.createAlias("famedenUserBean", "famedenUser");
			Criterion requestIDRestriction = Restrictions
					.eq("famedenUser.famdenExternalUserId",
							dto.getExternalUserId());
			crit.add(requestIDRestriction);
			List<FamedenUserInfoBean> famedenUserInfoList = ((List<FamedenUserInfoBean>) crit
					.list());

			if (famedenUserInfoList != null && famedenUserInfoList.size() > 0) {
				userInfo = famedenUserInfoList.get(0);
			}

			if (dto.getAddressList() != null && dto.getAddressList().size() > 0) {
				List<FamedenUserAddressBean> addressList = new ArrayList<FamedenUserAddressBean>();
				for (FameDenUserAddress address : dto.getAddressList()) {
					if (address != null) {
						FamedenUserAddressBean add = new FamedenUserAddressBean();
						add.setAddressLine1(address.getAddressLine1());
						add.setAddressLine2(address.getAddressLine2());
						add.setAddressType(address.getAddressType());
						add.setCity(address.getCity());
						add.setContactNumber(address.getContactNumber());
						add.setCountry(address.getCountry());
						add.setPinCode(address.getPinCode());
						add.setState(address.getState());
						addressList.add(add);
					}
				}
				userInfo.setAddressList(addressList);
			}
			if (dto.getAlternateEmailAddress() != null) {
				userInfo.setAlternateEmailAddress(dto
						.getAlternateEmailAddress());
			}
			if (dto.getDob() != null) {
				userInfo.setDob(dto.getDob());
			}
			if (dto.getFamdenUserMeterDetails() != null) {
				FamdenUserMeter famdenUserMeter = new FamdenUserMeter();
				if (dto.getFamdenUserMeterDetails().getFamedenAttr1() > 0) {
					famdenUserMeter.setFamedenAttr1(dto
							.getFamdenUserMeterDetails().getFamedenAttr1());
				}
				if (dto.getFamdenUserMeterDetails().getFamedenAttr2() > 0) {
					famdenUserMeter.setFamedenAttr2(dto
							.getFamdenUserMeterDetails().getFamedenAttr2());
				}
				if (dto.getFamdenUserMeterDetails().getFamedenAttr3() > 0) {
					famdenUserMeter.setFamedenAttr3(dto
							.getFamdenUserMeterDetails().getFamedenAttr2());
				}
				userInfo.setFamdenUserMeter(famdenUserMeter);
			}
			if (dto.getMobileNumber() != null) {
				FamedenUserBean userBean = new FamedenUserBean();
				userBean.setMobileNumber(dto.getMobileNumber());
				userInfo.setFamedenUserBean(userBean);
			}
			if (dto.getFullName() != null) {
				userInfo.setFullName(dto.getFullName());
			}
			if (dto.getProfileImageURL() != null) {
				userInfo.setProfileImageURL(dto.getProfileImageURL());
			}
			if (dto.getSocialNetworkDetails() != null) {
				FamdenUserSocialNetworkDetail fsnd = new FamdenUserSocialNetworkDetail();
				fsnd.setPrivateToken(RSAAlgorithmImpl
						.getInstance()
						.decryptText(
								dto.getSocialNetworkDetails().getPrivateToken()));
				fsnd.setPublicToken(RSAAlgorithmImpl.getInstance().decryptText(
						dto.getSocialNetworkDetails().getPublicToken()));
				fsnd.setSocialNetworkName(dto.getSocialNetworkDetails()
						.getSocialNetworkName());
				userInfo.setSocialNetworkDetail(fsnd);
			}
			if (dto.getUserInterests() != null) {
				userInfo.setUserInterests(dto.getUserInterests());
			}
			
			session.update(userInfo);

			session.getTransaction().commit();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return userInfo;
	}
}
