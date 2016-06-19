package my.ebay.MonitorSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class SshEvent extends BasicCustEvent implements Runnable{	
	 
    private String user = "";
    private String passwd = "";
	public SshEvent(String hostname,String task, String username, String password) {
		super(hostname, task);	
		this.user = username;
		this.passwd = password;		
	}	
	
	//
	@Override
	public String handle() {
		return task;
		
	}

	public void run() {
		// TODO 自动生成的方法存根
		StringBuilder res = new StringBuilder();
		try {
			Connection conn = new Connection(hostname);
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(user, passwd);
			if (isAuthenticated == false)
				Utils.resultprint("ssh " + task + " to host name " + hostname +": Authentication failed");
			Session sess = conn.openSession();
			sess.execCommand(task);
			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
			String line = "";
            while ((line = reader.readLine()) != null) {
			   res.append(line); 
			}
            Utils.resultprint(res.toString());
			sess.close();
			conn.close();
		}catch (IOException e)
        {
			Utils.resultprint("ssh " + task + " to host name " + hostname +": Connect failed");
			Utils.printStack(e);         
        }
	}
	
}