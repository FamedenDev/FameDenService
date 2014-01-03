package com.fameden.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;


public abstract class CommonOperations {
	public abstract Object convertWSRequestToDTO(Object obj,String requestType);
	
	public String getIPAddress(WebServiceContext wsContext){
		MessageContext mc = wsContext.getMessageContext();
		HttpServletRequest req = (HttpServletRequest) mc
				.get(MessageContext.SERVLET_REQUEST);
		return req.getRemoteAddr();
	}
}
