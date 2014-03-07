package org.eclipse.plugin.openbox.apiunit.core.runner.ui;

import org.eclipse.core.runtime.IPath;

public class CaseFolder extends AbstractCaseElement {
	private int num;
	private TreeElement[] children;

	public CaseFolder(AbstractCaseElement parent, IPath path, int num) {
		super(parent, path, true);
		this.num = 0;
		children = new TreeElement[num];
	}

	@Override
	public boolean hasChildren() {
		return children.length > 0;
	}

	@Override
	public TreeElement[] getChildren() {
		return children;
	}

	public void addElement(AbstractCaseElement element) {
		children[num++] = element;
	}

	public int getNum() {
		return num;
	}
}
