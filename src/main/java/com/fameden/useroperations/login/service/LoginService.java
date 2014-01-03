package com.fameden.useroperations.login.service;

import java.sql.Date;
import java.util.Calendar;

import com.fameden.bean.request.FamedenRequestBean;
import com.fameden.bean.request.FamedenRequestDetailBean;
import com.fameden.bean.user.FamedenUserBean;
import com.fameden.bean.user.FamedenUserInfoBean;
import com.fameden.common.constants.CommonConstants;
import com.fameden.common.dao.CommonOperationsDAO;
import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.common.service.ICommonService;
import com.fameden.useroperations.login.constants.LoginConstatns;
import com.fameden.useroperations.login.dao.LoginDAO;
import com.fameden.useroperations.login.dto.LoginDTO;
import com.fameden.useroperations.login.exception.LoginValidationException;
import com.fameden.useroperations.login.model.FameDenLoginResponse;
import com.fameden.util.CommonValidations;
import com.fameden.util.encryptdecrypt.RSAAlgorithmImpl;

public class LoginService implements ICommonService {

	private static LoginService SINGLETON;

	public static LoginService getInstance() throws BreachingSingletonException {
		if (SINGLETON == null)
			SINGLETON = new LoginService();

		return SINGLETON;
	}

	private LoginService() throws BreachingSingletonException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(LoginService.class.getName());
		}
	}

	@Override
	public boolean validate(Object obj) throws Exception {
		LoginDTO dto = (LoginDTO) obj;

		boolean result = false;
		if (!CommonValidations.isStringEmpty(dto.getUserEmailAddress())
				&& CommonValidations.isValidEmailAddress(dto
						.getUserEmailAddress())) {
			if (!CommonValidations.isStringEmpty(dto.getRequestType())
					&& dto.getRequestType().trim()
							.equals(LoginConstatns.REQUEST_TYPE)) {
				if (!CommonValidations.isStringEmpty(dto.getPassword())) {
					result = true;
				} else {
					throw new LoginValidationException(
							CommonConstants.EMPTY_PASSWORD);
				}
			} else {
				throw new LoginValidationException(
						CommonConstants.INVALID_REQUEST_TYPE);
			}
		} else {
			throw new LoginValidationException(
					CommonConstants.INVALID_EMAIL_ADDRESS);
		}

		return result;
	}

	@Override
	public Object processRequest(Object dto) {
		FameDenLoginResponse response = new FameDenLoginResponse();
		CommonOperationsDAO commonDao = null;
		int requestId = 0;
		try {
			commonDao = CommonOperationsDAO.getInstance();
			boolean validate = validate(dto);

			if (validate) {
				CommonOperationsDAO dao = CommonOperationsDAO.getInstance();
				FamedenUserBean user = dao.searchByEmailId(((LoginDTO) dto)
						.getUserEmailAddress());
				if (user != null) {
					requestId = insertRequest(dto);

					RSAAlgorithmImpl rsaImpl = RSAAlgorithmImpl.getInstance();
					((LoginDTO) dto).setPassword(rsaImpl
							.decryptText(((LoginDTO) dto).getPassword()));

					LoginDAO loginDao = LoginDAO.getInstance();
					((LoginDTO) dto).setExternalUserId(user
							.getFamdenExternalUserId());
					FamedenUserInfoBean userInfo = loginDao
							.authenticateUser((LoginDTO) dto);

					response.setErrorCode(null);
					response.setErrorMessage(null);
					response.setRequestStatus(CommonConstants.SUCCESS);
					response.setUserAlternativeEmailAddress(userInfo
							.getAlternateEmailAddress());
					response.setUserEmailAddress(userInfo.getFamedenUserBean()
							.getEmailAddress());
					response.setUserFullName(userInfo.getFullName());
					response.setUserInterests(userInfo.getUserInterests());
				} else
					throw new LoginValidationException(
							CommonConstants.USER_DO_NOT_EXIST);

			}
		} catch (LoginValidationException e) {
			response.setErrorCode("400");
			response.setRequestStatus(CommonConstants.FAILURE);
			response.setErrorMessage(e.getMessage());

		} catch (Exception e) {
			response.setErrorCode("500");
			response.setRequestStatus(CommonConstants.FAILURE);
			response.setErrorMessage(e.getMessage());
		}
		try {
			commonDao.updateRequestStatus(requestId,
					response.getRequestStatus());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return response;
	}

	@Override
	public int insertRequest(Object obj) throws Exception {
		int requestId = 0;
		FamedenRequestDetailBean famedenRequestDetailBean = null;
		FamedenRequestBean famedenRequestBean = null;
		LoginDTO dto = (LoginDTO) obj;
		if (dto != null) {
			famedenRequestBean = new FamedenRequestBean();
			famedenRequestBean.setCustomerIP(dto.getUserIPAddress());
			famedenRequestBean.setRequestStatus(CommonConstants.IN_PROCESS);
			famedenRequestBean.setRequestType(LoginConstatns.REQUEST_TYPE);
			famedenRequestBean.setRequestUser(dto.getUserEmailAddress());
			famedenRequestBean.setRequestDate(new Date(Calendar.getInstance()
					.getTimeInMillis()));
			famedenRequestBean.setFamedenUserId(0);
			famedenRequestDetailBean = new FamedenRequestDetailBean();
			famedenRequestDetailBean.setFamedenRequestBean(famedenRequestBean);

			CommonOperationsDAO dao = CommonOperationsDAO.getInstance();
			requestId = dao.insertRequest(famedenRequestDetailBean);

		}
		return requestId;
	}

}
