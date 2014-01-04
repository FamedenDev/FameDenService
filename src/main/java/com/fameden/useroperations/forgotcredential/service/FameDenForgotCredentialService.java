package com.fameden.useroperations.forgotcredential.service;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Calendar;

import javax.crypto.NoSuchPaddingException;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fameden.bean.codeverification.FamedenCodeVerificationBean;
import com.fameden.bean.request.FamedenRequestBean;
import com.fameden.bean.request.FamedenRequestDetailBean;
import com.fameden.bean.user.FamedenUserInfoBean;
import com.fameden.common.constants.CommonConstants;
import com.fameden.common.dao.CommonOperationsDAO;
import com.fameden.common.dto.CommonAttributesDTO;
import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.common.model.CommonResponseAttributes;
import com.fameden.common.service.ICommonService;
import com.fameden.useroperations.forgotcredential.constants.ForgotCredentialConstants;
import com.fameden.useroperations.forgotcredential.dao.ForgotCredentialDAO;
import com.fameden.useroperations.forgotcredential.dto.ForgotCredentialDTO;
import com.fameden.useroperations.forgotcredential.dto.UpdatePasswordDTO;
import com.fameden.useroperations.forgotcredential.exception.ForgotCredentialException;
import com.fameden.util.CommonValidations;
import com.fameden.util.RandomCodeGenerator;
import com.fameden.util.encryptdecrypt.RSAAlgorithmImpl;
import com.fameden.util.encryptdecrypt.SaltAlgorithmImpl;
import com.fameden.util.mail.SendMail;

public class FameDenForgotCredentialService implements ICommonService {

	private static FameDenForgotCredentialService SINGLETON;
	private Logger logger = LoggerFactory
			.getLogger(FameDenForgotCredentialService.class);

	private CommonOperationsDAO commonDao = null;

	public static FameDenForgotCredentialService getInstance()
			throws BreachingSingletonException {
		if (SINGLETON == null)
			SINGLETON = new FameDenForgotCredentialService();

		return SINGLETON;
	}

