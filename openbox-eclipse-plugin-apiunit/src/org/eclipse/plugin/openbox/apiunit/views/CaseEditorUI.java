package org.eclipse.plugin.openbox.apiunit.views;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.plugin.openbox.apiunit.ApiUnitPlugin;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.CaseContent;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.MethodDecorator;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.MethodDecoratorConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


public class CaseEditorUI extends ApplicationWindow {
	private CaseContent caseContent;

	public CaseEditorUI(Shell parent, MethodDecorator methodDecorator) throws Exception {
		super(parent);
		this.addMenuBar();
		this.addToolBar(SWT.FLAT);
		this.addStatusLine();
		this.caseContent = MethodDecoratorConverter
				.toCaseContent(methodDecorator);
	}

	public CaseEditorUI(Shell parent, CaseContent caseContent) {
		super(parent);
		this.caseContent = caseContent;
		this.addMenuBar();
		this.addToolBar(SWT.FLAT);
		this.addStatusLine();
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Welcome to Api Unit Test");
		shell.setFullScreen(false);
		setShellIcon(shell);
	}

	private void setShellIcon(Shell shell) {
		shell.setImage(ApiUnitPlugin.getCachedImage("openbox.png"));
	}

	protected Control createContents(Composite parent) {
		// super.createContents(parent);
		// this.setTitle("API - " + methodDecorator.getMethodSource());
		caseContent.drawUI(parent);
		parent.layout();
		return parent;
	}

	protected MenuManager createMenuManager() {
		MenuManager menuBar = new MenuManager();

		MenuManager oMenu = new MenuManager("File(&P)");
		// MenuManager editMenu = new MenuManager("Edit(&E)");
		// MenuManager formatMenu = new MenuManager("Format(&F)");
		MenuManager helpMenu = new MenuManager("Helper(&H)");

		menuBar.add(oMenu);
		menuBar.add(helpMenu);

		return menuBar;
	}

	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBar = new ToolBarManager(style);
		return toolBar;
	}

	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		statusLineManager.setMessage("Task Status");
		return statusLineManager;

	}

	public void show() {
		setBlockOnOpen(true);
		open();
	}

	public StatusLineManager getStatusLineManager() {
		return super.getStatusLineManager();
	}
	
//    protected Point getInitialSize() {
//        return new Point(500, 375);
//    }
}
