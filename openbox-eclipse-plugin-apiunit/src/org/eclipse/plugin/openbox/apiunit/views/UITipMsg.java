package org.eclipse.plugin.openbox.apiunit.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class UITipMsg {
	
	public static final String TITLE = "Api Unit tool tips";
	
	private String tipMsg;
	private boolean success;

	
	public UITipMsg(boolean success ,String tipMsg){
		this.success = success;
		this.tipMsg = tipMsg;
	}
	
	public void tip(Composite parent){
		Shell shell = parent.getShell();
		if(isSuccess()){
			MessageDialog.openInformation(shell,TITLE,getTipMsg());

		}else{
			MessageDialog.openError(shell,TITLE,getTipMsg());
		}
	}
	
	public String getTipMsg() {
		return tipMsg;
	}
	public void setTipMsg(String tipMsg) {
		this.tipMsg = tipMsg;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
