package org.eclipse.plugin.openbox.apiunit.core.runner.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.plugin.openbox.apiunit.core.store.CaseStoreStrategy;
import org.eclipse.plugin.openbox.apiunit.core.store.CaseStoreStrategyImpl;


public abstract class AbstractCasesTreeBuilder implements CasesTreeBuilder {

	public static final Log logger = LogFactory
			.getLog(AbstractCasesTreeBuilder.class);
	protected static final CaseStoreStrategy caseStoreStrategy = CaseStoreStrategyImpl
			.getInstance();
	
	protected abstract AbstractCaseElement buildCaseTree(AbstractCaseElement parent,
			IPath casesRootPath) throws Exception;
	
	@Override
	public AbstractCaseElement buildCaseTree() throws Exception {
		IPath casesRootPath = Path
				.fromOSString(caseStoreStrategy.getRootPath());

		return buildCaseTree(casesRootPath);
	}
	@Override
	public AbstractCaseElement buildCaseTree(IPath casesRootPath) throws Exception {
		return buildCaseTree(null,casesRootPath);
	}
}
