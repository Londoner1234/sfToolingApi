package com.sf.metadata.api.Example;

import com.sf.metadata.api.Session.ToolingApiSession;
import com.sforce.soap.tooling.SoapConnection;
import com.sforce.ws.ConnectionException;


public class TestClass {

	public static void main(String[] args) throws ConnectionException {
		SoapConnection api = ToolingApiSession.connection() ; 
		
		api.getAPIPerformanceInfo() ; 
//		for (SObject record : result.getRecords()) {
//			System.out.println(record.getId());
//		}
	}
}
