package org.eclipse.plugin.openbox.apiunit.core.runner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.plugin.openbox.apiunit.utils.ClassHelper;


public class RunnerClassLoader {
	private static final Log logger = LogFactory.getLog(RunnerClassLoader.class);
	
	public static Class<?> loadClass(IJavaProject project, String className) {
		JavaBasicTypeProxy javaBasicType = JavaBasicTypeProxy
				.fromValue(className);
		if (null != javaBasicType) {
			return javaBasicType.getBasicTypeClass();
		}
		Class<?> clss = null;
		try {
			clss = ClassHelper.loadClass(project, className);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("load class [" + className + "] error !!!", e);
		}
		return clss;
	}
	
	public static Class<?> loadBasicMockedClass(IJavaProject project, String className) {
		String handledClassName = className;
		LocatizedJavaBasicTypeMock javaBasicType = LocatizedJavaBasicTypeMock
				.fromValue(className);
		if (null != javaBasicType) {
			handledClassName = javaBasicType.getBasicTypeWrapperName();
		}
		Class<?> clss = null;
		try {
			clss = ClassHelper.loadClass(project, handledClassName);
		} catch (Exception e) {
			logger.error("load class [" + className + "] error !!!", e);
		}
		return clss;
	}
}
