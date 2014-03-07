package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class ReturnObject extends AbstractCaseUI {
	
	public static final String VOID_RETURN_TYPE_ID = "void";
	private String typeId;
	private ApiInputParameter constructor;
	private Fields fields;
	
	public ReturnObject(String typeId){
		this.typeId = typeId;
		this.fields = null;
	}
	@Override
	protected Composite createUI(Composite parent) {
		if(null == fields){
			return null;
		}
		if(fields.getNum()<=0){
			return null;
		}
		Group group = new Group(parent,
				SWT.SHADOW_ETCHED_OUT);
		String groupName = null == getTypeId()? "null": getTypeId();
		group.setText(groupName);
		group.setLayout(new GridLayout(1, false));
		if(null != constructor)
			constructor.createUI(group);
		fields.createUI(group);
		return group;
	}
	
	@Override
	public void save() {
		if(null != constructor){
			constructor.save();
		}
		if(null == fields){
			return;
		}
		if(fields.getNum()<=0){
			return;
		}		
		fields.save();
	}
	
	@Override
	public void refresh() {
		if(null == fields){
			return;
		}
		if(fields.getNum()<=0){
			return;
		}		
		fields.refresh();	
	}

	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public Fields getFields() {
		return fields;
	}
	public void setFields(Fields fields) {
		this.fields = fields;
	}
	public ApiInputParameter getConstructor() {
		return constructor;
	}
	public void setConstructor(ApiInputParameter constructor) {
		this.constructor = constructor;
	}
}
