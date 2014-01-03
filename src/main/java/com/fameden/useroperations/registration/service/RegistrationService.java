package com.fameden.useroperations.registration.service;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Calendar;

import javax.crypto.NoSuchPaddingException;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fameden.bean.request.FamedenRequestBean;
import com.fameden.bean.request.FamedenRequestDetailBean;
import com.fameden.bean.user.FamedenUserBean;
import com.fameden.common.constants.CommonConstants;
import com.fameden.common.dao.CommonOperationsDAO;
import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.common.exception.FamedenSystemException;
import com.fameden.common.service.ICommonService;
import com.fameden.useroperations.registration.constants.RegistrationConstants;
import com.fameden.useroperations.registration.dao.RegistrationDAO;
import com.fameden.useroperations.registration.dto.RegistrationDTO;
import com.fameden.useroperations.registration.exception.RegistrationValidationException;
import com.fameden.useroperations.registration.model.FameDenRegistrationResponse;
import com.fameden.util.CommonValidations;
import com.fameden.util.encryptdecrypt.CustomEncryptionImpl;
import com.fameden.util.encryptdecrypt.RSAAlgorithmImpl;
import com.fameden.util.encryptdecrypt.SaltAlgorithmImpl;
import com.fameden.util.mail.SendMail;

public class RegistrationService implements ICommonService {

	private static RegistrationService SINGLETON;

	private Logger logger = LoggerFactory.getLogger(RegistrationService.class);

	public static RegistrationService getInstance()
			throws BreachingSingletonException {
		if (SINGLETON == null)
			SINGLETON = new RegistrationService();

		return SINGLETON;
	}

