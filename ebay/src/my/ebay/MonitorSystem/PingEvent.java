package my.ebay.MonitorSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PingEvent extends BasicCustEvent implements Runnable{
	 
    public static int pingtimes = 3;
    public static int timeout = 3000;
    
    public class returnRes {
    	public int successTimes;
    	public int unreachableTimes;
    	public int timeoutTimes;
    	
    	public int latency;
    	
    	public returnRes(int suc,int unreach, int timeout ,int lat) {
    		this.successTimes = suc;
    		this.latency = lat;
    		this.unreachableTimes = unreach;
    		this.timeoutTimes = timeout;
    	}
    	
    	public String toString() {
			return "success times: " + successTimes +", unreachable times: " + unreachableTimes +", timeout times: " + timeoutTimes +", Average latency: "+ ((successTimes != 0)?(int)(latency/successTimes):-1);    		
    	}

		public void update(returnRes everyLineRes) {
			// TODO 自动生成的方法存根
			successTimes += everyLineRes.successTimes;
			latency += everyLineRes.latency;
			unreachableTimes += everyLineRes.unreachableTimes;
			timeoutTimes += everyLineRes.timeoutTimes;
		}
		
		public void update(int suc, int unreach, int timeout, int lat) {
			successTimes += suc;
			latency += lat;
			unreachableTimes += unreach;
			timeoutTimes += timeout;
		}
    }
	public PingEvent(String hostname, String task) {
		super(hostname, task);		
	}	
		
	@Override
	public String handle() {
		return task;
		
	}

	public String getPingResult() {
		returnRes res = new returnRes(0,0,0,0);
		BufferedReader in = null; 
		Runtime r = Runtime.getRuntime(); 
		String pingCommand = "ping " + hostname + " -n " + pingtimes    + " -w " + timeout; 
		try {
			Process p = r.exec(pingCommand);   
			if (p == null) { 
			   return res.toString(); 
			}
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));			 
			String line = null;  
			while ((line = in.readLine()) != null) {
				res.update(getEveryLineRes(line));
			}
		} catch (Exception ex) {
			Utils.printStack(ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Utils.printStack(e);
				}
			}
		}
		return res.toString();
	}
	
	private returnRes getEveryLineRes(String line) {
		// TODO 自动生成的方法存根
		returnRes res = new returnRes(0,0,0,0);
		int latencytemp = 0;
		int succtemp = 0;
		int timeouttemp = 0;
		int unreachtemp = 0;
		Pattern succ_pattern = Pattern.compile("(<|=)(\\d+)ms(\\s+)(TTL=\\d+)",  Pattern.CASE_INSENSITIVE); 
		Matcher succ_matcher = succ_pattern.matcher(line); 
		while (succ_matcher.find()) { 			
			 succtemp++;
			 if(succ_matcher.group(1).equals("=")) {				 
				 latencytemp += Integer.parseInt(succ_matcher.group(2));	
			 } 
		}
		Utils.debugprint(line);
		Pattern unreach_pattern = Pattern.compile("(Destination Host Unreachable)|(\u76ee\u6807\u4e3b\u673a\u65e0\u6cd5\u8bbf\u95ee)",  Pattern.CASE_INSENSITIVE); 
		Matcher unrech_matcher = unreach_pattern.matcher(line); 
		while (unrech_matcher.find()) { 
			Utils.debugprint("matchresult unreach" + unrech_matcher.group(1));
			unreachtemp++;
		}
		
		// timeout_pattern = Pattern.compile(new String("\u8bf7\u6c42\u8d85\u65f6".getBytes("UTF-8"), "UTF-8"), Pattern.CASE_INSENSITIVE);
		Pattern timeout_pattern = Pattern.compile("(Request timed out)|(\u8bf7\u6c42\u8d85\u65f6)", Pattern.CASE_INSENSITIVE);
		Matcher timeout_matcher = timeout_pattern.matcher(line);
		while (timeout_matcher.find()) { 
			Utils.debugprint("matchresult timeout" + timeout_matcher.group(1));
			timeouttemp++;
		}
		
		res.update(succtemp, unreachtemp, timeouttemp, latencytemp);
		return res;
	}

	public void run() {
		// TODO 自动生成的方法存根
		Utils.debugprint("I am is ping event");
		String res = getPingResult();
		Utils.resultprint(res);		
	}
}