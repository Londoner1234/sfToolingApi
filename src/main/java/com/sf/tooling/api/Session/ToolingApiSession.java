
package com.sf.tooling.api.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

import com.sf.tooling.api.Properties.PropertiesFile;
import com.sforce.soap.tooling.Connector;
import com.sforce.soap.tooling.LoginResult;
import com.sforce.soap.tooling.SoapConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;



public class ToolingApiSession {
	private static SoapConnection connection;
	private static final Logger LOG = Logger.getLogger(ToolingApiSession.class.getName()) ; 
	private static  String org = "" ; 
	private static ToolingApiSession instance; 

	private static final String PROD_AUTH = "https://login.salesforce.com/services/Soap/T/41.0/"  ; 
	private static final String SANDBOX_AUTH = "https://test.salesforce.com/services/Soap/T/41.0/"  ; 

	
	private static PropertiesFile props = new PropertiesFile() ;
	

 	private ToolingApiSession() {
		org = PropertiesFile.org ;
		File apiLog = new File("./APILoggingFile.log") ;
		String endPoint = (props.getType()  == "SANDBOX" ) ? SANDBOX_AUTH : PROD_AUTH ; 
		if(apiLog.exists()) // if the file exists clear contents
		{
			try {
				Files.delete(apiLog.toPath());
			} catch (IOException e) {
				LOG.warning("Error Deleting existing API Log File !!");
			}
			
		}
	    ConnectorConfig config = new ConnectorConfig();
	    config.setUsername(props.getUsername());
	    config.setPassword(props.getPassword());
	    config.setTraceMessage(true);
	    config.setPrettyPrintXml(true);
	    config.setAuthEndpoint(endPoint);
	    
	    try {
			config.setTraceFile(apiLog.getAbsolutePath());
		} catch (FileNotFoundException e) {
			
		}


	    try {     
	    	LOG.info("API Performing Connection ! to " +org);
	    	 connection = Connector.newConnection(config);
	    	 LoginResult result = connection.login(connection.getConfig().getUsername(), connection.getConfig().getPassword()) ;
	    	 
	    	 connection.setSessionHeader(result.getSessionId());

	    	 LOG.info("API Connnected Successfully ! - Refer to Log for more information - " + apiLog.getAbsolutePath());
	      } catch (ConnectionException e1) {
	    	  LOG.warning("API Connection UnSuccessfull ! - Check properties file \nExiting Application\n" +e1 );
	    	  
	    	  System.exit(1);
	      }  
	}
	private static synchronized ToolingApiSession getInstance(){
		if(instance== null )
			instance = new ToolingApiSession() ; 
		return ToolingApiSession.instance; 	
	}
	public static synchronized SoapConnection connection(){
		return ToolingApiSession.getInstance().connection ; 
	}
}
