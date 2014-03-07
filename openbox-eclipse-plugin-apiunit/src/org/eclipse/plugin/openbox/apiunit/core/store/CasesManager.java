package org.eclipse.plugin.openbox.apiunit.core.store;

import java.util.List;

import org.eclipse.plugin.openbox.apiunit.core.casepojo.CaseContent;
import org.eclipse.plugin.openbox.apiunit.views.UITipMsg;


public interface CasesManager {
	
	List<ICase> listCases();
	
	int getCaseNum();
	
	String getCaseId();
	
	UITipMsg addCase(CaseContent caseContent);
	
}
