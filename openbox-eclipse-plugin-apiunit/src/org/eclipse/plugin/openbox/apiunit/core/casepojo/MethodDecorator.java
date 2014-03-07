package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.plugin.openbox.apiunit.utils.ClassDetector;
import org.eclipse.plugin.openbox.apiunit.utils.ClassHelper;
import org.eclipse.plugin.openbox.apiunit.utils.FileSystemClassLoader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;


@SuppressWarnings("restriction")
public class MethodDecorator {
	private static final Log logger = LogFactory.getLog(MethodDecorator.class);

	private String id;
	private IMethod method;
	private IType sourceType;
	private ICompilationUnit unit;
	private IPackageFragment pkg;
	private IJavaProject project;

	private String classForName;
	private String classDataRootPath;

	public MethodDecorator(IMethod method) {
		this.method = method;
		hookInitMethodDecorator();
	}

	private void initClassForName() {
		try {
			classForName = unit.getPackageDeclarations()[0].getElementName()
					+ "." + unit.getElementName().replace(".java", "");

		} catch (JavaModelException e) {
			logger.error("get class name error !", e);
		}
	}

	public void initDataRootPath() {
		String projectPath = project.getProject().getLocationURI().getPath();
		ClassDetector classDetector = new ClassDetector(projectPath,
				getClassForName());

		classDataRootPath = classDetector.getClassDataRootPath();
	}

	public String getMethodSource() {
		String code = null;
		try {
			code = method.getSource();
		} catch (JavaModelException e) {
			logger.error("get the method source code error", e);
		}
		return code;
	}

	private String[] getParameterTypesHook() {
		return getParameterTypeShortNames();
	}

	public Method parseMethod() {
		List<Method> methods = listAllOverloadMethods();
		String[] parameterTypes = getParameterTypesHook();
		Method m = null;

		int length = parameterTypes.length;
		for (Method aMethod : methods) {
			Class<?>[] pClasses = aMethod.getParameterTypes();
			if (length != pClasses.length) {
				continue;
			}
			boolean match = true;
			for (int i = 0; i < length; i++) {
				if (pClasses[i].getName().endsWith(parameterTypes[i])) {
					continue;
				} else {
					match = false;
					break;
				}
			}
			if (match) {
				m = aMethod;
				break;
			}
		}
		return m;
	}

	public int getParametersNum() {
		ILocalVariable[] parameters = getParameters();
		if (null == parameters) {
			return 0;
		}
		return parameters.length;
	}

	public String[] getParameterNames() {
		String[] parameterNames = null;
		try {
			return method.getParameterNames();
		} catch (JavaModelException e) {
			logger.error("IMethod-->getParameterNames failed !!!", e);
		}
		return parameterNames;
	}

	public String[] getParameterTypeShortNames(){
		String [] types = method.getParameterTypes();
		int num = types.length;
		String[] shortNames = new String[num];
		for(int i = 0 ; i< num ; i++){
			shortNames[i] = Signature.getSignatureSimpleName(types[i]);
		}
		return shortNames;
	}
	
	public ILocalVariable[] getParameters() {
		ILocalVariable[] parameters = null;
		try {
			parameters = method.getParameters();
		} catch (JavaModelException e) {
			logger.error("call the method getParameters error", e);
		}
		return parameters;
	}

	public Class<?> loadFileClass(String className) {
		return loadFileClass(getClassDataRootPath(), className);
	}

	public static Class<?> loadFileClass(String classDataRootPath, String className) {
		FileSystemClassLoader fsc = new FileSystemClassLoader(classDataRootPath);
		return fsc.loadFileClass(className);
	}

	public String getClassForName() {
		if (null == classForName)
			initClassForName();
		return classForName;
	}

	public String getClassDataRootPath() {
		if (null == classDataRootPath)
			initDataRootPath();
		return classDataRootPath;
	}
	
	/**
	 * version for class file load mode
	 * 
	 * @return
	 */
	public List<Method> listAllOverloadMethods() {
		String className = getClassForName();
		String methodName = getMethodName();
		List<Method> overloadMethods = new LinkedList<Method>();
		try {
			Class<?> clss =loadClass(className);
			Method[] methods = clss.getDeclaredMethods();
			for (Method method : methods) {
				if (methodName.equals(method.getName())) {
					overloadMethods.add(method);
				}
			}
		} catch (Exception e) {
			System.out.println("reflect [" + className + "] to get method ["
					+ methodName + "] all overload methods failed !"
					+ e.toString());
			logger.error("reflect [" + className + "] to get method ["
					+ methodName + "] all overload methods failed !", e);
		}
		return overloadMethods;
	}

	public String getMethodName() {
		return getMethod().getElementName();
	}
	
	public void getClassLoaderBundle(){
		BundleContext bc= EclipseStarter.getSystemBundleContext();
		System.out.println("EclipseStarter.getSystemBundleContext()==>"+bc.getBundle().getBundleId());
		Bundle[] bundles =bc.getBundles();
		for(int i = 0 ; i<bundles.length ; i++){
			Bundle bundle = bundles[i];
			System.out.println("bundle["+i+"].getBundleId()==>"+bundle.getBundleId());
			System.out.println("bundle["+i+"].getLocation()==>"+bundle.getLocation());
		}
		
	}
	
	public Class<?> loadClass(String className){
		Class<?> clss = null;
		try {
			clss =  ClassHelper.loadClass(getProject(), className);
		} catch (Exception e) {
			logger.error("error when load class ["+className+"]", e);
		}
		return clss;
	}
	
	private void hookInitMethodDecorator() {
		id = UUID.randomUUID().toString();
		sourceType = (IType) method.getParent();
		unit = (ICompilationUnit) sourceType.getParent();
		pkg = (IPackageFragment) unit.getParent();
		project = method.getJavaProject();

		initClassForName();
		initDataRootPath();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public IMethod getMethod() {
		return method;
	}

	public void setMethod(IMethod method) {
		this.method = method;
	}

	public IType getSourceType() {
		return sourceType;
	}

	public void setSourceType(IType sourceType) {
		this.sourceType = sourceType;
	}

	public ICompilationUnit getUnit() {
		return unit;
	}

	public void setUnit(ICompilationUnit unit) {
		this.unit = unit;
	}

	public IPackageFragment getPkg() {
		return pkg;
	}

	public void setPkg(IPackageFragment pkg) {
		this.pkg = pkg;
	}

	public IJavaProject getProject() {
		return project;
	}

	public void setProject(IJavaProject project) {
		this.project = project;
	}

}
