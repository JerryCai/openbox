package org.eclipse.plugin.openbox.apiunit.core.runner.ui;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class CaseManagerRunnerTableContentProvider implements
		IStructuredContentProvider {

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
		if(inputElement instanceof TestResult){
			TestResult testResult = (TestResult)inputElement;
			Object[] line = new Object[]{testResult,testResult,testResult,testResult};
			return line;
		}
		return null;
	}

}
