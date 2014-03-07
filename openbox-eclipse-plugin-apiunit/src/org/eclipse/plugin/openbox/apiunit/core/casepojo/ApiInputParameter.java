package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ApiInputParameter extends AbstractCaseUI {
	private int index;
	private String typeId;
	private boolean isBasicType;
	private NameValuePair baseTypeInputParameter;
	private ApiInputParameters nonBasicTypeInputParameters;
	private ReturnObject returnObject;

	public ApiInputParameter(int index) {
		this.index = index;
		this.isBasicType = true;
		this.nonBasicTypeInputParameters = null;
		this.returnObject = null;
	}

	@Override
	protected Composite createUI(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		composite.setLayout(gridLayout);

		Label typeIdLabel = new Label(composite, SWT.RIGHT);
		setLableValueHook(typeIdLabel, getTypeId());
		typeIdLabel.setLayoutData(new GridData(GridData.BEGINNING
				| GridData.FILL));
		if (isBasicType) {
			baseTypeInputParameter.createUI(composite);
		} else {
			Composite subComposite = new Composite(composite, SWT.BORDER);
			subComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			subComposite.setLayout(new GridLayout(1, false));

			nonBasicTypeInputParameters.createUI(subComposite);
			if (null != returnObject) {
				returnObject.createUI(subComposite);
			}
		}
		return composite;
	}

	@Override
	public void save() {
		if (isBasicType) {
			baseTypeInputParameter.save();
		} else {
			if(null != returnObject){
				returnObject.save();
			}
			nonBasicTypeInputParameters.save();
		}
	}

	@Override
	public void refresh() {
		if (isBasicType) {
			baseTypeInputParameter.refresh();
		} else {
			if(null != returnObject){
				returnObject.refresh();
			}
			nonBasicTypeInputParameters.refresh();
		}
	}

	public ApiInputParameter(int index, String typeId) {
		this.index = index;
		this.typeId = typeId;
		this.nonBasicTypeInputParameters = null;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public boolean isBasicType() {
		return isBasicType;
	}

	public void setBasicType(boolean isBasicType) {
		this.isBasicType = isBasicType;
	}

	public NameValuePair getBaseTypeInputParameter() {
		return baseTypeInputParameter;
	}

	public void setBaseTypeInputParameter(NameValuePair baseTypeInputParameter) {
		this.baseTypeInputParameter = baseTypeInputParameter;
	}

	public ApiInputParameters getNonBasicTypeInputParameters() {
		return nonBasicTypeInputParameters;
	}

	public void setNonBasicTypeInputParameters(
			ApiInputParameters nonBasicTypeInputParameters) {
		this.nonBasicTypeInputParameters = nonBasicTypeInputParameters;
	}

	public ReturnObject getReturnObject() {
		return returnObject;
	}

	public void setReturnObject(ReturnObject returnObject) {
		this.returnObject = returnObject;
	}
}