	private FameDenForgotCredentialService() throws BreachingSingletonException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(
					FameDenForgotCredentialService.class.getName());
		}
	}

	@Override
	public boolean validate(Object obj) throws Exception {
		boolean result = false;
		switch (((CommonAttributesDTO) obj).getRequestType()) {

		case ForgotCredentialConstants.UPDATE_PASSWORD_REQUEST_TYPE:
			result = validateUpdatePassword((UpdatePasswordDTO) obj);
			break;
		case ForgotCredentialConstants.FORGOT_PASSWORD_REQUEST_TYPE:
			result = validateForgotPassword((ForgotCredentialDTO) obj);
			break;
		}
		return result;
	}

	private boolean validateForgotPassword(ForgotCredentialDTO dto)
			throws ForgotCredentialException {
		boolean result = false;
		if (!CommonValidations.isStringEmpty(dto.getUserEmailAddress())) {
			if (!CommonValidations.isStringEmpty(dto.getRequestType())
					&& dto.getRequestType()
							.equals(ForgotCredentialConstants.FORGOT_PASSWORD_REQUEST_TYPE)) {
				result = true;
			} else {
				throw new ForgotCredentialException(
						CommonConstants.INVALID_REQUEST_TYPE);
			}
		} else {
			throw new ForgotCredentialException(
					CommonConstants.INVALID_EMAIL_ADDRESS);
		}
		return result;
	}

	private boolean validateUpdatePassword(UpdatePasswordDTO dto)
			throws ForgotCredentialException {
		boolean result = false;
		if (!CommonValidations.isStringEmpty(dto.getPassword())) {
			if (!CommonValidations.isStringEmpty(dto.getRequestType())
					&& dto.getRequestType()
							.equals(ForgotCredentialConstants.UPDATE_PASSWORD_REQUEST_TYPE)) {
				if (CommonValidations.isValidEmailAddress(dto
						.getUserEmailAddress())) {
					if (!CommonValidations.isStringEmpty(dto
							.getVerificationCode())) {
						result = true;
					} else {
						throw new ForgotCredentialException(
								CommonConstants.EMPTY_VERIFICATION_CODE);
					}
				} else {
					throw new ForgotCredentialException(
							CommonConstants.INVALID_EMAIL_ADDRESS);
				}
			} else {
				throw new ForgotCredentialException(
						CommonConstants.INVALID_REQUEST_TYPE);
			}
		} else {
			throw new ForgotCredentialException(
					CommonConstants.INVALID_EMAIL_ADDRESS);
		}
		return result;
	}

	@Override
	public Object processRequest(Object dto) {
		CommonResponseAttributes response = new CommonResponseAttributes();
		int requestId = 0;
		try {
			boolean validate = validate(dto);
			commonDao = CommonOperationsDAO.getInstance();

			if (validate) {
				ForgotCredentialDAO dao = ForgotCredentialDAO.getInstance();

				if (((CommonAttributesDTO) dto).getRequestType().equals(
						ForgotCredentialConstants.UPDATE_PASSWORD_REQUEST_TYPE)) {
					FamedenCodeVerificationBean verificationBean = dao
							.getVerificationCodeDetails(
									((UpdatePasswordDTO) dto)
											.getUserEmailAddress(),
									null,
									((UpdatePasswordDTO) dto)
											.getVerificationCode(),
									ForgotCredentialConstants.FORGOT_PASSWORD_REQUEST_TYPE);

					if (verificationBean != null) {
						SaltAlgorithmImpl saltImpl = SaltAlgorithmImpl
								.getInstance();
						RSAAlgorithmImpl rsaImpl = RSAAlgorithmImpl
								.getInstance();
						String encryptedPassword = saltImpl.createHash(rsaImpl
								.decryptText(((UpdatePasswordDTO) dto)
										.getPassword()));
						((UpdatePasswordDTO) dto)
								.setPassword(encryptedPassword);

						dao.upatePasswordByEmailId(
								((UpdatePasswordDTO) dto).getUserEmailAddress(),
								((UpdatePasswordDTO) dto).getPassword());
						response.setRequestStatus(CommonConstants.SUCCESS);

					} else {
						throw new ForgotCredentialException(
								CommonConstants.INVALID_REQUEST_TYPE);

					}
				} else if (((ForgotCredentialDTO) dto).getRequestType().equals(
						ForgotCredentialConstants.FORGOT_PASSWORD_REQUEST_TYPE)) {
					FamedenUserInfoBean user = commonDao
							.getVerifiedUserInfoByEmailId(((ForgotCredentialDTO) dto)
									.getUserEmailAddress());

					if (user != null) {
						requestId = insertRequest(dto);
						String randomCode = RandomCodeGenerator
								.randomString(CommonConstants.CODE_LENGTH);
						((ForgotCredentialDTO) dto)
								.setVerificationCode(randomCode);
						dao.insertVerificationCode((ForgotCredentialDTO) dto);

						response.setRequestStatus(CommonConstants.SUCCESS);

						sendForgotPasswordMailNotification(
								((ForgotCredentialDTO) dto)
										.getUserEmailAddress(),
								user.getFullName(), randomCode);

					} else {
						throw new ForgotCredentialException(
								CommonConstants.USER_DO_NOT_EXIST);
					}
				}
			}

		} catch (ForgotCredentialException e) {
			response.setErrorCode("400");
			response.setRequestStatus(CommonConstants.FAILURE);
			response.setErrorMessage(e.getMessage());
		} catch (Exception e) {
			response.setErrorCode("500");
			response.setRequestStatus(CommonConstants.FAILURE);
			response.setErrorMessage(e.getMessage());
		}

		try {
			commonDao = CommonOperationsDAO.getInstance();

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
		switch (((CommonAttributesDTO) obj).getRequestType()) {

		case ForgotCredentialConstants.UPDATE_PASSWORD_REQUEST_TYPE:
			requestId = insertUpdatePasswordRequest((UpdatePasswordDTO) obj);
			break;
		case ForgotCredentialConstants.FORGOT_PASSWORD_REQUEST_TYPE:
			requestId = insertForgotPasswordRequest((ForgotCredentialDTO) obj);
			break;
		}
		return requestId;
	}

	private int insertForgotPasswordRequest(ForgotCredentialDTO dto)
			throws Exception {
		int requestId = 0;
		FamedenRequestDetailBean famedenRequestDetailBean = null;
		FamedenRequestBean famedenRequestBean = null;
		if (dto != null) {
			famedenRequestBean = new FamedenRequestBean();
			famedenRequestBean.setCustomerIP(dto.getUserIPAddress());
			famedenRequestBean.setRequestStatus(CommonConstants.IN_PROCESS);
			famedenRequestBean.setRequestType(dto.getRequestType());
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

	private int insertUpdatePasswordRequest(UpdatePasswordDTO dto)
			throws Exception {
		int requestId = 0;
		FamedenRequestDetailBean famedenRequestDetailBean = null;
		FamedenRequestBean famedenRequestBean = null;
		if (dto != null) {
			famedenRequestBean = new FamedenRequestBean();
			famedenRequestBean.setCustomerIP(dto.getUserIPAddress());
			famedenRequestBean.setRequestStatus(CommonConstants.IN_PROCESS);
			famedenRequestBean.setRequestType(dto.getRequestType());
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

	private void sendForgotPasswordMailNotification(String emailAddress,
			String fullName, String code) throws NoSuchAlgorithmException,
			NoSuchPaddingException, BreachingSingletonException {

		StringTemplateGroup emailTemplateGroup = new StringTemplateGroup(
				ForgotCredentialConstants.FORGOT_PASSWORD_GROUP,
				CommonConstants.EMAIL_TEMPLATE_FOLDER_LOCATION);
		StringTemplate forgotEmail = emailTemplateGroup
				.getInstanceOf(ForgotCredentialConstants.FORGOT_PASSWORD_TEMPLATE_FILE);
		forgotEmail.setAttribute(
				CommonConstants.EMAIL_TEMPLATE_FULLNAME_PLACE_HOLDER, fullName);
		forgotEmail.setAttribute(
				ForgotCredentialConstants.EMAIL_TEMPLATE_CODE_PLACE_HOLDER,
				code);
		forgotEmail.setAttribute(
				CommonConstants.EMAIL_TEMPLATE_FROM_PLACE_HOLDER,
				CommonConstants.FAMEDEN_ORG_NAME);
		String message = forgotEmail.toString();

		logger.info(message);

		String to[] = { emailAddress };

		SendMail sm = new SendMail(
				ForgotCredentialConstants.FORGOT_PASSWORD_SUBJECT, message, to);
		sm.send();
	}
}
