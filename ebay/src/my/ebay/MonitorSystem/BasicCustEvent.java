package my.ebay.MonitorSystem;

public class BasicCustEvent implements Runnable{
	
	protected String task = "";
	protected String hostname = "";

	public BasicCustEvent(String hostname, String task) {
		this.hostname = hostname;
		this.task = task;
	}
	
	public String getTask() {
        return this.task;
    }
	
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