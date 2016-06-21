package my.ebay.MonitorSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class SshEvent extends BasicCustEvent{	
	 
    private String user = "";
    private String passwd = "";
    
    /**
	  * Constructor
	  * @param hostname 
	  * @param task
	  * @param username
	  * @param password
	  * 
   */
	public SshEvent(String hostname,String task, String username, String password) {
		super(hostname, task);	
		this.user = username;
		this.passwd = password;		
	}	
	
	/**
	  * Constructor
	  * @param hostname 
	  * @param task
	  * @param params
	  * 
   */
	public SshEvent(String hostname,String task, String[] params) {
		super(hostname, task);	
		this.user = params[0];
		this.passwd = params[1];		
	}
	
	//
	@Override
	public String handle() {
		return task;
		
	}

	public void run() {
		// TODO 自动生成的方法存根
		Utils.debugprint("I am a ssh event, and my task is " + getTask() + ", my hostname is " + getHostName());
		StringBuilder res = new StringBuilder();
		try {
			Connection conn = new Connection(hostname);
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(user, passwd);
			if (isAuthenticated) {
				Session sess = conn.openSession();
				sess.execCommand(task);
				InputStream stdout = new StreamGobbler(sess.getStdout());
				BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
				String line = "";
	            while ((line = reader.readLine()) != null) {
				   res.append(line); 
				}
	            reader.close();
				sess.close();
			}			
			conn.close();
		}catch (IOException e)
        {			
			Utils.printStack(e);
			res.append("Connect failed because of IOException");
        } finally {
        	Utils.resultprint("Task " + getTask() + " of " + getHostName() + " : "+ res.toString());
        }
		Utils.debugprint("Task " + getTask() + " of " + getHostName() + " is finished");
	}
	
}