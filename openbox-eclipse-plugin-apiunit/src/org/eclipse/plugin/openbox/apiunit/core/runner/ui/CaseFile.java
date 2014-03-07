package org.eclipse.plugin.openbox.apiunit.core.runner.ui;

import org.eclipse.core.runtime.IPath;

public class CaseFile extends AbstractCaseElement {

	public CaseFile(AbstractCaseElement parent, IPath path) {
		super(parent, path, false);
	}
	
	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public TreeElement[] getChildren() {
		return null;
	}
}
