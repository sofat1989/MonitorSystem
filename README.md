## Monitor System

### 设计思路
RPC Method  


系统的输入：  
1. 需要监控的target列表  
2. 承诺客户的SLA时间。

这两个输入，通过json格式的数据文件来实现：  
JSON(JavaScript Object Notation) 是一种轻量级的数据交换格式。易于人阅读和编写，同时也易于机器解析和生成。  	
1. monitortarget.json
  
	{  
    	"hostIP":"192.168.1.100",     # IP OR DOMAIN NAME. EG. WWW.BAIDU.COM  
    	"http": false,                # WHETHER MONITORING TARGETS VIA HTTP OR NOT 
									  # TRUE: DO ; FALSE: UNDO
    	"hostPort":"8010",            # HTTP PORT
		"httpmethod":[                # METHODS CAN BE CALLED VIA HTTP
         {"method":"getMemory"},      
         {"method":"getCpuInfo"},
         {"method":"getBandWidth"}
         ],
    	"ssh": true,                  # WHETHER MONITORING TARGETS VIA HTTP OR NOT 
									  # TRUE: DO ; FALSE: UNDO
    	"sshusername": "shkotori",    # USER NAME
    	"sshpasswd": "iloveyou52917", # PASSWORD
    	"sshmethod":[                 # COMMAND LINE CAN BE EXECUTED BY SSH
       	{"method":"cat /proc/meminfo"}, 
       	{"method":"shell getcpu.sh"},
    	],
    	"ping": false                # WHETHER MONITORING TARGETS VIA HTTP OR NOT          
									 # TRUE: DO ; FALSE: UNDO
  	}

为了适应SLA服务  
现在并发  增加并发线程。  
线程数量受控  
MonitorSystemMain
共享输入，部署在不同服务器上



MonitorSystem中将target的某种调用
event时间，处理一个event用单独的线程。
方便部署在不同的容器内，也方便添加新的方法
只需要在对应的配置文件中修改。

	ExecutorService es = Executors.newFixedThreadPool(MAX_THREAD_NUM); 
	HttpEvent hpevent = new HttpEvent("http://"+hostIP+":"+hostPort, httpmethod.getJSONObject(j).getString("method"));  
    es.execute(hpevent);
	SshEvent sshevent = new SshEvent(hostIP, sshmethod.getJSONObject(j).getString("method"), username, passwd);  
	es.execute(sshevent);
	PingEvent pingevent = new PingEvent(hostIP,"");
    es.execute(pingevent);

txt和sql，设计
	[way]:[method]:[param0 param1]
    Http:getMemory:    
	Ssh:cat /proc/memory:username password
	Ping::	

构造函数  

	public HttpEvent(String hostname, String task, String[] params) {
		super(hostname, task);		
	}
	public SshEvent(String hostname, String task, String[] params) {
		super(hostname, task);
 		user = params[0];
		passwd = params[1];	
	}
	public PingEvent(String hostname, String task, String[] params) {
		super(hostname, task);		
	}
de	
	
	String way = "Http";
	String method = "getMemory";
	String params = new String[](){};
	ExecutorService es = Executors.newFixedThreadPool(MAX_THREAD_NUM);
	Class<?> clazz = Class.forName(this.getPackageName()+way+"Event"); 
	Constructor<?> constructors = clazz.getConstructor(String.class, String class, String[].class);
	Constructor<?> constructors = clazz.getConstructor(String.class, String.class, String[].class);
	String[] params = new String[]{};
	BasicCustEvent event = (BasicCustEvent)constructors.newInstance(hostIP,method,params);
	es.execute(event);
	

	


### TODO List
	


- 用户输入的合法性检测  
任何的输入都是不可靠的，需要做验证。  
预留接口：boolean isValid(String hostIP)   (MonitorSystemMain.java)
- http和ssh监测方法的格式化输出  
目前实现的monitorsystem，仅将target返回的结果输出到console。**为了方便将结果插入数据库、或者传送给三方软件，因此在程序设计时最好将结果保存在方便格式化输出的数据结构中。**http和ssh可以获得的数据太多，不清楚用户感兴趣哪些，需要和用户沟通后设计，就像PingEvent的returnRes那样。
- ssh用户的密码加密  
目前配置文件中密码是明文，应该采用密文。
- exception的封装   
de 

deeee 


	try {  
	}
### 特性分析
- 功能扩展性：数据格式  
de
- 并发
- 模块封装：
- 预留接口：验证、扩展、数据
- 水平扩展：cdn，IP索引与工作群组分发，最快的网络响应

### 测试说明
#### 测试HTTP方法  
1. 配置monitortarget.json,根据你感兴趣的东西，选择合适的“RPC”函数  
"hostIP":"192.168.1.100"  
"http": true
"hostPort":"8010"  
"httpmethod":[        
{"method":"getMemory"},   
{"method":"getCpuInfo"},  
{"method":"getBandWidth"}  
],   
2. 运行target（eg.web server)  
为了模拟target，提供了一个简单的程序，它能够监听网络接口，并能解析get method。根据get params 返回一个固定的值。 源码在javaserver文件夹内  
Windows: 运行javaserver.exe     
Linux:   
cd javaserver  
java -cp . my.java.server.MyServer
3. 运行monitorsystem  
Windwons: 运行monitorsystem.exe  
Linux:   
#### 测试SSH方法
1. 配置monitortarget.json，根据你感兴趣的东西，添加合适的执行命令  
"ssh": true,          
"sshusername": "xxx",  
"sshpasswd": "***", 
"sshmethod":[                
{"method":"cat /proc/meminfo"},   
{"method":"shell getcpu.sh"},  
],  
   
2. 准备好需要监控的Linux主机，运行monitorsystem
这里采用Linux主机，需要开启ssh服务  
	`sudo apt-get install openssh-client`  
	`sudo apt-get install openssl-server`  
	`sudo /etc/init.d/ssh start`  

如果采用docker的话，就可以写成如下Dockerfile，制作自己的镜像: 

    # Version 0.0.1  
  	FROM ubuntu:14.04     
  	MAINTAINER sofat "sofat1989@126.com"     
  	RUN apt-get install openssh-client && apt-get install  openssh-server
	RUN /etc/init.d/ssh start  
	EXPOSE 22  

#### 测试PING方法
1. 配置monitortarget.json，运行monitorsystem  
"ping": true
  
