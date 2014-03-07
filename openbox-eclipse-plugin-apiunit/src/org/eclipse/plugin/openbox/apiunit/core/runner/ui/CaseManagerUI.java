package org.eclipse.plugin.openbox.apiunit.core.runner.ui;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.plugin.openbox.apiunit.ApiUnitPlugin;
import org.eclipse.plugin.openbox.apiunit.core.actions.RunCasesAction;
import org.eclipse.plugin.openbox.apiunit.core.runner.CaseRunner;
import org.eclipse.plugin.openbox.apiunit.core.runner.DefaultCaseRunner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;


public class CaseManagerUI extends ApplicationWindow {
	private IJavaProject javaProject;
	private CaseRunner caseRunner;
	private TreeViewer treeView;
	private TableViewer tableView;
	public static final String[] COLUMN_NAME = TestResult.COLUMN_NAME;
	private AbstractCaseElement caseTreeRootElement;

	public CaseManagerUI(Shell parent, IJavaProject javaProject,
			AbstractCaseElement caseTreeRootElement) {
		super(parent);
		this.addMenuBar();
		this.addToolBar(SWT.FLAT);
		this.addStatusLine();
		this.javaProject = javaProject;
		this.caseRunner = new DefaultCaseRunner(this.javaProject);
		this.caseTreeRootElement = caseTreeRootElement;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Api Unit Cases Manager");
		shell.setFullScreen(false);
		setShellIcon(shell);
	}

	private void setShellIcon(Shell shell) {
		shell.setImage(ApiUnitPlugin.getImageDescriptor("openbox.png")
				.createImage());
	}

	protected Control createContents(Composite parent) {
		Composite mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new FillLayout(SWT.HORIZONTAL));
		SashForm form = new SashForm(mainPanel, SWT.HORIZONTAL | SWT.BORDER);
		form.setDragDetect(false);
		form.setTouchEnabled(false);
		form.setVisible(true);
		form.setLayout(new FillLayout(SWT.HORIZONTAL));
		// case tree
		Composite caseTreeComposite = new Composite(form, SWT.NONE);
		caseTreeComposite.setLayout(new FillLayout(SWT.HORIZONTAL
				| SWT.VERTICAL));
		treeView = new TreeViewer(caseTreeComposite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);

		tableView = new TableViewer(form, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		// case run table
		for (int i = 0; i < COLUMN_NAME.length; i++) {
			new TableColumn(tableView.getTable(), SWT.LEFT)
					.setText(COLUMN_NAME[i]);
			int width = 200;
			if (i == 0) {
				width = 200;
			} else if (i == 1) {
				width = 100;
			} else if (i == 2) {
				width = 100;
			} else if (i == 3) {
				width = 400;
			}
			tableView.getTable().getColumn(i).setWidth(width);
			tableView.getTable().getColumn(i).setMoveable(true);
//			tableView.getTable().getColumn(i).pack();
		}
		tableView.getTable().getColumn(0).setAlignment(SWT.LEFT);
		tableView.getTable().getColumn(1).setAlignment(SWT.CENTER);
		tableView.getTable().getColumn(2).setAlignment(SWT.CENTER);
		tableView.getTable().getColumn(3).setAlignment(SWT.LEFT);

		tableView.getTable().setHeaderVisible(true);
		tableView.getTable().setLinesVisible(true);
		tableView
				.setContentProvider(new CaseManagerRunnerTableContentProvider());
		tableView.setLabelProvider(new CaseManagerRunnerTableLabelProvider());
		tableView.setItemCount(0);

		// Provide the input to the ContentProvider
		treeView.setContentProvider(new CaseManagerTreeContentProvider());
		treeView.setLabelProvider(new CaseManagerTreeLabelProvider());
		treeView.setInput(caseTreeRootElement);
		treeView.expandAll();

		MenuManager menuMgr = new MenuManager("#PopupMenu");
		Menu menu = menuMgr.createContextMenu(treeView.getControl());
		treeView.getControl().setMenu(menu);
		menuMgr.add(new RunCasesAction(this));
		form.setWeights(new int[]{30,70});
		parent.layout();
		return parent;
	}

	protected MenuManager createMenuManager() {
		MenuManager menuBar = new MenuManager();

		MenuManager oMenu = new MenuManager("File(&P)");
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

	public IJavaProject getJavaProject() {
		return javaProject;
	}

	public void setJavaProject(IJavaProject javaProject) {
		this.javaProject = javaProject;
	}

	public CaseRunner getCaseRunner() {
		return caseRunner;
	}

	public void setCaseRunner(CaseRunner caseRunner) {
		this.caseRunner = caseRunner;
	}

	public AbstractCaseElement getCaseTreeRootElement() {
		return caseTreeRootElement;
	}

	public void setCaseTreeRootElement(AbstractCaseElement caseTreeRootElement) {
		this.caseTreeRootElement = caseTreeRootElement;
	}

	public TreeViewer getTreeView() {
		return treeView;
	}

	public void setTreeView(TreeViewer treeView) {
		this.treeView = treeView;
	}

	public TableViewer getTableView() {
		return tableView;
	}

	public void setTableView(TableViewer tableView) {
		this.tableView = tableView;
	}

}
