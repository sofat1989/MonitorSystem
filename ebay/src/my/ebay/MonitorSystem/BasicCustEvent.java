package my.ebay.MonitorSystem;

public class BasicCustEvent implements Runnable{
	
	protected String task = "";
	protected String hostname = "";

	/**
	  * Constructor
	  * @param hostname 
	  * @param task
	  * 
   */
	public BasicCustEvent(String hostname, String task) {
		this.hostname = hostname;
		this.task = task;
	}
	
	/**
	  * get the task in current thread
	  * @return String
	  * 
    */
	public String getTask() {
        return this.task;
    }
	
	/**
	  * get the host name of current task in current thread 
	  * @return String
	  * 
    */
	public String getHostName() {
        return this.hostname;
    }

	public String handle() {
		// TODO 自动生成的方法存根
		return null;
	}

	public void run() {
		// TODO 自动生成的方法存根
		
	}
}