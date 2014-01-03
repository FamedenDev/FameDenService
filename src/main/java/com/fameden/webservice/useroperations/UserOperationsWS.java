package com.fameden.webservice.useroperations;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import com.fameden.common.constants.CommonConstants;
import com.fameden.common.model.CommonRequestAttributes;
import com.fameden.common.model.CommonResponseAttributes;
import com.fameden.common.service.ICommonService;
import com.fameden.useroperations.forgotcredential.constants.ForgotCredentialConstants;
import com.fameden.useroperations.forgotcredential.dto.ForgotCredentialDTO;
import com.fameden.useroperations.forgotcredential.dto.UpdatePasswordDTO;
import com.fameden.useroperations.forgotcredential.model.FameDenForgotEmailRequest;
import com.fameden.useroperations.forgotcredential.model.UpdatePasswordRequest;
import com.fameden.useroperations.forgotcredential.service.FameDenForgotCredentialService;
import com.fameden.useroperations.login.constants.LoginConstatns;
import com.fameden.useroperations.login.dto.LoginDTO;
import com.fameden.useroperations.login.model.FameDenLoginRequest;
import com.fameden.useroperations.login.model.FameDenLoginResponse;
import com.fameden.useroperations.login.service.LoginService;
import com.fameden.useroperations.registration.constants.RegistrationConstants;
import com.fameden.useroperations.registration.dto.RegistrationDTO;
import com.fameden.useroperations.registration.model.FameDenRegistrationRequest;
import com.fameden.useroperations.registration.model.FameDenRegistrationResponse;
import com.fameden.useroperations.registration.service.RegistrationService;
import com.fameden.webservice.CommonOperations;
import com.fameden.webservice.contracts.useroperations.IUserOperationsWS;

