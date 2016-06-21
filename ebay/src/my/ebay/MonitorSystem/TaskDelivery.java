package my.ebay.MonitorSystem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TaskDelivery {
	// estimated handling time of each MonitorSystemMain thread
	private static double everythreadtime = 0.5;
	// keep MonitorSystemMain threads
	private static List<MonitorSystemMain> workthread = new ArrayList<MonitorSystemMain>();
	
	/**
	  * read the tasks of each target from JSON file
	  * @param path 
	  * @return String
	  * 
    */
	public String ReadTarget (String path) {
		String filecontent = "";
		BufferedReader reader = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(path);
		    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
		    reader = new BufferedReader(inputStreamReader);
		    String tempString = null;
		    while((tempString = reader.readLine()) != null) {
		    	filecontent += tempString;
		    }
	    } catch(IOException e) {
	    	Utils.printStack(e);	    	
	    } finally{
	    	if (reader != null) {
	    		try {
	    			reader.close();
	    		} catch(IOException e) {
	    			Utils.printStack(e);
	    		}
	    	}
	    }
		return filecontent;		
	}
	
	/**
	  * read the SLA time to meet clients' requirement
	  * @param path 
	  * @param level
	  * @return int
	  * 
    */
	public int ReadSLATime (String path, int level) {
		String filecontent = "";
		BufferedReader reader = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(path);
		    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
		    reader = new BufferedReader(inputStreamReader);
		    String tempString = null;
		    while((tempString = reader.readLine()) != null) {
		    	filecontent += tempString;
		    }
	    } catch(IOException e) {
	    	Utils.printStack(e);	    	
	    } finally{
	    	if (reader != null) {
	    		try {
	    			reader.close();
	    		} catch(IOException e) {
	    			Utils.printStack(e);
	    		}
	    	}
	  }
	  JSONObject slaobject = JSONObject.fromObject(filecontent);
	  // Utils.debugprint(""+slaobject.getInt("level0"));
	  // TODO: need judge the return is not null;
	  return slaobject.getInt("level"+level);				
	}
	
	public static void main (String[] args) {
		// Analyze the target list
	    String targetlistpath = "monitortarget.json";
	    TaskDelivery taskdelivery = new TaskDelivery();
	    String targetlists = taskdelivery.ReadTarget(targetlistpath);
	    JSONArray targetsArray = JSONArray.fromObject(targetlists);
	    int size = targetsArray.size();
	    Utils.debugprint("size = " + size);
	    // the number of thread I need to create
	    // default level is level2, 5min 
	    // Assume the thread do a monitor task need 500ms.
	    int slatime = taskdelivery.ReadSLATime("sla.json", 2);
	    int threadnum = (int)(size * everythreadtime /(slatime * 60)) + 1;
	    // create the thread and they wait for the task
	    Utils.debugprint("threadnum = " + threadnum);
	    for (int i = 0; i < threadnum; i++) {
	    	MonitorSystemMain worker = new MonitorSystemMain("worker thread "+ i);
	    	worker.start();
	    	workthread.add(worker);
	    }
	    
	    Iterator<MonitorSystemMain> workeriter = workthread.iterator();
	    
	    // distribute the task
	    for(int  i = 0; i < size; i++){
	    	// get target host name
	    	JSONObject task = targetsArray.getJSONObject(i);
	    	// choose an appropriate worker thread
	    	while (workeriter.hasNext()) {
	    		if (!workeriter.next().setTask(task.toString())) {
	    		  // if failed, choose next worker thead
	    		  workeriter.next();
	    		  continue;
	    		}
	    		break;
	    	}
	    	workeriter = workthread.iterator();    	
	    }
	    workeriter = workthread.iterator();
	    while(workeriter.hasNext()) {
	    	workeriter.next().setStopFlag();
	    }
	    // exit
	    Utils.debugprint("task delivery thread is exing¡­¡­");
	}
}