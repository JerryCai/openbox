package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import org.eclipse.swt.widgets.Composite;

public class ApiCheckpoint extends AbstractCaseUI {
	private int index;
	private CheckpointMode mode;
	private CheckpointApiInfo checkpointApiInfo;
	private ReturnObject returnObject;

	public ApiCheckpoint(CheckpointMode mode) {
		this.mode = mode;
	}
	
	public ApiCheckpoint(int index) {
		this.index = index;
		this.checkpointApiInfo = null;
		this.returnObject = null;
		this.mode = CheckpointMode.RETURN_OBJECT_MODE;
	}
	@Override
	protected Composite createUI(Composite parent) {
		if(null == returnObject){
			return null;
		}
		return returnObject.createUI(parent);
	}

	@Override
	public void save() {
		if(null == returnObject){
			return;
		}
		returnObject.save();
	}

	@Override
	public void refresh() {
		if(null == returnObject){
			return;
		}
		returnObject.refresh();
	}

	public CheckpointMode getMode() {
		return mode;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setMode(CheckpointMode mode) {
		this.mode = mode;
	}

	public CheckpointApiInfo getCheckpointApiInfo() {
		return checkpointApiInfo;
	}

	public void setCheckpointApiInfo(CheckpointApiInfo checkpointApiInfo) {
		this.checkpointApiInfo = checkpointApiInfo;
	}

	public ReturnObject getReturnObject() {
		return returnObject;
	}

	public void setReturnObject(ReturnObject returnObject) {
		this.returnObject = returnObject;
	}

}
