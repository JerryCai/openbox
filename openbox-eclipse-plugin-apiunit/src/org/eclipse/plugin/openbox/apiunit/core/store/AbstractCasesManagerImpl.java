package org.eclipse.plugin.openbox.apiunit.core.store;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.CaseContent;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.MethodDecorator;
import org.eclipse.plugin.openbox.apiunit.views.UITipMsg;


public abstract class AbstractCasesManagerImpl implements CasesManager {
	private static final Log logger = LogFactory
			.getLog(AbstractCasesManagerImpl.class);

	public static final CaseStoreStrategy storeStrategy = CaseStoreStrategyImpl
			.getInstance();

	private MethodDecorator methodDecorator;
	private IPath folderPath;
	private IProject project;
	private String rootPath;

	protected MethodDecorator getMethod(){
		return methodDecorator;
	}
	protected IProject getIProject(){
		return project;
	}
	
	public AbstractCasesManagerImpl(MethodDecorator methodDecorator) {
		this.methodDecorator = methodDecorator;
		hookGenerateCaseDataFilePath();
	}

	public void refresh(){
		IFolder folder = project.getFolder(Path.fromOSString(getRootPath()));
		try {
			folder.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			logger.warn("refresh the cases failed !", e);
		}
	}
	
	public String getRootPath(){
		return rootPath;
	}
	
	private void hookGenerateCaseDataFilePath() {
		project = methodDecorator.getProject().getProject();
		rootPath = "/"+storeStrategy.getRootPath();
		StringBuilder sb = new StringBuilder(rootPath);
		sb.append(getPkgPath());
		sb.append("/").append(getClassFolderName());
		sb.append("/").append(getMethodFolderName());
		sb.append("/").append(getCaseFolderName());
		folderPath = Path.fromOSString(sb.toString());
	}

	public IPath getPath() {
		return folderPath;
	}

	protected abstract String getPkgPath();

	protected abstract String getClassFolderName();

	protected abstract String getMethodFolderName();

	protected abstract String getCaseFolderName();

	@Override
	public List<ICase> listCases() {
		refresh();
		List<ICase> cases = new ArrayList<ICase>(5);
		IPath folderPath = getPath();
		IFolder folder = project.getFolder(folderPath);
		if (!folder.exists()) {
			return cases;
		}
		IResource[] caseFiles = null;
		try {
			caseFiles = folder.members(false);
		} catch (CoreException e) {
			logger.error(
					"find the test cases of api ["
							+ methodDecorator.getClassForName()
							+ "] failed !!!", e);
			return cases;
		}
		for (int i = 0; i < caseFiles.length; i++) {
			IResource caseFile = caseFiles[i];
			String extentation = "." + caseFile.getFileExtension();
			if (!storeStrategy.getStoreFileExtention().equals(extentation)) {
				continue;
			}
			String caseName = caseFile.getName().replace(extentation, "");
			cases.add(new TestCase(project, caseName, caseFile.getProjectRelativePath()));
		}
		return cases;
	}

	@Override
	public int getCaseNum() {
		return listCases().size();
	}

	@Override
	public String getCaseId() {
		return "" + (getCaseNum() + 1 + "th case");
	}

	@Override
	public UITipMsg addCase(CaseContent caseContent) {
		String caseName = caseContent.getName();
		IPath path = Path.fromOSString(getPath() + "/" + caseName
				+ storeStrategy.getStoreFileExtention());
		TestCase testCase = new TestCase(project, caseName, path);
		return testCase.update(caseContent);
	}
}
