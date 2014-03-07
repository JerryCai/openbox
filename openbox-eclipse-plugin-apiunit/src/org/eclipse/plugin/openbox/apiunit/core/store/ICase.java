package org.eclipse.plugin.openbox.apiunit.core.store;

import org.dom4j.Document;
import org.eclipse.core.runtime.IPath;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.CaseContent;
import org.eclipse.plugin.openbox.apiunit.views.UITipMsg;


public interface ICase {

	String getId();

	String getName();
	
	IPath getPath();

	boolean exists();

	CaseContent getCaseContent();

	Document getCaseEntity();

	UITipMsg save(CaseContent caseContent);

	UITipMsg update(CaseContent caseContent);

	UITipMsg delete();

	UITipMsg read();

}