@WebService(endpointInterface = "com.fameden.webservice.contracts.useroperations.IUserOperationsWS", serviceName = "UserOperationsService", portName = "UserOperationsPort")
public class UserOperationsWS extends CommonOperations implements
		IUserOperationsWS {

	@Resource
	WebServiceContext wsContext;

	private ICommonService service;

	public Object convertWSRequestToDTO(Object obj, String requestType) {
		Object dto = null;
		switch (requestType) {
		case RegistrationConstants.REQUEST_TYPE:
			dto = populateRegistrationDTO((FameDenRegistrationRequest) obj);
			break;
		case LoginConstatns.REQUEST_TYPE:
			dto = populateLoginDTO((FameDenLoginRequest) obj);
			break;
		case ForgotCredentialConstants.UPDATE_PASSWORD_REQUEST_TYPE:
			dto = populateUpdatePasswordDTO((UpdatePasswordRequest) obj);
			break;
		case ForgotCredentialConstants.FORGOT_PASSWORD_REQUEST_TYPE:
			dto = populateForgotPasswordDTO((CommonRequestAttributes) obj);
			break;
		}
		return dto;
	}

	private RegistrationDTO populateRegistrationDTO(
			FameDenRegistrationRequest request) {
		RegistrationDTO dto = null;
		if (request != null) {
			dto = new RegistrationDTO();
			dto.setRequestType(request.getRequestType());
			dto.setUserEmailAddress(request.getUserEmailAddress());
			dto.setUserFullName(request.getUserFullName());
			dto.setUserPassword(request.getUserPassword());

			dto.setUserIPAddress(getIPAddress(wsContext));
		}
		return dto;
	}

	private LoginDTO populateLoginDTO(FameDenLoginRequest request) {
		LoginDTO dto = null;
		if (request != null) {
			dto = new LoginDTO();
			dto.setPassword(request.getPassword());
			dto.setRequestType(request.getRequestType());
			dto.setUserEmailAddress(request.getUserEmailAddress());
			dto.setUserIPAddress(getIPAddress(wsContext));
		}
		return dto;
	}

	private ForgotCredentialDTO populateForgotPasswordDTO(
			CommonRequestAttributes request) {
		ForgotCredentialDTO dto = null;
		if (request != null) {
			dto = new ForgotCredentialDTO();
			dto.setRequestType(request.getRequestType());
			dto.setUserEmailAddress(request.getUserEmailAddress());
			dto.setUserIPAddress(getIPAddress(wsContext));
		}
		return dto;
	}

	private UpdatePasswordDTO populateUpdatePasswordDTO(
			UpdatePasswordRequest request) {
		UpdatePasswordDTO dto = null;
		if (request != null) {
			dto = new UpdatePasswordDTO();
			dto.setRequestType(request.getRequestType());
			dto.setUserEmailAddress(request.getUserEmailAddress());
			dto.setPassword(request.getPassword());
			dto.setVerificationCode(request.getVerificationCode());
			dto.setUserIPAddress(getIPAddress(wsContext));
		}
		return dto;
	}

	@Override
	public FameDenRegistrationResponse registerUser(
			FameDenRegistrationRequest request) throws Exception {
		FameDenRegistrationResponse response = null;

		if (request != null) {
			try {
				service = RegistrationService.getInstance();

				response = (FameDenRegistrationResponse) service
						.processRequest(convertWSRequestToDTO(request,
								request.getRequestType()));

				if (response == null) {
					throw new Exception();
				}
			} catch (Exception e) {
				response = new FameDenRegistrationResponse();
				response.setErrorCode("500");
				response.setRequestStatus(CommonConstants.FAILURE);
			}
		} else {
			response = new FameDenRegistrationResponse();
			response.setErrorCode("100");
			response.setRequestStatus(CommonConstants.FAILURE);
		}
		return response;
	}

	@Override
	public FameDenLoginResponse loginUser(FameDenLoginRequest request)
			throws Exception {
		FameDenLoginResponse response = null;

		if (request != null) {
			try {
				service = LoginService.getInstance();

				response = (FameDenLoginResponse) service
						.processRequest(convertWSRequestToDTO(request,
								request.getRequestType()));
				if (response == null) {
					throw new Exception();
				}

			} catch (Exception e) {
				response = new FameDenLoginResponse();
				response.setErrorCode("500");
				response.setRequestStatus(CommonConstants.FAILURE);
			}
		} else {
			response = new FameDenLoginResponse();
			response.setErrorCode("100");
			response.setRequestStatus(CommonConstants.FAILURE);
		}
		return response;
	}

	@Override
	public CommonResponseAttributes forgotPassword(
			CommonRequestAttributes request) throws Exception {
		CommonResponseAttributes response = null;
		if (request != null) {
			try {
				service = FameDenForgotCredentialService.getInstance();

				response = (CommonResponseAttributes) service
						.processRequest(convertWSRequestToDTO(request,
								request.getRequestType()));
				if (response == null) {
					throw new Exception();
				}

			} catch (Exception e) {
				response = new FameDenLoginResponse();
				response.setErrorCode("500");
				response.setRequestStatus(CommonConstants.FAILURE);
			}
		} else {
			response = new FameDenLoginResponse();
			response.setErrorCode("100");
			response.setRequestStatus(CommonConstants.FAILURE);
		}
		return response;
	}

	@Override
	public CommonResponseAttributes forgotEmailAddress(
			FameDenForgotEmailRequest request) throws Exception {
		// To be implemented
		return null;
	}

	@Override
	public CommonResponseAttributes updatePassword(UpdatePasswordRequest request)
			throws Exception {
		CommonResponseAttributes response = null;
		if (request != null) {
			try {
				service = FameDenForgotCredentialService.getInstance();

				response = (CommonResponseAttributes) service
						.processRequest(convertWSRequestToDTO(request,
								request.getRequestType()));
				if (response == null) {
					throw new Exception();
				}

			} catch (Exception e) {
				response = new FameDenLoginResponse();
				response.setErrorCode("500");
				response.setRequestStatus(CommonConstants.FAILURE);
			}
		} else {
			response = new FameDenLoginResponse();
			response.setErrorCode("100");
			response.setRequestStatus(CommonConstants.FAILURE);
		}
		return response;
	}

}
