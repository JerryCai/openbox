package com.googlecode.openbox.config;

interface DemoConfig{
	
	String getHostIp();
	
	int getThreadNumber();
	
	boolean isGsb();
}

class DemoConfigImpl implements DemoConfig{
	//Usage just link this :
	private static final String CONFIG_PATH = "conf/demo_config.properties";
	private static final ConfigLoader CL= ConfigLoader.newInstance(CONFIG_PATH);
	

	@Override
	public String getHostIp() {
		return CL.getConfigItem("host.ip");
	}

	@Override
	public int getThreadNumber() {
		return Integer.parseInt(CL.getConfigItem("thread.number"));
	}

	@Override
	public boolean isGsb() {
		return Boolean.parseBoolean(CL.getConfigItem("is.gsb"));
	}

}


public class UsageDemo{
	
	public static void main(String  ...args){
		DemoConfig demoConfig = new DemoConfigImpl();
		System.out.println(demoConfig.getHostIp());
		System.out.println(demoConfig.getThreadNumber());
		System.out.println(demoConfig.isGsb());
	}
	
}