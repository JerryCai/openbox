package org.eclipse.plugin.openbox.apiunit.core.runner.ui;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class CaseManagerTreeContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		if( inputElement instanceof TreeElement){
			TreeElement caseTreeRootElement = (TreeElement) inputElement;
			return caseTreeRootElement.getChildren();
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof TreeElement){
			TreeElement caseTreeRootElement = (TreeElement) parentElement;
			return caseTreeRootElement.getChildren();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if( element instanceof TreeElement){
			TreeElement caseTreeRootElement = (TreeElement) element;
			return caseTreeRootElement.getParent();
		}		
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if( element instanceof TreeElement){
			TreeElement caseTreeRootElement = (TreeElement) element;
			return caseTreeRootElement.hasChildren();
		}
		return false;
	}

}
