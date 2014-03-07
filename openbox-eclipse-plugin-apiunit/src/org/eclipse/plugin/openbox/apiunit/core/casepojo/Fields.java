package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class Fields extends AbstractCaseUI {

	private int num;
	private List<InternField> fields;

	public Fields(int num) {
		this.num = num;
		this.fields = new ArrayList<InternField>(this.num);
	}

	@Override
	protected Composite createUI(Composite parent) {
		if (null == fields) {
			return null;
		}
		Composite composite = new Composite(parent, SWT.BORDER);
		GridLayout gridLayout = new GridLayout(1, false);
		composite.setLayout(gridLayout);
		for (int i = 0; i < fields.size(); i++) {
			InternField field = fields.get(i);
			if (null == field) {
				continue;
			}
			field.createUI(composite);
		}
		return composite;
	}

	@Override
	public void save() {
		if (null == fields) {
			return;
		}
		for (int i = 0; i < fields.size(); i++) {
			InternField field = fields.get(i);
			if (null == field) {
				continue;
			}
			field.save();
		}
	}

	@Override
	public void refresh() {
		if (null == fields) {
			return;
		}
		for (int i = 0; i < fields.size(); i++) {
			InternField field = fields.get(i);
			if (null == field) {
				continue;
			}
			field.refresh();
		}
	}

	public void setField(InternField field) {
		fields.add(field.getIndex(), field);
	}

	public InternField getField(int index) {
		return fields.get(index);
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public InternField[] getFields() {
		return fields.toArray(new InternField[0]);
	}

	public void setFields(List<InternField> fields) {
		this.fields = fields;
	}
}
