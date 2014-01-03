package com.fameden.webservice.contracts.housekeepingoperations;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.fameden.housekeeping.model.RetrieveLatestFilesRequest;
import com.fameden.housekeeping.model.RetrieveLatestFilesResponse;

@WebService(name = "HouseKeeppingOperationsService")
public interface IHouseKeepingOperationsWS {

	@WebMethod(operationName = "GetLatestFiles")
	@WebResult(name = "GetLatestFilesResponse")
	public RetrieveLatestFilesResponse getLatestFiles(
			@WebParam(name = "GetLatestFilesRequest") RetrieveLatestFilesRequest request);

}