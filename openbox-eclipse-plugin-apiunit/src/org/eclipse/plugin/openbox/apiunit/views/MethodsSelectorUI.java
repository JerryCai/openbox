package org.eclipse.plugin.openbox.apiunit.views;

import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.MethodDecorator;
import org.eclipse.swt.widgets.Shell;


public class MethodsSelectorUI {

	public static PopupDialog getMethodsSelectorUI(Shell parent,
			MethodDecorator methodDecorator) {
		int shellStyle = PopupDialog.INFOPOPUP_SHELLSTYLE;
		boolean takeFocusOnOpen = true;
		boolean persistSize = false;
		boolean persistLocation = false;
		boolean showDialogMenu = false;
		boolean showPersistActions = true;
		String titleText = "Please select the one of API you want";
		String infoText = "The api have the displayed overload apis";

		// infoText = infoText +
		// methodDecorator.listAllOverloadMethods().get(0).getName();

		PopupDialog methodsSelectorUI = new PopupDialog(parent, shellStyle,
				takeFocusOnOpen, persistSize, persistLocation, showDialogMenu,
				showPersistActions, titleText, infoText);

		return methodsSelectorUI;
	}

}
