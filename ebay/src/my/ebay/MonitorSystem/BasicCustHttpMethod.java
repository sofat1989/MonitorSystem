package my.ebay.MonitorSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;

public class BasicCustHttpMethod {
	
	/**
	  * get the memory state of target
	  * @param hostname 
	  * @return String
	  * 
    */
	public String getMemory(String hostname) {
		// Utils.debugprint("getting memory of " + hostname);
		return sendAndRecv(hostname, "Memory");
	}
	
	/**
	  * get the CPU information of target
	  * @param hostname 
	  * @return String
	  * 
    */
	public String getCpuInfo(String hostname) {
		// Utils.debugprint("getting cpuinfo of " + hostname);
		return sendAndRecv(hostname, "CpuInfo");		
	}
	
	/**
	  * get the band width of target
	  * @param hostname 
	  * @return String
	  * 
    */
	public String getBandWidth(String hostname) {
		// Utils.debugprint("getting bandwidth of " + hostname);
		return sendAndRecv(hostname, "BandWidth");
	}
	
	/**
	  * send a GET-METHOD request and receive the response
	  * @param hostname 
	  * @param querystring 
	  * @return String
	  * 
    */
	public String sendAndRecv(String hostname, String querystring) {
		StringBuffer res = new StringBuffer();
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
		client.getHttpConnectionManager().getParams().setSoTimeout(3000);
		HttpMethod method = new GetMethod(hostname);
		method.setQueryString(querystring+"=");
		try {
			client.executeMethod(method); 
			if (method.getStatusCode() == HttpStatus.SC_OK) {
		       BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "utf-8"));
			   String line = "";
			   while ((line = reader.readLine()) != null) {
				   res.append(line); 
			   }
			  reader.close();
		    } else {
		    	Utils.resultprint("Method get" + querystring + " to Host " + hostname + " is failed");
		    }
		} catch (URIException e) { 
			// URI is not legal
			// Utils.resultprint("URI " + hostname + " : URI Exception is occured");
			res.append("URI Exception is occured");
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			// Utils.resultprint("URI " + hostname + " : UnsupportedEncoding Exception is occured");
			Utils.printStack(e);
			res.append("UnsupportedEncoding Exception is occured");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			// Utils.resultprint("URI " + hostname + " : IO Exception is occured");
			Utils.printStack(e);
			res.append("IO Exception is occured");
		} finally { 
            method.releaseConnection(); 
        }		
		return res.toString();	
	}
} 