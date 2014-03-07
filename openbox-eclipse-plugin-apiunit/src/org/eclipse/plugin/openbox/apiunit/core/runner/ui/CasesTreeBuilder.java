package org.eclipse.plugin.openbox.apiunit.core.runner.ui;

import org.eclipse.core.runtime.IPath;


public interface CasesTreeBuilder {

	AbstractCaseElement buildCaseTree() throws Exception;

	AbstractCaseElement buildCaseTree(IPath casesRootPath) throws Exception;
}
