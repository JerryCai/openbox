package org.eclipse.plugin.openbox.apiunit.core.casepojo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.plugin.openbox.apiunit.core.runner.JavaBasicTypeProxy;
import org.eclipse.plugin.openbox.apiunit.core.runner.RunnerClassLoader;
import org.eclipse.plugin.openbox.apiunit.core.store.CasesManager;
import org.eclipse.plugin.openbox.apiunit.core.store.DefaultCasesManager;


public class MethodDecoratorConverter {

	public static CaseContent toCaseContent(MethodDecorator method)
			throws Exception {
		try {
			String className = method.getClassForName();
			String methodName = method.getMethodName();
			String name = "test_" + method.getMethodName();
			String id = method.getMethod().getKey() + "_" + name;
			CaseContent caseContent = new CaseContent(className, methodName,
					id, name);
			caseContent
					.setDescription("This is unit test case for the below api :\n  "
							+ method.getMethodName()
							+ "\n\nSteps:\n\n"
							+ "Expected Result:\n\n");

			String apiSourceCode = method.getMethodSource();
			caseContent.setApiSourceCode(apiSourceCode);
			caseContent.setApiClassData(parseObjectFileds(method.getProject(),
					className));
			caseContent.setApiInputParameters(parseApiInputParameters(method));
			caseContent.setApiCheckpoints(parseApiCheckpoints(method));
			CasesManager casesManager = new DefaultCasesManager(method);
			caseContent.setCasesManager(casesManager);
			return caseContent;
		} catch (Exception e) {
			throw new Exception(
					"this tool can't support the current API testing", e);
		}
	}

	private static ApiCheckpoints parseApiCheckpoints(MethodDecorator method) {
		ApiCheckpoints apiCheckpoints = new ApiCheckpoints();

		Method m = method.parseMethod();
		Class<?> returnType = m.getReturnType();
		if (null == returnType) {
			return null;
		}
		if (ReturnObject.VOID_RETURN_TYPE_ID.equals(returnType.getName())) {
			return null;
		}

		ApiCheckpoint apiCheckpoint = new ApiCheckpoint(
				CheckpointMode.RETURN_OBJECT_MODE);
		ReturnObject returnObject = parseObjectFileds(method.getProject(),
				returnType.getName());
		apiCheckpoint.setReturnObject(returnObject);

		apiCheckpoints.appendApiCheckpoint(apiCheckpoint);
		return apiCheckpoints;
	}

	private static ApiInputParameter parseClassConstructor(
			IJavaProject project, String className) {
		ApiInputParameter param = new ApiInputParameter(0);
		param.setTypeId(className);
		param.setBasicType(false);
		param.setNonBasicTypeInputParameters(parseConstructorApiInputParameters(
				project, className));

		return param;
	}

	private static ApiInputParameters parseApiInputParameters(
			MethodDecorator method) {

		int parametersNum = method.getParametersNum();
		String[] parameterNames = method.getParameterNames();
		Method m = method.parseMethod();
		Class<?>[] parametersTypes = m.getParameterTypes();
		ApiInputParameters apiInputParameters = new ApiInputParameters(
				parametersNum);
		for (int i = 0; i < parametersNum; i++) {
			ApiInputParameter param = new ApiInputParameter(i);
			param.setTypeId(parametersTypes[i].getName());
			boolean isBasicType = isBasicType(parametersTypes[i]);
			param.setBasicType(isBasicType);
			String typeIndex = parametersTypes[i].getName();
			if (isBasicType) {
				String name = parameterNames[i];
				NameValuePair baseTypeInputParameter = new NameValuePair(name,
						typeIndex);
				param.setBaseTypeInputParameter(baseTypeInputParameter);
			} else {
				param.setReturnObject(parseObjectFileds(method.getProject(),
						typeIndex));
				param.setNonBasicTypeInputParameters(parseConstructorApiInputParameters(
						method.getProject(), typeIndex));
			}
			apiInputParameters.setParameter(param);
		}
		return apiInputParameters;
	}

	public static String[] getConstructorParameterNames(
			Constructor<?> constructor) {
		Class<?>[] classes = constructor.getParameterTypes();
		int length = classes.length;
		String[] names = new String[length];
		for (int i = 0; i < length; i++) {
			names[i] = "arg" + i;
		}
		return names;
//		return ClassHelper.getConstructorParameterNames(constructor);
	}