	private RegistrationService() throws BreachingSingletonException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(
					RegistrationService.class.getName());
		}
	}

	@Override
	public boolean validate(Object dto) throws Exception {
		RegistrationDTO registrationDTO = (RegistrationDTO) dto;

		boolean result = false;

		if (!CommonValidations.isStringEmpty(registrationDTO
				.getUserEmailAddress())
				&& CommonValidations.isValidEmailAddress(registrationDTO
						.getUserEmailAddress())) {
			if (!CommonValidations.isStringEmpty(registrationDTO
					.getRequestType())
					&& registrationDTO.getRequestType().trim()
							.equals(RegistrationConstants.REQUEST_TYPE)) {
				if (!CommonValidations.isStringEmpty(registrationDTO
						.getUserFullName())) {

					if (!CommonValidations.isStringEmpty(registrationDTO
							.getUserPassword())) {
						try {
							CommonOperationsDAO dao = CommonOperationsDAO
									.getInstance();
							FamedenUserBean user = dao
									.searchByEmailId(registrationDTO
											.getUserEmailAddress());
							if (user == null)
								result = true;
							else
								throw new RegistrationValidationException(
										RegistrationConstants.USER_OR_MOBILE_ALREADY_EXIST);
						} catch (Exception e) {
							if (e.getMessage().equals(
									CommonConstants.USER_DO_NOT_EXIST)) {
								result = true;
							} else {
								throw e;
							}
						}

					} else {
						throw new RegistrationValidationException(
								CommonConstants.EMPTY_PASSWORD);
					}

				} else {
					throw new RegistrationValidationException(
							RegistrationConstants.EMPTY_FULL_NAME);
				}
			} else {
				throw new RegistrationValidationException(
						CommonConstants.INVALID_REQUEST_TYPE);
			}

		} else {
			throw new RegistrationValidationException(
					CommonConstants.INVALID_EMAIL_ADDRESS);
		}

		return result;
	}

	@Override
	public Object processRequest(Object obj) {
		FameDenRegistrationResponse response = null;
		boolean validate = false;
		int requestId = 0;
		RegistrationDTO dto = (RegistrationDTO) obj;
		CommonOperationsDAO commonDao = null;
		try {
			validate = validate(dto);

			if (validate) {

				/*
				 * For every request from the user FameDen will record the
				 * request to be used in further transactions and also to be
				 * handy for future reference [Debug the issue and trace back to
				 * the user's request].
				 */
				requestId = insertRequest(dto);

				if (requestId > 0) {

					dto.setRequestId(requestId);
					SaltAlgorithmImpl saltImpl = SaltAlgorithmImpl
							.getInstance();
					RSAAlgorithmImpl rsaImpl = RSAAlgorithmImpl.getInstance();
					dto.setUserPassword(saltImpl.createHash(rsaImpl
							.decryptText(dto.getUserPassword())));

					RegistrationDAO dao = RegistrationDAO.getInstance();
					boolean result = dao.registerUser(dto);
					if (result) {
						response = new FameDenRegistrationResponse();
						response.setRequestStatus(CommonConstants.SUCCESS);

						sendMailNotification(dto.getUserEmailAddress(),
								dto.getRequestId(), dto.getUserFullName());

					} else {
						throw new FamedenSystemException(
								CommonConstants.GENERIC_EXCEPTION);
					}
				} else {
					throw new FamedenSystemException(
							CommonConstants.REQUEST_NOT_GENERATED);
				}
			}

		} catch (RegistrationValidationException e) {
			response = new FameDenRegistrationResponse();
			response.setErrorCode("400");
			response.setRequestStatus(CommonConstants.FAILURE);
			response.setErrorMessage(e.getMessage());

		} catch (Exception e) {
			response = new FameDenRegistrationResponse();
			response.setErrorCode("500");
			response.setRequestStatus(CommonConstants.FAILURE);
			response.setErrorMessage(e.getMessage());

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

		return response;
	}

	@Override
	public int insertRequest(Object obj) throws Exception {
		int requestId = 0;
		FamedenRequestDetailBean famedenRequestDetailBean = null;
		FamedenRequestBean famedenRequestBean = null;
		RegistrationDTO dto = (RegistrationDTO) obj;
		if (dto != null) {
			famedenRequestBean = new FamedenRequestBean();
			famedenRequestBean.setCustomerIP(dto.getUserIPAddress());
			famedenRequestBean.setRequestStatus(CommonConstants.IN_PROCESS);
			famedenRequestBean
					.setRequestType(RegistrationConstants.REQUEST_TYPE);
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

	private String generateVerificationLink(String emailAddress, int requestId)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			BreachingSingletonException {
		String url = null;
		CustomEncryptionImpl customEncryptImpl = CustomEncryptionImpl
				.getInstance();
		url = RegistrationConstants.VERIFICATION_PREFIX_URL
				+ customEncryptImpl.encryptNum(requestId);

		return url;
	}

	private void sendMailNotification(String emailAddress, int requestId,
			String fullName) throws NoSuchAlgorithmException,
			NoSuchPaddingException, BreachingSingletonException {
		String url = generateVerificationLink(emailAddress, requestId);

		StringTemplateGroup emailTemplateGroup = new StringTemplateGroup(
				RegistrationConstants.EMAIL_VERIFICATION_GROUP,
				CommonConstants.EMAIL_TEMPLATE_FOLDER_LOCATION);
		StringTemplate loginEmail = emailTemplateGroup
				.getInstanceOf(RegistrationConstants.EMAIL_VERIFICATION_TEMPLATE_FILE);
		loginEmail.setAttribute(
				CommonConstants.EMAIL_TEMPLATE_FULLNAME_PLACE_HOLDER, fullName);
		loginEmail.setAttribute(
				RegistrationConstants.EMAIL_TEMPLATE_URL_PLACE_HOLDER, url);
		loginEmail.setAttribute(
				CommonConstants.EMAIL_TEMPLATE_FROM_PLACE_HOLDER,
				CommonConstants.FAMEDEN_ORG_NAME);
		String message = loginEmail.toString();

		logger.info(message);

		String to[] = { emailAddress };

		SendMail sm = new SendMail(
				RegistrationConstants.EMAIL_VERIFICATION_SUBJECT, message, to);
		sm.send();
	}

}
