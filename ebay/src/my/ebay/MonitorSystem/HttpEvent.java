package my.ebay.MonitorSystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class HttpEvent extends BasicCustEvent{	
	 
	/**
	  * Constructor
	  * @param hostname 
	  * @param task
	  * 
   */
	public HttpEvent(String hostname, String task) {
		super(hostname, task);		
	}
	
	/**
	  * Constructor
	  * @param hostname 
	  * @param task
	  * @param params
	  * 
   */
	public HttpEvent(String hostname, String task, String[] params) {
		super(hostname, task);		
	}
	
	@Override
	public String handle() {
		return task;
		
	}

	public void run() {
		// TODO 自动生成的方法存根
		Utils.debugprint("I am a http event, and my task is " + getTask() + ", my hostname is " + getHostName());		
		Class<?> clz = null;
		BasicCustHttpMethod newinstance = null;
		String res = "";
		try {
			clz = Class.forName("my.ebay.MonitorSystem.BasicCustHttpMethod");
			newinstance = (BasicCustHttpMethod) clz.newInstance();
		} catch (ClassNotFoundException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace(System.err);
			res = "my.ebay.MonitorSystem.BasicCustHttpMethod not found";
		} catch (InstantiationException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace(System.err);
			res = "my.ebay.MonitorSystem.BasicCustHttpMethod instance is failed to create";
		} catch (IllegalAccessException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace(System.err);
			res = "my.ebay.MonitorSystem.BasicCustHttpMethod instance is failed to access";
		}		
		
		try {
			Method method = null;
			if (clz != null && newinstance != null) {
				method = clz.getMethod(getTask(), String.class);	
			}		    		
			// TODO: add the parameters except host name
			if (method != null) {
				res = method.invoke(newinstance, getHostName()).toString();				
			}			
		} catch (NoSuchMethodException e) {
			// TODO 自动生成的 catch 块
			Utils.printStack(e);
			res = getTask() + " is not found";
		} catch (SecurityException e) {
			// TODO 自动生成的 catch 块			
			Utils.printStack(e);
			res = getTask() + " is failed because of SecurityException";
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块			
			Utils.printStack(e);
			res = getTask() + " is failed because of IllegalAccessException";
		} catch (IllegalArgumentException e) {
			// TODO 自动生成的 catch 块
			Utils.printStack(e);
			res = getTask() + " is failed because of IllegalArgumentException";
		} catch (InvocationTargetException e) {
			// TODO 自动生成的 catch 块
			Utils.printStack(e);
			res = getTask() + " is failed because of InvocationTargetException";
		} finally {
			Utils.resultprint("Task " + getTask() + " of " + getHostName() + " : "+ res);
		}
		Utils.debugprint("Task " + getTask() + " of  " + getHostName() + " is finished");
	}	
}