package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ApiInputParameters extends AbstractCaseUI {

	private int num;
	private ApiInputParameter[] parameters;

	public ApiInputParameters(int num) {
		Assert.isTrue(num >= 0,
				"api input parameters number (>=0)shouldn't be [" + num + "]");
		this.num = num;
		this.parameters = new ApiInputParameter[num];
	}

	@Override
	protected Composite createUI(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER_SOLID);
		composite.setLayout(new GridLayout(1, false));
		for (ApiInputParameter parameter : parameters) {
			parameter.createUI(composite);
		}
		return composite;
	}

	@Override
	public void save() {
		for (ApiInputParameter parameter : parameters) {
			parameter.save();
		}
	}

	@Override
	public void refresh() {
		for (ApiInputParameter parameter : parameters) {
			parameter.refresh();
		}
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setParameter(ApiInputParameter param) {
		parameters[param.getIndex()] = param;
	}

	public ApiInputParameter getParameter(int index) {
		return parameters[index];
	}

	public ApiInputParameter[] getParameters() {
		return parameters;
	}

	public void setParameters(ApiInputParameter[] parameters) {
		this.parameters = parameters;
	}
}
