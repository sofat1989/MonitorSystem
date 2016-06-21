@echo off
set classpath=%classpath%;%~dp0lib\commons-beanutils-1.8.0.jar;%~dp0lib\commons-lang-2.5.jar;%~dp0lib\commons-logging-1.1.1.jar;%~dp0lib\commons-collections-3.2.1.jar;%~dp0lib\ezmorph-1.0.6.jar;%~dp0lib\json-lib-2.4-jdk15.jar;%~dp0bin\MonitorSystem.jar;%~dp0lib\commons-codec-1.4.jar;%~dp0lib\commons-httpclient-3.0.1.jar;%~dp0lib\commons-httpclient-3.1.jar;%~dp0lib\commons-httpclient-3.1-rc1.jar;%~dp0lib\ganymed-ssh2-build210.jar;
java my.ebay.MonitorSystem.TaskDelivery
pause