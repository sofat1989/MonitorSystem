package my.ebay.MonitorSystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class HttpEvent extends BasicCustEvent implements Runnable{	
	 

	public HttpEvent(String hostname, String task) {
		super(hostname, task);		
	}
	
	@Override
	public String handle() {
		return task;
		
	}

	public void run() {
		// TODO 自动生成的方法存根
		Utils.debugprint("I am is http event, and my task is " + getTask() + ", my hostname is " + getHostName());		
		Class<?> clz = null;
		BasicCustHttpMethod newinstance = null;
		String res = "";
		try {
			clz = Class.forName("my.ebay.MonitorSystem.BasicCustHttpMethod");
			newinstance = (BasicCustHttpMethod) clz.newInstance();
		} catch (ClassNotFoundException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace(System.err);
		} catch (InstantiationException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace(System.err);
		} catch (IllegalAccessException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace(System.err);
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
		} catch (SecurityException e) {
			// TODO 自动生成的 catch 块
			Utils.printStack(e);
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			Utils.printStack(e);
		} catch (IllegalArgumentException e) {
			// TODO 自动生成的 catch 块
			Utils.printStack(e);
		} catch (InvocationTargetException e) {
			// TODO 自动生成的 catch 块
			Utils.printStack(e);
		} finally {
			Utils.resultprint(res);
		}
		Utils.debugprint("http request thread is exiting");
	}	
}