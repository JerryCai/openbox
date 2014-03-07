package org.eclipse.plugin.openbox.apiunit.core.runner;

import org.eclipse.core.runtime.IPath;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.CaseContent;
import org.eclipse.plugin.openbox.apiunit.core.runner.ui.TestResult;
import org.eclipse.plugin.openbox.apiunit.core.store.ICase;


public interface CaseRunner {

	TestResult run(IPath path);

	TestResult run(ICase aCase);

	TestResult run(CaseContent caseContent);
}
