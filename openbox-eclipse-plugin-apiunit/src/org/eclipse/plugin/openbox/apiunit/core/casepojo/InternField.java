package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class InternField extends AbstractCaseUI {
	private String typeId;
	private int index;
	private boolean isBasicType;
	private String fieldName;
	private NameValuePair baseasicTypeReturnObject;
	private ReturnObject nonBasicTypeReturnObject;

	public InternField(String fieldName ,String typeId) {
		this.fieldName = fieldName;
		this.typeId = typeId;
		this.isBasicType = true;
		this.baseasicTypeReturnObject = null;
		this.nonBasicTypeReturnObject = null;
	}

	@Override
	protected Composite createUI(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER_DOT);
		GridLayout gridLayout = new GridLayout(2, false);
		composite.setLayout(gridLayout);

		Label typeIdLabel = new Label(composite, SWT.RIGHT);
		setLableValueHook(typeIdLabel, getTypeId());
		typeIdLabel.setLayoutData(new GridData(GridData.BEGINNING
				| GridData.FILL));
		if (isBasicType) {
			if (null != baseasicTypeReturnObject) {
				baseasicTypeReturnObject.createUI(composite);
			}
		} else {
			 Composite subComposite = new Composite(composite, SWT.BORDER);
			 subComposite.setLayoutData(new
			 GridData(GridData.FILL_HORIZONTAL));
			 subComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
			 nonBasicTypeReturnObject.createUI(subComposite);
		}
		return composite;
	}

	@Override
	public void save() {
		if (isBasicType) {
			if (null != baseasicTypeReturnObject) {
				baseasicTypeReturnObject.save();
			}
		} else {
			 nonBasicTypeReturnObject.save();
		}
	}

	@Override
	public void refresh() {
		if (isBasicType) {
			baseasicTypeReturnObject.refresh();
		} else {
			 nonBasicTypeReturnObject.refresh();
		}
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isBasicType() {
		return isBasicType;
	}

	public void setBasicType(boolean isBasicType) {
		this.isBasicType = isBasicType;
	}

	public NameValuePair getBaseasicTypeReturnObject() {
		return baseasicTypeReturnObject;
	}

	public void setBaseasicTypeReturnObject(
			NameValuePair baseasicTypeReturnObject) {
		this.baseasicTypeReturnObject = baseasicTypeReturnObject;
	}

	public ReturnObject getNonBasicTypeReturnObject() {
		return nonBasicTypeReturnObject;
	}

	public void setNonBasicTypeReturnObject(
			ReturnObject nonBasicTypeReturnObject) {
		this.nonBasicTypeReturnObject = nonBasicTypeReturnObject;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
