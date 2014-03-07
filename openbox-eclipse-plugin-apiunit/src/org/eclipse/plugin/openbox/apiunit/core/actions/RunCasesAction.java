package org.eclipse.plugin.openbox.apiunit.core.actions;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.plugin.openbox.apiunit.ApiUnitPlugin;
import org.eclipse.plugin.openbox.apiunit.core.runner.ui.CaseManagerUI;
import org.eclipse.plugin.openbox.apiunit.core.runner.ui.TestResult;
import org.eclipse.plugin.openbox.apiunit.core.runner.ui.TreeElement;
import org.eclipse.plugin.openbox.apiunit.core.store.ICase;
import org.eclipse.plugin.openbox.apiunit.core.store.TestCase;
import org.eclipse.plugin.openbox.apiunit.views.UITipMsg;


public class RunCasesAction extends Action {

	private CaseManagerUI caseManagerTree;
	private TreeViewer treeView ;
	private TableViewer tableView;

	public RunCasesAction(CaseManagerUI caseManagerTree) {
		super();
		setText("click to run selected cases");
		setImageDescriptor(ApiUnitPlugin.getImageDescriptor("run.gif"));
		this.caseManagerTree = caseManagerTree;
		treeView = caseManagerTree.getTreeView();
		tableView = caseManagerTree.getTableView();
	}

	public void run() {
		TreeSelection treeSelection = (TreeSelection) treeView.getSelection();
		if (null == treeSelection || TreeSelection.EMPTY == treeSelection
				|| (treeSelection.size() <= 0)) {
			UITipMsg tipMsg = new UITipMsg(false,
					"you don't select any cases to run , Please select some cases firstly !");
			tipMsg.tip(treeView.getControl().getShell());
			return;
		}
		Object element = treeSelection.getFirstElement();
		if (element instanceof TreeElement) {
			TreeElement caseElement = (TreeElement) element;
			List<TreeElement> selectedCaseEntities = caseElement.listRunnableCases();
			for(TreeElement aCase : selectedCaseEntities){
				ICase caseEntity = new TestCase(caseManagerTree.getJavaProject().getProject()
						, aCase.getName(), aCase.getPath());
				caseEntity.read();
				try {
					TestResult testResult = caseManagerTree.getCaseRunner().run(caseEntity);
					createTableLine(testResult);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	private void createTableLine(TestResult testResult){
		tableView.add(testResult);
	}
}
