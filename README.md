## Monitor System
### 设计思路
配置文件（）：
### TODO List
- 用户输入的合法性检测
- 格式化http和ssh监测方法的输出
- ssh明文密码
### 特性分析
- 功能扩展性：数据格式
- 并发
- 模块封装：
- 预留接口
- 水平扩展：cdn，IP索引与工作群组分发，最快的网络响应
### 测试说明

`# Version 0.0.1`
`FROM ubuntu:14.04`     

    # Version 0.0.1  
  	FROM ubuntu:14.04     
  	MAINTAINER sofat "sofat1989@126.com"     
  	RUN apt-get install openssh-client && apt-get install  openssh-server
	RUN /etc/init.d/ssh start  
	EXPOSE 22

