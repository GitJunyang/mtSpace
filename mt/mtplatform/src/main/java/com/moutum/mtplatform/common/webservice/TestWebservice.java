package com.moutum.mtplatform.common.webservice;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface TestWebservice {
	
	@WebMethod
	@WebResult(name="result")
	String testService(String msg);

}
