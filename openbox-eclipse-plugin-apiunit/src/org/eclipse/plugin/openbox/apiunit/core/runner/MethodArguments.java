package org.eclipse.plugin.openbox.apiunit.core.runner;

public class MethodArguments {
	private int num;
	private MethodArgument[] args;

	public MethodArguments(int num) {
		this.num = num;
		this.args = new MethodArgument[this.num];
	}
	
	public void setMethodArgument(MethodArgument arg){
		args[arg.getIndex()] = arg;
	}
	
	public Class<?>[] getArgumentTypes(){
		Class<?>[] argumentTypes = new Class<?>[num];
		for(int i = 0 ; i < num ; i++){
			argumentTypes[i] = args[i].getClzz();
		}
		return argumentTypes;
	}
	
	public Object[] getArgumentValues(){
		Object [] argumentValues = new Object[num];
		for(int i = 0 ; i < num ; i++){
			argumentValues[i] = args[i].getValue();
		}
		return argumentValues;
	}

}
