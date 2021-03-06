package com.fameden.webservice.contracts.useroperations;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.fameden.common.model.CommonRequestAttributes;
import com.fameden.common.model.CommonResponseAttributes;
import com.fameden.useroperations.forgotcredential.model.FameDenForgotEmailRequest;
import com.fameden.useroperations.forgotcredential.model.UpdatePasswordRequest;
import com.fameden.useroperations.login.model.FameDenLoginRequest;
import com.fameden.useroperations.login.model.FameDenLoginResponse;
import com.fameden.useroperations.registration.model.FameDenRegistrationRequest;
import com.fameden.useroperations.registration.model.FameDenRegistrationResponse;

@WebService(name = "UserOperationsService")
public interface IUserOperationsWS {

	@WebMethod(operationName = "RegisterUser")
	@WebResult(name = "RegistrationResponse")
	public FameDenRegistrationResponse registerUser(
			@WebParam(name = "RegistrationRequest") FameDenRegistrationRequest request)
			throws Exception;

	@WebMethod(operationName = "LoginUser")
	@WebResult(name = "LoginResponse")
	public FameDenLoginResponse loginUser(
			@WebParam(name = "LoginRequest") FameDenLoginRequest request)
			throws Exception;

	@WebMethod(operationName = "ForgotPassword")
	@WebResult(name = "ForgotPasswordResponse")
	public CommonResponseAttributes forgotPassword(
			@WebParam(name = "ForgotPasswordRequest") CommonRequestAttributes request)
			throws Exception;

	@WebMethod(operationName = "ForgotEmailAddress")
	@WebResult(name = "ForgotEmailAddressResponse")
	public CommonResponseAttributes forgotEmailAddress(
			@WebParam(name = "ForgotEmailAddressRequest") FameDenForgotEmailRequest request)
			throws Exception;

	@WebMethod(operationName = "UpdatePassword")
	@WebResult(name = "UpdatePasswordResponse")
	public CommonResponseAttributes updatePassword(
			@WebParam(name = "UpdatePasswordRequest") UpdatePasswordRequest request)
			throws Exception;

}