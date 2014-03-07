package org.eclipse.plugin.openbox.apiunit.views;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.MethodDecorator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;


public class OverloadAPISelectorDialog extends TitleAreaDialog {
	private MethodDecorator methodDecorator;
	private int selectedIndex;

	public OverloadAPISelectorDialog(Shell parentShell,
			MethodDecorator methodDecorator) {
		super(parentShell);
		this.methodDecorator = methodDecorator;
		this.selectedIndex = -1;
	}

	@Override
	protected Control createContents(Composite c) {
		super.createContents(c);
		this.getShell().setText("Select one of overload API");
		this.setTitle("There are totally have [] overload APIs");
		this.setMessage("Please select one of API which you need to test");
		// this.setTitleImage(null);
		return c;
	}

	private void setSelectIndex(int index) {
		this.selectedIndex = index;

	}

	protected Control createDialogArea(Composite c) {
		super.createDialogArea(c);
		Composite composite = new Composite(c, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		List<Method> methods = methodDecorator.listAllOverloadMethods();
		int size = methods.size();

		Group group = new Group(composite, SWT.SHADOW_OUT);
		group.setText("overload api group");
		group.setLayout(new GridLayout(1, true));
		for (int i = 0; i < size; i++) {

			Button button = new Button(group, SWT.RADIO | SWT.LEFT);
			button.setText(methodDecorator.getMethodName());
			final int index = i;
			button.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					setSelectIndex(index);
				}
			});
		}

		return c;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

}
