package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import org.eclipse.swt.widgets.Composite;

public interface CaseUI {

	/*
	 * generate the related component ui
	 */
	Composite drawUI(Composite parent);

	/*
	 * save the user input
	 */
	void save();
	/*
	 * refresh the UI data from PO
	 */
	void refresh();

}
