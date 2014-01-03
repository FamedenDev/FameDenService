package com.fameden.useroperations.emailverification.service;

import com.fameden.common.constants.CommonConstants;
import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.common.model.CommonResponseAttributes;
import com.fameden.common.service.ICommonService;
import com.fameden.useroperations.emailverification.dao.EmailVerificationDAO;
import com.fameden.useroperations.emailverification.dto.EmailVerificationDTO;

public class EmailVerificationService implements ICommonService {

	private static EmailVerificationService SINGLETON;

	private EmailVerificationDAO dao = null;

	public static EmailVerificationService getInstance()
			throws BreachingSingletonException {
		if (SINGLETON == null)
			SINGLETON = new EmailVerificationService();

		return SINGLETON;
	}

	private EmailVerificationService() throws BreachingSingletonException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(EmailVerificationService.class.getName());
		}
	}

	@Override
	public boolean validate(Object dto) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object processRequest(Object obj) {
		EmailVerificationDTO dto = (EmailVerificationDTO) obj;
		CommonResponseAttributes response = new CommonResponseAttributes();
		try {
			dao = EmailVerificationDAO.getInstance();
			boolean result = dao.verifyEmailAddress(dto);

			if (result) {
				response.setRequestStatus(CommonConstants.SUCCESS);
				response.setErrorMessage("The email is successfully verified");
			} else {
				response.setRequestStatus(CommonConstants.FAILURE);
				response.setErrorCode("100");
				response.setErrorMessage("Request is invalid");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setRequestStatus(CommonConstants.FAILURE);
			response.setErrorCode("500");
			response.setErrorMessage("Something went wrong");
		}
		return response;
	}

	@Override
	public int insertRequest(Object dto) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
