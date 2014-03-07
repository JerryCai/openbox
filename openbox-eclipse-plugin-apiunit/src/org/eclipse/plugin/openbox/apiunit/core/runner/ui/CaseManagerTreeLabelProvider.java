package org.eclipse.plugin.openbox.apiunit.core.runner.ui;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.plugin.openbox.apiunit.ApiUnitPlugin;
import org.eclipse.swt.graphics.Image;


public class CaseManagerTreeLabelProvider implements ILabelProvider  {

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
	public Image getImage(Object element) {
		if(null == element){
			return ApiUnitPlugin.getCachedImage("error.gif");
		}
		if( element instanceof TreeElement){
			TreeElement caseTreelement = (TreeElement) element;
			if(caseTreelement.isFolder()){
				return ApiUnitPlugin.getCachedImage("folder.gif");
			}
			return ApiUnitPlugin.getCachedImage("file.gif");
		}
		return ApiUnitPlugin.getCachedImage("error.gif");
	}

	@Override
	public String getText(Object element) {
		if(null == element){
			return "null";
		}
		if( element instanceof TreeElement){
			TreeElement caseTreeElement = (TreeElement) element;
			return caseTreeElement.getName();
		}
		return "error";
	}
}
