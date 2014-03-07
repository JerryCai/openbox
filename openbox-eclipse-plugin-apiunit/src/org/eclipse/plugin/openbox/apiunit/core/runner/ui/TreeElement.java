package org.eclipse.plugin.openbox.apiunit.core.runner.ui;

import java.util.List;

import org.eclipse.core.runtime.IPath;

public interface TreeElement {
	
	public String getName();
	
	public boolean hasChildren();
	
	public TreeElement [] getChildren();
	
	public TreeElement getParent();

	boolean isFolder();

	List<TreeElement> listRunnableCases();

	IPath getPath();
}
