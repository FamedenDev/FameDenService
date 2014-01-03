package com.fameden.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.common.model.CommonResponseAttributes;
import com.fameden.useroperations.emailverification.dto.EmailVerificationDTO;
import com.fameden.useroperations.emailverification.service.EmailVerificationService;
import com.fameden.util.encryptdecrypt.CustomEncryptionImpl;

/**
 * Servlet implementation class EmailAddressVerification
 */
@WebServlet("/EmailAddressVerification")
public class EmailAddressVerification extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private EmailVerificationService service = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EmailAddressVerification() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String emailAddressVerificationCode = request
				.getParameter("emailAddressVerificationCode");
		try {
			service = EmailVerificationService.getInstance();
			CustomEncryptionImpl customEncryptionImpl = CustomEncryptionImpl
					.getInstance();

			int requestId = customEncryptionImpl
					.decryptNum(emailAddressVerificationCode);

			CommonResponseAttributes resp = (CommonResponseAttributes) service
					.processRequest(buildDTO(requestId));
			PrintWriter out = response.getWriter();
			out.println("<h1>" + resp.getRequestStatus() + "</h1>");
			out.println("<h1>" + resp.getErrorCode() + "</h1>");
			out.println("<h1>" + resp.getErrorMessage() + "</h1>");

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BreachingSingletonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private EmailVerificationDTO buildDTO(int requestId) {
		EmailVerificationDTO dto = new EmailVerificationDTO();
		dto.setRequestId(requestId);
		return dto;
	}

}
