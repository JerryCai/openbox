package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ApiCheckpoints extends AbstractCaseUI {
	private List<ApiCheckpoint> checkpoints;

	public ApiCheckpoints(){
		checkpoints = new ArrayList<ApiCheckpoint>(1);
	}
	
	@Override
	protected Composite createUI(Composite parent) {
		Composite composite = new Composite(parent,SWT.BORDER_SOLID);
		composite.setLayout(new GridLayout(1, false));
		if(checkpoints.size()<=0){
			return null;
		}
		ApiCheckpoint checkpoint = checkpoints.get(0);
		if(null == checkpoint){
			return null;
		}
		checkpoint.createUI(composite);
		return composite;
	}
	
	@Override
	public void save() {
		if(checkpoints.size()<=0){
			return;
		}
		ApiCheckpoint checkpoint = checkpoints.get(0);
		if(null == checkpoint){
			return;
		}
		checkpoint.save();
	}

	@Override
	public void refresh() {
		if(checkpoints.size()<=0){
			return;
		}
		ApiCheckpoint checkpoint = checkpoints.get(0);
		if(null == checkpoint){
			return;
		}
		checkpoint.refresh();	
	}

	public ApiCheckpoint getCheckpoint(int index) {
		if (null == checkpoints)
			return null;
		for (ApiCheckpoint checkpoint : checkpoints)
			if (index == checkpoint.getIndex())
				return checkpoint;

		return null;
	}
	
	public void removeCheckpoint(int index){
		if (null == checkpoints)
			return ;
		for (ApiCheckpoint checkpoint : checkpoints)
			if (index == checkpoint.getIndex()){
				checkpoints.remove(checkpoint);
				break;
			}
	}

	public void appendApiCheckpoint(ApiCheckpoint checkpoint) {
		if (null == checkpoints)
			checkpoints = new LinkedList<ApiCheckpoint>();
		checkpoints.add(checkpoint);
	}

	public int getApiCheckpointsNum() {
		if (null == checkpoints)
			return 0;
		return checkpoints.size();
	}

	public List<ApiCheckpoint> getCheckpoints() {
		return checkpoints;
	}

	public void setCheckpoints(List<ApiCheckpoint> checkpoints) {
		this.checkpoints = checkpoints;
	}
}
