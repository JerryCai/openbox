package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractCaseUI implements CaseUI {

	protected Composite parent;

	public Composite getParent() {
		return parent;
	}

	public void setParent(Composite parent) {
		this.parent = parent;
	}

	@Override
	public Composite drawUI(Composite parent) {
		this.parent = parent;
		createUI(this.parent);
		return parent;
	}

	protected abstract Composite createUI(Composite parent);

	protected void setTextValueHook(Text text , String value){
		if(null == value){
			text.setText("null");
		}else{
			text.setText(value);
		}
	}
	
	protected void setLableValueHook(Label label , String value){
		if(null == value){
			label.setText("null ");
		}else{
			label.setText(value +" ");
		}
	}
	
	protected void setLableValueEqualHook(Label label , String value){
		if(null == value){
			label.setText("null = ");
		}else{
			label.setText(value +" = ");
		}
	}
}
