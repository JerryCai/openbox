package org.eclipse.plugin.openbox.apiunit.core.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.plugin.openbox.apiunit.ApiUnitPlugin;
import org.eclipse.plugin.openbox.apiunit.core.runner.ui.AbstractCaseElement;
import org.eclipse.plugin.openbox.apiunit.core.runner.ui.CaseManagerUI;
import org.eclipse.plugin.openbox.apiunit.core.runner.ui.CasesTreeBuilder;
import org.eclipse.plugin.openbox.apiunit.core.runner.ui.DefaultCasesTreeBuilder;
import org.eclipse.plugin.openbox.apiunit.views.UITipMsg;


public class OpenCaseMananagerAction extends Action {
	private TreeViewer fViewer;

	public OpenCaseMananagerAction(TreeViewer fViewer) {
		super();
		setText("Open Case Manager");
		setImageDescriptor(ApiUnitPlugin.getImageDescriptor("openbox.png"));
		this.fViewer = fViewer;
	}

	public void run() {
		TreeSelection treeSelection = (TreeSelection) fViewer.getSelection();
		if (null == treeSelection || TreeSelection.EMPTY == treeSelection
				|| (treeSelection.size() <= 0)) {
			UITipMsg tipMsg = new UITipMsg(false,
					"Please select a Java Project to open !");
			tipMsg.tip(fViewer.getControl().getShell());
			return;
		}
		Object element = treeSelection.getFirstElement();
		if (element instanceof IJavaElement) {
			IJavaElement javaElement = (IJavaElement) element;
			IJavaProject javaProject = javaElement.getJavaProject();
			IProject project = javaProject.getProject();

			CasesTreeBuilder caseTreeBuilder = new DefaultCasesTreeBuilder(
					project);
			AbstractCaseElement casesTreeRoot = null;
			try {
				casesTreeRoot = caseTreeBuilder.buildCaseTree();
			} catch (Exception e) {
				UITipMsg tipMsg = new UITipMsg(false,
						"builder cases tree failed as " + e.getMessage());
				tipMsg.tip(fViewer.getControl().getShell());
				return;
			}
			if(null == casesTreeRoot){
				UITipMsg tipMsg = new UITipMsg(true,
						"You hasn't any test cases on this project , Please check it again !");
				tipMsg.tip(fViewer.getControl().getShell());
				return;
			}
			CaseManagerUI caseManagerUI = new CaseManagerUI(fViewer
					.getControl().getShell(), javaProject ,casesTreeRoot);
			caseManagerUI.show();
		}

	}
}
