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
	
	public String getMemory(String hostname) {
		Utils.debugprint("getting memory of " + hostname);
		return getHttpResponse(hostname, "Memory");
	}
	
	public String getCpuInfo(String hostname) {
		Utils.debugprint("getting cpuinfo of " + hostname);
		return getHttpResponse(hostname, "CpuInfo");		
	}
	
	public String getBandWidth(String hostname) {
		Utils.debugprint("getting bandwidth of " + hostname);
		return getHttpResponse(hostname, "BandWidth");
	}
	
	public String getHttpResponse(String hostname, String querystring) {
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
			Utils.resultprint("URI " + hostname + " : URI Exception is occured");
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			Utils.resultprint("URI " + hostname + " : UnsupportedEncoding Exception is occured");
			Utils.printStack(e);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			Utils.resultprint("URI " + hostname + " : IO Exception is occured");
			Utils.printStack(e);
		} finally { 
            method.releaseConnection(); 
        }
		Utils.debugprint(res.toString());
		return res.toString();	
	}
} 