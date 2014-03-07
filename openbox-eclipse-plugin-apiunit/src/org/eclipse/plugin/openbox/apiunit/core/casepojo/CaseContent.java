package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import java.util.List;

import org.eclipse.plugin.openbox.apiunit.core.store.CasesManager;
import org.eclipse.plugin.openbox.apiunit.core.store.ICase;
import org.eclipse.plugin.openbox.apiunit.views.CaseEditorUI;
import org.eclipse.plugin.openbox.apiunit.views.UITipMsg;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class CaseContent extends AbstractCaseUI {

	private String id;
	private String name;
	private String className;
	private String methodName;
	private Text nameText;
	private String apiSourceCode;
	private String description;
	private Text descriptionText;
	private ReturnObject apiClassData;
	private ApiInputParameters apiInputParameters;
	private ApiCheckpoints apiCheckpoints;
	private CasesManager casesManager;
	private boolean isEditorStatus;
	private Composite parent;

	public CaseContent(String className , String methodName,String id, String name) {
		this.className = className;
		this.methodName = methodName;
		this.id = id;
		this.name = name;
		this.isEditorStatus = false;
	}

	private CaseContent getCurrent() {
		return this;
	}

	public Composite getCurrentParent() {
		return parent;
	}

	private Text getCurrentNameText() {
		return nameText;
	}

	private CasesManager getCurrentCasesManager() {
		return casesManager;
	}

	@Override
	protected Composite createUI(final Composite parent) {
		this.parent = parent;
		Composite mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new FillLayout(SWT.HORIZONTAL));
		SashForm form = new SashForm(mainPanel, SWT.HORIZONTAL | SWT.BORDER);
		form.setDragDetect(false);
		form.setTouchEnabled(false);
		form.setVisible(true);

		ScrolledComposite sc = new ScrolledComposite(form, SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		sc.setLayout(new FillLayout(SWT.HORIZONTAL | SWT.VERTICAL));
		Composite composite = new Composite(sc, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		composite.setLayout(gridLayout);

		final Composite caseInfoPanel = new Composite(composite, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.BEGINNING;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		caseInfoPanel.setLayoutData(gridData);
		GridLayout caseInfoLayout = new GridLayout(2, false);
		caseInfoLayout.horizontalSpacing = 10;
		caseInfoPanel.setLayout(caseInfoLayout);

		// case id
		Label doneCasesLabel = new Label(caseInfoPanel, SWT.RIGHT);
		List<ICase> casesList = casesManager.listCases();
		int size = casesList.size();
		doneCasesLabel.setText("Total Cases(" + size + "):");
		final Combo casesListCombo = new Combo(caseInfoPanel, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		if (isEditorStatus) {
			casesListCombo.setEnabled(false);
			String currentCaseName = getName();
			casesListCombo.setText(null == currentCaseName ? "null"
					: currentCaseName);
			casesListCombo.setToolTipText("you are now editing the case "
					+ getName());
		} else {
			casesListCombo.setEnabled(true);
			casesListCombo.setToolTipText("you can select " + size
					+ " done cases to edit");
		}

		String[] items = new String[size];
		casesListCombo.setTextLimit(50);

		for (int i = 0; i < size; i++) {
			ICase aCase = casesList.get(i);
			String key = aCase.getName();
			casesListCombo.add(key, i);
			casesListCombo.setData(key, aCase);
			items[i] = key;
		}
		casesListCombo.setItems(items);
		casesListCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ICase aCase = (ICase) casesListCombo.getData(casesListCombo
						.getText());
				UITipMsg uiTipMsg = aCase.read();
				uiTipMsg.tip(caseInfoPanel);
				if (uiTipMsg.isSuccess()) {
					CaseContent caseContent = aCase.getCaseContent();
					caseContent.setCasesManager(getCurrentCasesManager());
					caseContent.setEditorStatus(true);
					CaseEditorUI caseEditorUI = new CaseEditorUI(parent
							.getShell(), caseContent);
					caseEditorUI.show();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		// case name
		Label nameLabel = new Label(caseInfoPanel, SWT.RIGHT);
		nameLabel.setText("Case Name:");
		Composite editButtonHookPanel = new Composite(caseInfoPanel, SWT.NONE);
		editButtonHookPanel
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		editButtonHookPanel.setLayout(new FillLayout(SWT.HORIZONTAL));
		nameText = new Text(editButtonHookPanel, SWT.SINGLE | SWT.BORDER
				| SWT.LEFT);
		final Button useAsTempleteButton = new Button(editButtonHookPanel,
				SWT.TOGGLE);
		if (isEditorStatus()) {
			nameText.setEditable(false);
			nameText.setEnabled(false);
			useAsTempleteButton.setVisible(true);
		} else {
			useAsTempleteButton.setVisible(false);
		}

		useAsTempleteButton.setText("use as case template");
		useAsTempleteButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Text nameText = getCurrentNameText();
				nameText.setEditable(true);
				nameText.setEnabled(true);
				useAsTempleteButton.setVisible(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// setTextValueHook(nameText, getName());
		// case description
		Label descriptionLabel = new Label(caseInfoPanel, SWT.RIGHT);
		descriptionLabel.setText("Description:");
		descriptionText = new Text(caseInfoPanel, SWT.MULTI | SWT.BORDER
				| SWT.LEFT);
		descriptionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// setTextValueHook(descriptionText, getDescription());

		
		//api's corresponding constructor group
		Composite constructorPanel = new Composite(composite, SWT.NONE);
		constructorPanel.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		constructorPanel.setLayout(new FillLayout(SWT.HORIZONTAL
				| SWT.VERTICAL));

		Group constructorGroup = new Group(constructorPanel,
				SWT.SHADOW_ETCHED_OUT);
		constructorGroup.setLayout(new FillLayout(SWT.HORIZONTAL
				| SWT.VERTICAL));
		constructorGroup.setText("api corresponding class object");
		apiClassData.createUI(constructorGroup);
		
		// apiInputParameters group
		Composite apiInputParametersPanel = new Composite(composite, SWT.NONE);
		apiInputParametersPanel.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		apiInputParametersPanel.setLayout(new FillLayout(SWT.HORIZONTAL
				| SWT.VERTICAL));

		Group apiInputParametersGroup = new Group(apiInputParametersPanel,
				SWT.SHADOW_ETCHED_OUT);
		apiInputParametersGroup.setLayout(new FillLayout(SWT.HORIZONTAL
				| SWT.VERTICAL));
		apiInputParametersGroup.setText("all input parameters");
		apiInputParameters.createUI(apiInputParametersGroup);

		// apiCheckpoints group
		Composite apiCheckpointsPanel = new Composite(composite, SWT.NONE);
		apiCheckpointsPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		apiCheckpointsPanel.setLayout(new FillLayout(SWT.HORIZONTAL));

		Group apiCheckpointsGroup = new Group(apiCheckpointsPanel,
				SWT.SHADOW_ETCHED_OUT);
		apiCheckpointsGroup.setText("checkpoints");
		apiCheckpointsGroup.setLayout(new FillLayout(SWT.HORIZONTAL
				| SWT.VERTICAL));
		if (null != apiCheckpoints) {
			apiCheckpoints.createUI(apiCheckpointsGroup);
		}

		// operation button panel
		final Composite opButtonPanel = new Composite(composite, SWT.NONE);
		opButtonPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));
		opButtonPanel.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button saveButton = new Button(opButtonPanel, SWT.BUTTON1);
		saveButton.setText("save");
		saveButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				CaseContent caseContent = getCurrent();
				caseContent.save();
				UITipMsg uiTipMsg = casesManager.addCase(caseContent);
				uiTipMsg.tip(opButtonPanel);				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		refresh();
		composite.layout();
		composite.pack();
		sc.setContent(composite);

		Composite sourceCodeComposite = new Composite(form, SWT.BORDER_DOT);
		sourceCodeComposite.setLayout(new FillLayout());
		String apiSourceCode = getApiSourceCode();
		StyledText apiSourceCodeText = new StyledText(sourceCodeComposite,
				SWT.BOLD | SWT.MULTI | SWT.READ_ONLY  | SWT.H_SCROLL | SWT.V_SCROLL);
		apiSourceCodeText.setText(null == apiSourceCode ? "null"
				: apiSourceCode);
		apiSourceCodeText.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		apiSourceCodeText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_DARK_GREEN));
		form.setWeights(new int[] { 50, 50 });
		return form;
	}

	@Override
	public void save() {
		name = nameText.getText().trim();
		description = descriptionText.getText().trim();
		apiClassData.save();
		apiInputParameters.save();
		if(null != apiCheckpoints)
			apiCheckpoints.save();
	}

	@Override
	public void refresh() {
		setTextValueHook(nameText, getName());
		setTextValueHook(descriptionText, getDescription());
		apiInputParameters.refresh();
		if(null != apiCheckpoints)
			apiCheckpoints.refresh();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ApiInputParameters getApiInputParameters() {
		return apiInputParameters;
	}

	public void setApiInputParameters(ApiInputParameters apiInputParameters) {
		this.apiInputParameters = apiInputParameters;
	}

	public ApiCheckpoints getApiCheckpoints() {
		return apiCheckpoints;
	}

	public void setApiCheckpoints(ApiCheckpoints apiCheckpoints) {
		this.apiCheckpoints = apiCheckpoints;
	}

	public CasesManager getCasesManager() {
		return casesManager;
	}

	public void setCasesManager(CasesManager casesManager) {
		this.casesManager = casesManager;
		this.id = casesManager.getCaseId();
	}

	public boolean isEditorStatus() {
		return isEditorStatus;
	}

	public void setEditorStatus(boolean isEditorStatus) {
		this.isEditorStatus = isEditorStatus;
	}

	public String getApiSourceCode() {
		return apiSourceCode;
	}

	public void setApiSourceCode(String apiSourceCode) {
		this.apiSourceCode = apiSourceCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public ReturnObject getApiClassData() {
		return apiClassData;
	}

	public void setApiClassData(ReturnObject apiClassData) {
		this.apiClassData = apiClassData;
	}


}
