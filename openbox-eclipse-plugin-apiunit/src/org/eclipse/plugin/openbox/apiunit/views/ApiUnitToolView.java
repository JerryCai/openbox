package org.eclipse.plugin.openbox.apiunit.views;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.plugin.openbox.apiunit.ApiUnitPlugin;
import org.eclipse.plugin.openbox.apiunit.core.actions.OpenCaseMananagerAction;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.MethodDecorator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkingSet;

@SuppressWarnings("restriction")
public class ApiUnitToolView extends PackageExplorerPart {

	private TreeViewer fViewer = null;
	// private PackageExplorerActionGroup fActionSet;
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.eclipse.plugin.openbox.apiunit.views.ApiUnitToolView";

	/**
	 * The constructor.
	 */
	public ApiUnitToolView() {
		// fActionSet= new PackageExplorerActionGroup(this);

	}

	public Image getDefaultImage() {
		return ApiUnitPlugin.getCachedImage("openbox.png");
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		if (null == fViewer) {
			fViewer = super.getTreeViewer();
		}
		fViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				// fActionSet.
				handleDoubleClick(event);
			}
		});

		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(new OpenCaseMananagerAction(fViewer));

	}

	@Override
	public void dispose() {
		super.dispose();
	}

	private Shell getParentShellHook() {
		return new Shell(fViewer.getControl().getShell(), SWT.SHELL_TRIM);
	}

	void handleDoubleClick(DoubleClickEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event
				.getSelection();
		Object element = selection.getFirstElement();
		if (element instanceof IMethod) {
			IMethod method = (IMethod) element;

			MethodDecorator methodDecorator = new MethodDecorator(method);
			CaseEditorUI selector = null;
			UITipMsg tipMsg = null;
			try {
				selector = new CaseEditorUI(this.getParentShellHook(),
						methodDecorator);
			} catch (Exception e) {
				tipMsg = new UITipMsg(false, e.getMessage());
				tipMsg.tip(fViewer.getControl().getShell());
			}
			if (null == tipMsg) {
				selector.show();
			}
		}
		if (fViewer.isExpandable(element)) {
			if (doubleClickGoesInto()) {
				// don't zoom into compilation units and class files
				if (element instanceof ICompilationUnit
						|| element instanceof IClassFile) {
					return;
				}
				if (element instanceof IOpenable
						|| element instanceof IContainer
						|| element instanceof IWorkingSet) {
				}
			} else {
			}
		} else if (element instanceof IProject
				&& !((IProject) element).isOpen()) {
		}
	}

	private boolean doubleClickGoesInto() {
		return PreferenceConstants.DOUBLE_CLICK_GOES_INTO
				.equals(PreferenceConstants.getPreferenceStore().getString(
						PreferenceConstants.DOUBLE_CLICK));
	}
}