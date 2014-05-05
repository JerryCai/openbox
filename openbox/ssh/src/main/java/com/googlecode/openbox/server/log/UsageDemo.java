package com.googlecode.openbox.server.log;

import com.googlecode.openbox.server.Server;

public class UsageDemo {
	
	public static void go(){
		String ip = "10.224.58.168";
		int sshPort = 333;
		String username="root";
		String password="tahoe.webex";
		
		String home="/opt/webex/jtel/logs";
		String name= "dtlog_2012_11_26_01_30_49.243";
		
		Server server = Server.newInstance(ip, sshPort, username, password);
		ServerLog serverLog = ServerLogImpl.newInstance(server, home, name);
		System.out.println(serverLog.getCurrentLineNum());
//		System.out.println(serverLog.getLastestContent(500));
		System.out.println(serverLog.getContentBetween(1, 100));
		serverLog.getContentByCommand("grep 'xxxxx'");
	}

	public static void main(String ...args){
		go();
	}
}