	public static ReturnObject parseObjectFileds(IJavaProject project,
			String className) {
		ReturnObject returnObject = new ReturnObject(className);
		Class<?> clss = RunnerClassLoader.loadClass(project, className);
		ApiInputParameter constructor = parseClassConstructor(project,
				className);
		returnObject.setConstructor(constructor);
		if (isBasicType(clss)) {
			Fields internFileds = new Fields(1);
			InternField internField = new InternField("arg0",className);
			internField.setBasicType(isBasicType(clss));
			internField.setIndex(0);

			NameValuePair baseasicTypeReturnObject = new NameValuePair("arg0",
					className);
			internField.setBaseasicTypeReturnObject(baseasicTypeReturnObject);
			internFileds.setField(internField);
			returnObject.setFields(internFileds);
			return returnObject;
		}
		Field[] fileds = clss.getDeclaredFields();
		Fields internFileds = new Fields(fileds.length);
		int count = 0;
		for (int i = 0; i < fileds.length; i++) {
			Field filed = fileds[i];
			if (Modifier.isFinal(filed.getModifiers())) {
				count++;
				continue;
			}
			String name = filed.getName();
			String typeId = filed.getType().getName();
			InternField internField = new InternField(name,typeId);
			Class<?> filedClass = RunnerClassLoader.loadClass(project, typeId);
			boolean isBasicType = isBasicType(filedClass);
			internField.setBasicType(isBasicType);
			internField.setIndex(i - count);
			if (isBasicType){
				NameValuePair baseasicTypeReturnObject = new NameValuePair(
						name, typeId);
				internField
						.setBaseasicTypeReturnObject(baseasicTypeReturnObject);
			}else{
				// can't to deep analysis for the endless loop
				 ReturnObject nonBasicTypeReturnObject = parseObjectFileds(
				 project, typeId);
				 internField.setNonBasicTypeReturnObject(nonBasicTypeReturnObject);
			}
			internFileds.setField(internField);
		}
		internFileds.setNum(fileds.length - count);
		returnObject.setFields(internFileds);
		return returnObject;
	}

	public static ApiInputParameters parseConstructorApiInputParameters(
			IJavaProject project, String className) {
		Class<?> pC = RunnerClassLoader.loadClass(project, className);
		Constructor<?>[] constructors = pC.getDeclaredConstructors();
		if (constructors.length <= 0) {
			ApiInputParameters nonBasicTypeInputParameters = new ApiInputParameters(
					0);
			return nonBasicTypeInputParameters;
		}
		Constructor<?> constructor = constructors[0];
		String[] parameterNames = getConstructorParameterNames(constructor);

		Class<?>[] parametersTypes = constructor.getParameterTypes();
		int num = parameterNames.length;
		ApiInputParameters nonBasicTypeInputParameters = new ApiInputParameters(
				num);
		for (int i = 0; i < num; i++) {
			ApiInputParameter param = new ApiInputParameter(i);
			boolean isBasicType = isBasicType(parametersTypes[i]);
			param.setBasicType(isBasicType);
			String typeIndex = parametersTypes[i].getName();
			param.setTypeId(typeIndex);
			if (isBasicType) {
				String name = parameterNames[i];
				NameValuePair baseTypeInputParameter = new NameValuePair(name,
						typeIndex);
				param.setBaseTypeInputParameter(baseTypeInputParameter);
			} else {
				// param.setReturnObject(parseObjectFileds(project, typeIndex));
				param.setNonBasicTypeInputParameters(parseConstructorApiInputParameters(
						project, typeIndex));
			}
			nonBasicTypeInputParameters.setParameter(param);
		}
		return nonBasicTypeInputParameters;
	}

	private static boolean isBasicType(Class<?> c) {
		if (null == c) {
			return true;
		}
		if(JavaBasicTypeProxy.isBasicType(c.getName())){
			return true;
		}
		return c.isPrimitive();
	}

	public static char getTypeIndex(char[] string) {
		return getTypeIndex(string, 0);
	}

	public static char getTypeIndex(char[] string, int start) {
		// need a minimum 1 char
		if (start >= string.length) {
			throw new IllegalArgumentException();
		}
		return string[start];
	}
}
