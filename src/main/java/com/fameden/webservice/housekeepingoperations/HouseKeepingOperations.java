package com.fameden.webservice.housekeepingoperations;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.jws.WebService;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.MTOM;

import com.fameden.common.constants.CommonConstants;
import com.fameden.housekeeping.constants.HouseKeepingConstants;
import com.fameden.housekeeping.model.RetrieveLatestFilesRequest;
import com.fameden.housekeeping.model.RetrieveLatestFilesResponse;
import com.fameden.util.CommonValidations;
import com.fameden.util.encryptdecrypt.CheckSumGeneration;
import com.fameden.webservice.contracts.housekeepingoperations.IHouseKeepingOperationsWS;

@MTOM
@WebService(endpointInterface = "com.fameden.webservice.contracts.housekeepingoperations.IHouseKeepingOperationsWS", serviceName = "HouseKeepingOperationsService", portName = "HouseKeepingOperationsPort")
public class HouseKeepingOperations implements IHouseKeepingOperationsWS {
	/*
	 * @WebMethod public void upload(String fileName, byte[] imageBytes) {
	 * 
	 * String filePath = fileName;
	 * 
	 * try { FileOutputStream fos = new FileOutputStream(filePath);
	 * BufferedOutputStream outputStream = new BufferedOutputStream(fos);
	 * outputStream.write(imageBytes); outputStream.close();
	 * 
	 * System.out.println("Received file: " + filePath);
	 * 
	 * } catch (IOException ex) { System.err.println(ex); throw new
	 * WebServiceException(ex); } }
	 */

	@Override
	public RetrieveLatestFilesResponse getLatestFiles(
			RetrieveLatestFilesRequest request) {

		RetrieveLatestFilesResponse response = new RetrieveLatestFilesResponse();

		try {
			String tncfilePath = System
					.getProperty(HouseKeepingConstants.TNC_FILE_PROPERTY_NAME);

			String tncremoteChecksum = CheckSumGeneration
					.generateChecksumOfFile(tncfilePath);

			String aboutUsfilePath = System
					.getProperty(HouseKeepingConstants.ABOUT_US_FILE_PROPERTY_NAME);

			String aboutUsremoteChecksum = CheckSumGeneration
					.generateChecksumOfFile(aboutUsfilePath);

			if (!CommonValidations.isStringEmpty(request
					.getClientTnCFileChecksum())
					&& !request.getClientTnCFileChecksum().equals(
							tncremoteChecksum)) {
				try {
					File file = new File(tncfilePath);
					FileInputStream fis = new FileInputStream(file);
					BufferedInputStream inputStream = new BufferedInputStream(
							fis);
					byte[] fileBytes = new byte[(int) file.length()];
					inputStream.read(fileBytes);
					inputStream.close();

					response.setRequestStatus(CommonConstants.SUCCESS);
					response.setRemoteTnCFile(fileBytes);

				} catch (IOException ex) {
					System.err.println(ex);
					throw new WebServiceException(ex);
				}

			}
			if (!CommonValidations.isStringEmpty(request
					.getAboutUsFileChecksum())
					&& !request.getAboutUsFileChecksum().equals(
							aboutUsremoteChecksum)) {

				try {
					File file = new File(aboutUsfilePath);
					FileInputStream fis = new FileInputStream(file);
					BufferedInputStream inputStream = new BufferedInputStream(
							fis);
					byte[] fileBytes = new byte[(int) file.length()];
					inputStream.read(fileBytes);
					inputStream.close();

					response.setRequestStatus(CommonConstants.SUCCESS);
					response.setAboutUsFile(fileBytes);

				} catch (IOException ex) {
					System.err.println(ex);
					throw new WebServiceException(ex);
				}

			}

			if ((CommonValidations.isStringEmpty(request
					.getAboutUsFileChecksum()) || request
					.getAboutUsFileChecksum().equals(aboutUsremoteChecksum))
					&& (CommonValidations.isStringEmpty(request
							.getClientTnCFileChecksum()) || request
							.getClientTnCFileChecksum().equals(
									tncremoteChecksum))) {
				response.setRequestStatus(CommonConstants.FAILURE);
			}
		} catch (Exception e) {
			response.setRequestStatus(CommonConstants.FAILURE);
		}

		return response;
	}
}
