package my.ebay.MonitorSystem;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MonitorSystemMain extends Thread
{
    private String name;
    private static int MAX_THREAD_NUM = 50;
    
    private boolean isStop = false;
    Queue<String> taskqueue = new LinkedList<String>();
    
    public MonitorSystemMain(String name)
    {
        this.name = name;   
    }
    
    public boolean setTask(String task) {
    	// save tasks using queue 
    	Utils.debugprint("setTask " + task);
    	return taskqueue.offer(task);
    }
    
    public void setStopFlag() {
    	isStop = true;
    }
    
    public void run()
    { 
        // wait for task
        ExecutorService es = Executors.newFixedThreadPool(MAX_THREAD_NUM);        
        
        while(true) {
        	if (isStop && (taskqueue.isEmpty())) {
        		try {
					es.awaitTermination(5, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					Utils.printStack(e);
				} 
        		Utils.debugprint(name + "is exiting");
        		break;
        	}
        	if (!(taskqueue.isEmpty())) {
        		// do the task
        		String task = taskqueue.poll();
        		Utils.debugprint("getTask " + task);
        		JSONObject taskobject = JSONObject.fromObject(task);
        		String hostIP = taskobject.getString("hostIP");
        		
    	    	// get http monitor method        		 
    	    	if (taskobject.getBoolean("http")) {
    	    		String hostPort = taskobject.getString("hostPort");
    	    		JSONArray httpmethod = taskobject.getJSONArray("httpmethod");    	    		
    	    		for (int j = 0; j < httpmethod.size(); j++) {
    	    			Utils.debugprint("httpmethod " + httpmethod.getJSONObject(j).getString("method"));
    	    			// TODO: if the method has parameters
    	    			HttpEvent hpevent = new HttpEvent("http://"+hostIP+":"+hostPort, httpmethod.getJSONObject(j).getString("method"));
    	    			es.execute(hpevent);
    	    		}   	    		
    	    	}
    	    	
    	    	// get ssh monitor method
    	    	if (taskobject.getBoolean("ssh")) {
    	    		String username = taskobject.getString("sshusername");
    	    		String passwd = taskobject.getString("sshpasswd");
    	    		JSONArray sshmethod = taskobject.getJSONArray("sshmethod");
   	    		    for (int j = 0; j < sshmethod.size(); j++) {
   	    		    	Utils.debugprint("sshmethod " + sshmethod.getJSONObject(j).getString("method"));
   	    		    	// TODO: if the method has parameters
   	    		    	SshEvent sshevent = new SshEvent(hostIP, sshmethod.getJSONObject(j).getString("method"), username, passwd);
   	    		    	es.execute(sshevent);
   	    		    }	 
    	    	}
    	    	// get ping monitor method
    	    	if (taskobject.getBoolean("ping")) {    	    		
    	    		PingEvent pingevent = new PingEvent(hostIP,"");
    	    		es.execute(pingevent);
    	    	}
        	}
        }        
    }
    
    public static void main(String[] args)
    {
        Thread thread = new MonitorSystemMain("workthread");
        thread.start();       
    }
}