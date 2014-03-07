package org.eclipse.plugin.openbox.apiunit.core.store;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.CaseContent;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.CaseContentConverter;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.CaseEntityConverter;
import org.eclipse.plugin.openbox.apiunit.views.UITipMsg;


public class TestCase implements ICase {
	private static final Log logger = LogFactory.getLog(TestCase.class);

	private IProject project;
	// private String id;
	private String name;
	private IPath path;
	private boolean exist;
	private CaseContent caseContent;
	private Document caseEntity;

	public TestCase(IProject project, String name, IPath path) {
		this.project = project;
		this.path = path;
		this.name = name;
	}

	@Override
	public String getId() {
		return getName();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IPath getPath() {
		return path;
	}

	@Override
	public boolean exists() {
		exist = project.exists(this.getPath());
		return exist;
	}

	@Override
	public CaseContent getCaseContent() {
		return caseContent;
	}

	@Override
	public Document getCaseEntity() {
		return caseEntity;
	}

	@Override
	public UITipMsg save(CaseContent caseContent) {
		return update(caseContent);
	}

	@Override
	public UITipMsg update(CaseContent caseContent) {
		this.caseContent = caseContent;
		IPath path = this.getPath();
		String[] pathSplits = path.segments();
		IFolder folder = null;
		int i = 0;
		for (; i < path.segmentCount() - 1; i++) {
			if (i == 0) {
				folder = project.getFolder(pathSplits[i]);
			} else {
				folder = folder.getFolder(pathSplits[i]);
			}
			if (!folder.exists()) {
				try {
					folder.create(true, true, null);
				} catch (CoreException e) {
					String msg = "create the eclipse workspace parent folder for api unit encounter error !";
					UITipMsg tipMsg = new UITipMsg(false, msg + e.toString());
					logger.error(msg, e);
					return tipMsg;
				}
			}
		}
		IFile file = folder.getFile(pathSplits[i]);
		InputStream caseStream = null;
		try {
			caseStream = hookGetCaseContent();
		} catch (Exception e1) {
			String msg = "get case content error!";
			UITipMsg tipMsg = new UITipMsg(false, msg + e1.toString());
			logger.error(msg, e1);
			return tipMsg;
		}
		if (file.exists()) {
			try {
				file.setContents(caseStream, true, true, null);
			} catch (Exception e) {
				String msg = "create the eclipse workspace file for api unit encounter error !";
				UITipMsg tipMsg = new UITipMsg(false, msg + e.toString());
				logger.error(msg, e);
				return tipMsg;
			}finally{
				try {
					if(null != caseStream){
						caseStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			File systemFile = file.getLocation().toFile();
			if (systemFile.exists()) {
				systemFile.delete();
			}
			try {
				file.create(caseStream, true, null);
			} catch (Exception e) {
				String msg = "create the eclipse workspace file for api unit encounter error !";
				e.printStackTrace();
				UITipMsg tipMsg = new UITipMsg(false, msg + e.toString());
				logger.error(msg, e);
				return tipMsg;
			}finally{
				try {
					if(null != caseStream){
						caseStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		String msg = "save case successfully";
		UITipMsg tipMsg = new UITipMsg(true, msg);
		return tipMsg;
	}

	private InputStream hookGetCaseContent() throws Exception {
		return CaseContentConverter.toInputStream(getCaseContent());
	}

	@Override
	public UITipMsg delete() {
		if (!exists()) {
			String msg = "test case data on path =[" + getPath()
					+ "] can't be found under project ["
					+ project.getLocationURI() + "] !";
			logger.error(msg);
			return new UITipMsg(false, msg);
		}
		IFile file = project.getFile(getPath());
		try {
			file.delete(false, null);
		} catch (CoreException e) {
			String msg = "delete case data file on path =[" + getPath()
					+ "] under project [" + project.getLocationURI()
					+ "] failed !!!";
			logger.error(msg, e);
			return new UITipMsg(false, msg + e.toString());
		}
		return new UITipMsg(false, "delete the test case on path =["
				+ getPath() + "] success under project ["
				+ project.getLocationURI() + "]");
	}

	@Override
	public UITipMsg read() {
		if (!exists()) {
			String msg = "test case data on path =[" + getPath()
					+ "] can't be found under project ["
					+ project.getLocationURI() + "] !";
			logger.error(msg);
			return new UITipMsg(false, msg);
		}

		IFile file = project.getFile(getPath());
		InputStream content = null;
		try {
			content = file.getContents();
		} catch (CoreException e) {
			String msg = "read case data stream on path =[" + getPath()
					+ "] under project [" + project.getLocationURI()
					+ "] failed !";
			logger.error(msg, e);
			return new UITipMsg(false, msg + e.toString());
		}

		if (null == content) {
			String msg = "read case data on path =[" + getPath()
					+ "] is null under project [" + project.getLocationURI()
					+ "] failed !";
			logger.error(msg);
			return new UITipMsg(false, msg);
		}
		SAXReader xmlBuilder = new SAXReader();
		try {
			caseEntity = xmlBuilder.read(content);
		} catch (DocumentException e) {
			String msg = "Builder test case entity document from input stream failed on path =["
					+ getPath()
					+ "] is null under project ["
					+ project.getLocationURI() + "] !!!";
			logger.error(msg, e);
			return new UITipMsg(false, msg + e.toString());
		} finally {
			try {
				content.close();
			} catch (IOException e) {
				logger.error("close the test case read stream failed !", e);
			}
		}
		
		try {
			caseContent = CaseEntityConverter.toCaseContent(caseEntity);
		} catch (Exception e) {
			String msg = "Builder test case entity document from input stream failed on path =["
					+ getPath()
					+ "]under project ["
					+ project.getLocationURI() + "] !!!";
			logger.error(msg, e);
			return new UITipMsg(false, msg + e.toString());
		}
		
		return new UITipMsg(true, "read local case [" + getPath()
				+ "] under project = [" + project.getLocationURI()
				+ "]success ");
	}

}
