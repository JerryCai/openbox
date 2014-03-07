package org.eclipse.plugin.openbox.apiunit.core.runner.ui;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.plugin.openbox.apiunit.ApiUnitPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


public class CaseManagerRunnerTableLabelProvider implements
		ITableLabelProvider, ITableColorProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		TestResult testResult = (TestResult)element;
		int status = testResult.getResult();
		if(status == TestResult.SUCCESS){
			return Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		}
		if(status == TestResult.FAILURE){
			return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		}
		return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		TestResult testResult = (TestResult)element;
		if(columnIndex == 2){
			int status = testResult.getResult();
			if(status == TestResult.SUCCESS){
				return ApiUnitPlugin.getCachedImage("success.gif");
			}
			if(status == TestResult.FAILURE){
				return ApiUnitPlugin.getCachedImage("failure.gif");
			}
			return ApiUnitPlugin.getCachedImage("unknown.gif");
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		TestResult testResult = (TestResult)element;
		if(columnIndex ==0){
			return testResult.getCaseName();
		}else if(columnIndex ==1){
			return testResult.getDuration()+" ms";
		}else if(columnIndex == 2){
//			return testResult.getTestResultStringExp();
			return null;
		}else if(columnIndex ==3){
			return testResult.getMessage();
		}
		return element.toString();
	}

}
