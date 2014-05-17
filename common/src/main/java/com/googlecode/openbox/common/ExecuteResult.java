package com.googlecode.openbox.common;

public class ExecuteResult {
	
	private boolean result;
	private String response;
	private StringBuilder msgBuilder;
	
	public ExecuteResult(boolean result){
		this.result = result;
		this.response = null;
		this.msgBuilder = new StringBuilder();
	}
	
	public static ExecuteResult newInstance(boolean result){
		return new ExecuteResult(result);
	}
	
	public static ExecuteResult mergeExecuteResult(ExecuteResult[] results){
		ExecuteResult mergedResult = ExecuteResult.newInstance(true);
		for(int i = 0 ; i< results.length ; i++){
			ExecuteResult result = results[i];
			boolean resultValue = result.getResult();
			if(!resultValue){
				mergedResult.setResult(resultValue);
			}
			mergedResult.appendMessage(result.getResultMessage());
		}
		return mergedResult;
	}
	
	public void setResult(boolean result){
		this.result = result;
	}
	
	public boolean getResult(){
		return this.result;
	}
	
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getResultMessage(){
		return msgBuilder.toString();
	}
	
	public void appendMessage(String info){
		msgBuilder.append(info+"\n");
	}
}
