package com.fameden.useroperations.editprofile.service;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Calendar;

import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fameden.bean.request.FamedenRequestBean;
import com.fameden.bean.request.FamedenRequestDetailBean;
import com.fameden.bean.user.FamedenUserBean;
import com.fameden.common.constants.CommonConstants;
import com.fameden.common.dao.CommonOperationsDAO;
import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.common.model.CommonResponseAttributes;
import com.fameden.common.service.ICommonService;
import com.fameden.useroperations.editprofile.constants.EditProfileConstants;
import com.fameden.useroperations.editprofile.dto.EditProfileDTO;
import com.fameden.useroperations.registration.constants.RegistrationConstants;
import com.fameden.useroperations.registration.dto.RegistrationDTO;
import com.fameden.useroperations.viewprofile.constants.ViewProfileConstants;
import com.fameden.useroperations.viewprofile.dto.ViewProfileDTO;
import com.fameden.useroperations.viewprofile.exception.ViewProfileException;
import com.fameden.useroperations.viewprofile.model.FameDenViewProfileResponse;
import com.fameden.util.CommonValidations;
import com.fameden.util.encryptdecrypt.RSAAlgorithmImpl;

public class EditProfileService implements ICommonService {

	private static EditProfileService SINGLETON;
	private CommonOperationsDAO commonDao;
	private Logger logger = LoggerFactory.getLogger(EditProfileService.class);

	public static EditProfileService getInstance()
			throws BreachingSingletonException {
		if (SINGLETON == null)
			SINGLETON = new EditProfileService();

		return SINGLETON;
	}

	private EditProfileService() throws BreachingSingletonException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(
					EditProfileService.class.getName());
		}
	}

	@Override
	public boolean validate(Object obj) throws Exception {
		EditProfileDTO dto = (EditProfileDTO) obj;
		boolean result = false;
		FamedenUserBean user = null;

		if (CommonValidations.isStringEmpty(dto.getRequestType())
				&& dto.getExternalUserId() > 0
				&& dto.getRequestType().equals(
						EditProfileConstants.REQUEST_TYPE)) {
			try {
				user = commonDao
						.searchByExternalUserId(dto.getExternalUserId());
			} catch (Exception e) {
				throw new ViewProfileException(
						CommonConstants.USER_DO_NOT_EXIST);
			}
			if (user != null) {
				result = true;
			} else
				throw new ViewProfileException(
						CommonConstants.USER_DO_NOT_EXIST);
		}

		return result;
	}

	@Override
	public Object processRequest(Object obj) {
		CommonResponseAttributes response = null;
		EditProfileDTO dto = (EditProfileDTO) obj;
		int requestId = 0;
		boolean result = false;
		try {
			if (validate(dto)) {
				requestId = insertRequest(dto);
				try {
					response = null;
					
					if(result){
						
					}else{
						response = new CommonResponseAttributes();
						response.setRequestStatus(CommonConstants.FAILURE);
						response.setErrorCode("101");
						response.setErrorMessage(CommonConstants.GENERIC_EXCEPTION);
					}
				} catch (Exception e) {
					response = new CommonResponseAttributes();
					response.setRequestStatus(CommonConstants.FAILURE);
					response.setErrorCode("101");
					response.setErrorMessage(e.getMessage());
					logger.error(e.getMessage(), e);
				}

				if (requestId > 0) {
					try {
						commonDao = CommonOperationsDAO.getInstance();
						commonDao.updateRequestStatus(requestId,
								response.getRequestStatus());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}
		} catch (Exception e) {
			response = new CommonResponseAttributes();
			response.setRequestStatus(CommonConstants.FAILURE);
			response.setErrorCode("101");
			response.setErrorMessage(e.getMessage());
			logger.error(e.getMessage(), e);
		}

		return response;
	}

	@Override
	public int insertRequest(Object obj) throws Exception {
		int requestId = 0;
		FamedenRequestDetailBean famedenRequestDetailBean = null;
		FamedenRequestBean famedenRequestBean = null;
		EditProfileDTO dto = (EditProfileDTO) obj;
		if (dto != null) {
			famedenRequestBean = new FamedenRequestBean();
			famedenRequestBean.setCustomerIP(dto.getUserIPAddress());
			famedenRequestBean.setRequestStatus(CommonConstants.IN_PROCESS);
			famedenRequestBean
					.setRequestType(RegistrationConstants.REQUEST_TYPE);
			famedenRequestBean.setRequestUser(dto.getUserEmailAddress());
			famedenRequestBean.setRequestDate(new Date(Calendar.getInstance()
					.getTimeInMillis()));
			famedenRequestBean.setFamedenUserId(dto.getExternalUserId());
			famedenRequestDetailBean = new FamedenRequestDetailBean();
			famedenRequestDetailBean.setFamedenRequestBean(famedenRequestBean);

			CommonOperationsDAO dao = CommonOperationsDAO.getInstance();
			requestId = dao.insertRequest(famedenRequestDetailBean);

		}
		return requestId;
	}

}
