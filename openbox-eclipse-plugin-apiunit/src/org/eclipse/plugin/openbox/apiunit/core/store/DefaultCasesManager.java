package org.eclipse.plugin.openbox.apiunit.core.store;

import org.eclipse.plugin.openbox.apiunit.core.casepojo.MethodDecorator;

public class DefaultCasesManager extends AbstractCasesManagerImpl {

	public DefaultCasesManager(MethodDecorator methodDecorator) {
		super(methodDecorator);
	}

	private String covertIPathHook(String path) {
		return "/"+path.replaceAll("\\.", "\\/");
	}

	@Override
	protected String getPkgPath() {
		return covertIPathHook(getMethod().getPkg().getElementName());
	}

	@Override
	protected String getClassFolderName() {
		return getMethod().getUnit().getElementName().replaceAll(".java", "");
	}

	@Override
	protected String getMethodFolderName() {
		return getMethod().getMethodName();
	}

	@Override
	protected String getCaseFolderName() {
		return getMethod().getMethodName()
				+ getMethod().getMethod().getKey().hashCode();
	}

}
