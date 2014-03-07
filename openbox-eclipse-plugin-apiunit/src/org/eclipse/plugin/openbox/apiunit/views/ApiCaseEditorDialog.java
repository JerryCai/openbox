package org.eclipse.plugin.openbox.apiunit.views;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.CaseContent;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.MethodDecorator;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.MethodDecoratorConverter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


public class ApiCaseEditorDialog extends TitleAreaDialog {

	public static final int ID_SAVE = IDialogConstants.OK_ID;
	public static final int ID_CANCEL = IDialogConstants.CANCEL_ID;
	public static final int ID_RESET = IDialogConstants.BACK_ID;

	public static final String LABEL_SAVE = "Save";
	public static final String LABEL_CANCEL = "Cancel";
	public static final String LABEL_RESET = "Reset";

	private MethodDecorator methodDecorator;
	private int selectedIndex;

	public ApiCaseEditorDialog(Shell parentShell,
			MethodDecorator methodDecorator) {
		super(parentShell);
		this.methodDecorator = methodDecorator;
		this.selectedIndex = -1;
	}

	@Override
	protected Control createContents(Composite c) {
		super.createContents(c);
		this.getShell().setText("API Unit Test Editor");
		this.setTitle("API - " + methodDecorator.getMethodSource());
		this.setMessage("Please set your input paramete values and checkpoints below correctly ");
		return c;
	}

	public void setSelectIndex(int index) {
		this.selectedIndex = index;

	}
	
	public int getSelectedIndex(){
		return this.selectedIndex;
	}

	protected Control createDialogArea(Composite c) {
		super.createDialogArea(c);
		CaseContent caseContent = null;
		try {
			caseContent = MethodDecoratorConverter
					.toCaseContent(methodDecorator);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return caseContent.drawUI(c);
	}

	protected void createButtonsForButtonBar(Composite parent) {

		createButton(parent, ID_SAVE, LABEL_SAVE, true);
		createButton(parent, ID_CANCEL, LABEL_CANCEL, false);
		createButton(parent, ID_RESET, LABEL_RESET, false);

	}

}
