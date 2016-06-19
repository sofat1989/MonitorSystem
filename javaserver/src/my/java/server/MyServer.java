package my.java.server;
import java.io.BufferedReader;
import java.io.DataInputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;
import java.io.PrintStream;  
import java.net.ServerSocket;  
import java.net.Socket;  
  
public class MyServer {  
	
    public static void main(String args[]) {  
        int PORT = 8010;  
        ServerSocket server = null;  
        Socket client = null;  
        try {  
            server = new ServerSocket(PORT);  
            System.out.println("listen:" + server.getLocalPort());  
            for (;;) {  
            	// accept client's request
                client = server.accept();
                new ConnectionThread(client).start();  
            }  
        } catch (Exception e) {  
            System.out.println(e);  
        }  
    }  
}  
  

class ConnectionThread extends Thread {  
    Socket client; 
  
    public ConnectionThread(Socket client) {  
        this.client = client;  
    }  
  
    public void run() {  
        try {  
        	// client Ip address
            String iP = client.getInetAddress().toString();
            // client port
            int port = client.getPort(); 
            String res = "";
            System.out.println("connected to " + iP + " on port " + port + ".");  
            PrintStream outstream = new PrintStream(client.getOutputStream());  
            BufferedReader instream = new BufferedReader(new InputStreamReader(client.getInputStream()));  
            String request  = instream.readLine();
            if (request != null) {
            	System.out.println("request:" + request);  
            } 
            if (request.contains("Memory")) {
            	res = "total: 3G, free : 1G, Cache: 500M";
            } else if (request.contains("BandWidth")) {
            	res = "20Mbps";
            } else if (request.contains("CpuInfo")) {
            	res = "4 Core, 20%, 20%, 20%, 10%";
            } else {
            	
            }
            
            // generate the reponse
            outstream.println("HTTP/1.0 200 OK");  
            outstream.println("MIME_version:1.0");  
            outstream.println("Content_Type:text/html");  
            outstream.println();//一定要输出一个空行  
  
            // add result  
            outstream.println(res);  
            outstream.flush();  
            client.close();  
        } catch (IOException e) {  
            System.out.println("Exception:" + e);  
        } 
    }  
} 