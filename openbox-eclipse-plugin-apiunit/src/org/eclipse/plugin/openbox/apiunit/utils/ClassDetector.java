package org.eclipse.plugin.openbox.apiunit.utils;

import java.io.File;

public class ClassDetector {

	private String projectPath;
	private String className;

	private String classDataRootPath;

	public ClassDetector(String projectPath, String className) {
		this.projectPath = projectPath;
		this.className = hookHandleClassName(className);
		this.classDataRootPath = null;
	}

	private String hookHandleClassName(String className) {
		String cn = className.replaceAll("\\.", "\\\\") + ".class";
		return cn;
	}

	public String getClassDataRootPath() {
		if (null == classDataRootPath) {
			scanClassDataRootPath();
		}
		return classDataRootPath;
	}

	private void scanClassDataRootPath() {
		scanClassDataRootPath(projectPath);
	}

	private void scanClassDataRootPath(String projectPath) {
		File project = new File(projectPath);
		File[] files = project.listFiles();
		for (File file : files) {
			if (null != classDataRootPath) {
				break;
			}
			String filePath = file.getPath();
			if (file.isFile()) {
				if (filePath.endsWith(className)) {
					classDataRootPath = filePath.substring(0,
							filePath.indexOf(className));
					break;
				}
			} else {
				scanClassDataRootPath(filePath);
			}
		}
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String toString() {
		return "projectPath=[" + projectPath + "],classPath=[" + className
				+ "]";
	}

	public static void main(String... args) {
		String projectPath = "C:/EclipseEE/workspace/dms_test_platform";
		String className = "dms.test.cmis.atom.ConfigCenter";

		ClassDetector classDetector = new ClassDetector(projectPath, className);
		System.out.println(classDetector);
		System.out.println(classDetector.getClassDataRootPath());
	}
}
