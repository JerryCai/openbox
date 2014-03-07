package org.eclipse.plugin.openbox.apiunit.core.runner.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class DefaultCasesTreeBuilder extends AbstractCasesTreeBuilder {
	public static final Log logger = LogFactory
			.getLog(DefaultCasesTreeBuilder.class);

	private IProject project;

	public DefaultCasesTreeBuilder(IProject project) {
		this.project = project;
	}

	@Override
	public AbstractCaseElement buildCaseTree(AbstractCaseElement parent,
			IPath casesRootPath) throws Exception {
		if (null == casesRootPath)
			return null;

		if (isCaseFile(casesRootPath)) {
			IFile file = project.getFile(casesRootPath);
			refreshHook(file);
			if (!file.exists())
				return null;

			CaseFile caseFile = new CaseFile(parent, casesRootPath);
			return caseFile;
		}

		IFolder folder = project.getFolder(casesRootPath);
		refreshHook(folder);
		if (!folder.exists())
			return null;

		IResource[] resources;
		try {
			resources = folder.members();
		} catch (CoreException e) {
			throw new Exception("list case resource failed !", e);
		}
		int num = resources.length;
		CaseFolder caseFolder = new CaseFolder(parent, casesRootPath, num);
		for (int i = 0; i < num; i++) {
			IResource resource = resources[i];
			IPath path = resource.getProjectRelativePath();
			caseFolder.addElement(buildCaseTree(caseFolder, path));
		}
		return caseFolder;
	}

	private boolean isCaseFile(IPath casesRootPath) {
		return casesRootPath.lastSegment().endsWith(
				caseStoreStrategy.getStoreFileExtention());
	}

	private void refreshHook(IResource resource) {
		try {
			resource.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			logger.warn("refresh the cases resources failed !", e);
		}
	}
}
