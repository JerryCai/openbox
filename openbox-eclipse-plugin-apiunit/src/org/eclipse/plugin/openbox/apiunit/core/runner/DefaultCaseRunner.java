package org.eclipse.plugin.openbox.apiunit.core.runner;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.plugin.openbox.apiunit.core.store.CaseStoreStrategy;
import org.eclipse.plugin.openbox.apiunit.core.store.CaseStoreStrategyImpl;
import org.eclipse.plugin.openbox.apiunit.core.store.ICase;
import org.eclipse.plugin.openbox.apiunit.core.store.TestCase;


public class DefaultCaseRunner extends AbstractCaseRunner {
	public static Class<?> BOOLEAN_CLASS;
	public static Class<?> BYTE_CLASS;
	public static Class<?> CHAR_CLASS;
	public static Class<?> DOUBLE_CLASS;
	public static Class<?> FLOAT_CLASS;
	public static Class<?> INT_CLASS;
	public static Class<?> LONG_CLASS;
	public static Class<?> SHORT_CLASS;

	private IJavaProject project;

	private static final CaseStoreStrategy caseStore = CaseStoreStrategyImpl
			.getInstance();

	public DefaultCaseRunner(IJavaProject project) {
		this.project = project;
		initHook();
	}

	public void initHook() {
		try {
			BOOLEAN_CLASS = loadClass(JavaBasicTypes.JAVA_LANG_BOOLEAN);
			BYTE_CLASS = loadClass(JavaBasicTypes.JAVA_LANG_BYTE);
			CHAR_CLASS = loadClass(JavaBasicTypes.JAVA_LANG_CHARACTER);
			DOUBLE_CLASS = loadClass(JavaBasicTypes.JAVA_LANG_DOUBLE);
			FLOAT_CLASS = loadClass(JavaBasicTypes.JAVA_LANG_FLOAT);
			INT_CLASS = loadClass(JavaBasicTypes.JAVA_LANG_INTEGER);
			LONG_CLASS = loadClass(JavaBasicTypes.JAVA_LANG_LONG);
			SHORT_CLASS = loadClass(JavaBasicTypes.JAVA_LANG_SHORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected ICase getCase(IPath path) {
		return new TestCase(project.getProject(), getCaseNameFromIPath(path),
				path);
	}

	@Override
	protected Class<?> loadClass(String className) throws Exception {
		return RunnerClassLoader.loadClass(project, className);
	}

	@Override
	protected Class<?> loadMockClass(String className) throws Exception {
		return RunnerClassLoader.loadBasicMockedClass(project, className);
	}

	private String getCaseNameFromIPath(IPath path) {
		return path.lastSegment().replaceAll(caseStore.getStoreFileExtention(),
				"");
	}

}
