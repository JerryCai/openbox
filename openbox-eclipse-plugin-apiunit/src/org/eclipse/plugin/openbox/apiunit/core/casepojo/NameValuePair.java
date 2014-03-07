package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NameValuePair extends AbstractCaseUI {

	private String name;
	private String value; 
	private Text stringValueText;
	private String typeIndex;

	public NameValuePair(String name, String typeIndex) {
		this.name = name;
		this.typeIndex = typeIndex;
		hookInitDefaultValue();
	}
	public NameValuePair(String name) {
		this.name = name;
		hookInitDefaultValue();
	}

	@Override
	protected Composite createUI(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(2, false));
		// case id
		Label nameLabel = new Label(composite, SWT.RIGHT);
		setLableValueEqualHook(nameLabel, getName());
		
		Composite valueComposite = new Composite(composite, SWT.BORDER);
		valueComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		valueComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		stringValueText = new Text(valueComposite, SWT.SINGLE | SWT.BORDER | SWT.LEFT);
		setTextValueHook(stringValueText, getValue());
		refresh();
		return composite;
	}
	
	public void createValueUI(Composite parent){
		
	}

	@Override
	public void save() {
		value = stringValueText.getText();
	}

	@Override
	public void refresh() {
//		setTextValueHook(valueText, getValue());
	}

	private void hookInitDefaultValue() {
		// TODO
		this.value = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTypeIndex() {
		return typeIndex;
	}

	public void setTypeIndex(String typeIndex) {
		this.typeIndex = typeIndex;
	}
}
