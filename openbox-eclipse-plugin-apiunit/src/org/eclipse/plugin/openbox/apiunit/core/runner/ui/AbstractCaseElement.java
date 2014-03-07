package org.eclipse.plugin.openbox.apiunit.core.runner.ui;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.plugin.openbox.apiunit.core.store.CaseStoreStrategy;
import org.eclipse.plugin.openbox.apiunit.core.store.CaseStoreStrategyImpl;


public class AbstractCaseElement implements TreeElement {

	private static final CaseStoreStrategy caseStoreStrategy = CaseStoreStrategyImpl
			.getInstance();
	private IPath path;
	private String nodeName;
	private boolean isFolder;
	private AbstractCaseElement parent;

	public AbstractCaseElement(AbstractCaseElement parent, IPath path,
			boolean isFolder) {
		this.parent = parent;
		this.path = path;
		this.isFolder = isFolder;
		hookInit();
	}

	private void hookInit() {
		nodeName = path.lastSegment().replaceAll(
				caseStoreStrategy.getStoreFileExtention(), "");
	}

	@Override
	public String getName() {
		return nodeName;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public TreeElement[] getChildren() {
		return null;
	}

	@Override
	public TreeElement getParent() {
		return parent;
	}

	@Override
	public boolean isFolder() {
		return isFolder;
	}
	
	@Override
	public List<TreeElement> listRunnableCases(){
		List<TreeElement> cases = new LinkedList<TreeElement>();
		if(!isFolder){
			cases.add(this);
		}else{
			TreeElement[] subCases =  getChildren();
			if(null == subCases){
				return cases;
			}
			for(TreeElement aCase : subCases){
				cases.addAll(aCase.listRunnableCases());
			}
		}
		return cases;
	}
	
	@Override
	public IPath getPath() {
		return path;
	}
}
