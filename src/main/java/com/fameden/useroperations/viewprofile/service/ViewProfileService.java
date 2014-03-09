package com.fameden.useroperations.viewprofile.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fameden.bean.user.FamedenUserAddressBean;
import com.fameden.bean.user.FamedenUserBean;
import com.fameden.bean.user.FamedenUserInfoBean;
import com.fameden.common.constants.CommonConstants;
import com.fameden.common.dao.CommonOperationsDAO;
import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.common.service.ICommonService;
import com.fameden.useroperations.registration.service.RegistrationService;
import com.fameden.useroperations.viewprofile.constants.ViewProfileConstants;
import com.fameden.useroperations.viewprofile.dao.ViewProfileDAO;
import com.fameden.useroperations.viewprofile.dto.ViewProfileDTO;
import com.fameden.useroperations.viewprofile.exception.ViewProfileException;
import com.fameden.useroperations.viewprofile.model.FameDenMeterDetails;
import com.fameden.useroperations.viewprofile.model.FameDenUserAddress;
import com.fameden.useroperations.viewprofile.model.FameDenViewProfileResponse;
import com.fameden.util.CommonValidations;
import com.fameden.util.encryptdecrypt.RSAAlgorithmImpl;

public class ViewProfileService implements ICommonService {

	private static ViewProfileService SINGLETON;

	private Logger logger = LoggerFactory.getLogger(ViewProfileService.class);
	private CommonOperationsDAO commonDao;

	public static ViewProfileService getInstance()
			throws BreachingSingletonException {
		if (SINGLETON == null)
			SINGLETON = new ViewProfileService();

		return SINGLETON;
	}

	private ViewProfileService() throws BreachingSingletonException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(
					RegistrationService.class.getName());
		}
	}

	@Override
	public boolean validate(Object object) throws Exception {
		ViewProfileDTO dto = (ViewProfileDTO) object;
		boolean result = false;
		FamedenUserBean user = null;

		if (CommonValidations.isStringEmpty(dto.getRequestType())
				&& dto.getExternalUserId() > 0
				&& CommonValidations.isStringEmpty(dto.getViewProfileUserId())
				&& dto.getRequestType().equals(
						ViewProfileConstants.REQUEST_TYPE)) {
			try {
				user = commonDao
						.searchByExternalUserId(dto.getExternalUserId());
			} catch (Exception e) {
				throw new ViewProfileException(
						ViewProfileConstants.REQUESTING_USER_DOES_NOT_EXIST);
			}
			if (user != null) {
				try {
					user = commonDao.searchByExternalUserId(Integer
							.parseInt(RSAAlgorithmImpl.getInstance()
									.decryptText(dto.getViewProfileUserId())));
				} catch (Exception e) {
					throw new ViewProfileException(
							ViewProfileConstants.VIEW_PROFILE_USER_DOES_NOT_EXIST);
				}
				if (user != null) {
					result = true;
				} else
					throw new ViewProfileException(
							ViewProfileConstants.VIEW_PROFILE_USER_DOES_NOT_EXIST);
			} else
				throw new ViewProfileException(
						ViewProfileConstants.REQUESTING_USER_DOES_NOT_EXIST);
		}

		return result;
	}

	@Override
	public Object processRequest(Object object) {
		FameDenViewProfileResponse response = null;
		ViewProfileDTO dto = (ViewProfileDTO) object;
		try {
			if (validate(dto)) {

				try {
					response = getProfileInformation(Integer
							.parseInt(RSAAlgorithmImpl.getInstance()
									.decryptText(dto.getViewProfileUserId())));
				} catch (NumberFormatException | NoSuchAlgorithmException
						| NoSuchPaddingException | BreachingSingletonException e) {
					response = new FameDenViewProfileResponse();
					response.setRequestStatus(CommonConstants.FAILURE);
					response.setErrorCode("101");
					response.setErrorMessage(e.getMessage());
					logger.error(e.getMessage(), e);
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return response;
	}

	public FameDenViewProfileResponse getProfileInformation(int externalUserId) {
		FameDenViewProfileResponse response = null;
		try {
			FamedenUserInfoBean bean = ViewProfileDAO.getInstance()
					.getUserProfileInfo(externalUserId);
			List<FameDenUserAddress> addressList = null;
			response = new FameDenViewProfileResponse();

			if (bean.getAddressList() != null
					&& bean.getAddressList().size() > 0) {
				addressList = new ArrayList<>();
				for (FamedenUserAddressBean address : bean.getAddressList()) {
					if (address != null) {
						FameDenUserAddress add = new FameDenUserAddress();
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
			}
			response.setAddressList(addressList);
			response.setAlternateEmailAddress(bean.getAlternateEmailAddress());
			response.setDob(bean.getDob());
			response.setEmailAddress(bean.getFamedenUserBean()
					.getEmailAddress());
			if (bean.getFamdenUserMeter() != null) {
				FameDenMeterDetails famedenUserMeterDetails = new FameDenMeterDetails();
				famedenUserMeterDetails.setFamedenAttr1(bean
						.getFamdenUserMeter().getFamedenAttr1());
				famedenUserMeterDetails.setFamedenAttr2(bean
						.getFamdenUserMeter().getFamedenAttr1());
				famedenUserMeterDetails.setFamedenAttr3(bean
						.getFamdenUserMeter().getFamedenAttr1());
				response.setFamdenUserMeterDetails(famedenUserMeterDetails);
			}
			response.setFullName(bean.getFullName());
			response.setMobileNumber(bean.getFamedenUserBean()
					.getMobileNumber());
			response.setProfileImageURL(bean.getProfileImageURL());
			response.setRequestStatus(CommonConstants.SUCCESS);
			response.setUserInterests(bean.getUserInterests());
		} catch (BreachingSingletonException e) {
			response = new FameDenViewProfileResponse();
			response.setRequestStatus(CommonConstants.FAILURE);
			response.setErrorCode("101");
			response.setErrorMessage(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			response = new FameDenViewProfileResponse();
			response.setRequestStatus(CommonConstants.FAILURE);
			response.setErrorCode("101");
			response.setErrorMessage(e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public int insertRequest(Object obj) throws Exception {
		return 0;
	}

}
