package com.googlecode.openbox.server.ssh;

public class CommandBuilder {
	
	private StringBuilder builder ;
	
	public CommandBuilder(){
		this.builder = new StringBuilder();
	}
	
	public static CommandBuilder newInstance(){
		return new CommandBuilder();
	}
	
	public void appendCommand(String command){
		builder.append(command+"\n");
	}
	
	public String toCommands(){
		return builder.toString();
	}
}
