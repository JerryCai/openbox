package org.eclipse.plugin.openbox.apiunit.core.runner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.core.runtime.IPath;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.ApiCheckpoint;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.ApiCheckpoints;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.ApiInputParameter;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.ApiInputParameters;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.CaseContent;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.Fields;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.InternField;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.NameValuePair;
import org.eclipse.plugin.openbox.apiunit.core.casepojo.ReturnObject;
import org.eclipse.plugin.openbox.apiunit.core.runner.ui.TestResult;
import org.eclipse.plugin.openbox.apiunit.core.store.CaseStoreStrategyImpl;
import org.eclipse.plugin.openbox.apiunit.core.store.ICase;
import org.eclipse.plugin.openbox.apiunit.views.UITipMsg;


public abstract class AbstractCaseRunner implements CaseRunner {

	protected abstract ICase getCase(IPath path);

	protected abstract Class<?> loadClass(String className) throws Exception;

	protected abstract Class<?> loadMockClass(String className) throws Exception;

	
	@Override
	public TestResult run(IPath path) {
		ICase aCase = null;
		try {
			aCase = loadCase(path);
		} catch (Exception e) {
			String caseFileName = path.lastSegment()
					.replace(
							CaseStoreStrategyImpl.getInstance()
									.getStoreFileExtention(), "");
			TestResult testResult = new TestResult(caseFileName);
			testResult.setResult(TestResult.CASE_ERROR);
			testResult.setMessage(e.toString());
			return testResult;
		}
		return run(aCase);
	}

	@Override
	public TestResult run(ICase aCase) {
		CaseContent caseContent = aCase.getCaseContent();
		return run(caseContent);
	}

	@Override
	public TestResult run(CaseContent caseContent) {
		TestResult testResult = new TestResult(caseContent.getName());
		try {
			String className = caseContent.getClassName();
			Class<?> clss = loadMockClass(className);
			String methodName = caseContent.getMethodName();
			ApiInputParameters apiInputParameters = caseContent
					.getApiInputParameters();
			MethodArguments args = getMethodArguments(apiInputParameters);
			Method method = clss.getDeclaredMethod(methodName, args.getArgumentTypes());
			method.setAccessible(true);
			Object classObject = newInstance(clss,
					caseContent.getApiClassData());
			testResult.startTest();
			Object returnObject = method.invoke(classObject,
					args.getArgumentValues());
			testResult.endTest();
			Class<?> returnType = method.getReturnType();
			ApiCheckpoints checkpionts = caseContent.getApiCheckpoints();
			executeCheckpoints(returnType, checkpionts, returnObject,
					testResult);
		} catch (Exception e) {
			e.printStackTrace();
			testResult.setResult(TestResult.CASE_ERROR);
			testResult.setMessage(e.getMessage()+" --> "+e.getCause());
		}
		return testResult;

	}

	private ICase loadCase(IPath path) throws Exception {
		ICase aCase = getCase(path);
		UITipMsg result = aCase.read();
		if (!result.isSuccess()) {
			throw new Exception(result.getTipMsg());
		}
		return aCase;
	}

	private Object newInstance(Class<?> clss, ReturnObject apiClassData)
			throws Exception {
		Object obj = null;
		ApiInputParameter constructor = apiClassData.getConstructor();
		if (constructor.isBasicType()) {
			NameValuePair nameValue = constructor.getBaseTypeInputParameter();
			String value = nameValue.getValue();
			Class<?> stringClass = loadMockClass("java.lang.String");
			Constructor<?> cons = clss.getDeclaredConstructor(stringClass);
			cons.setAccessible(true);
			obj = cons.newInstance(value);
		} else {
			ApiInputParameters apiInputParameters = constructor
					.getNonBasicTypeInputParameters();
			MethodArguments args = getMethodArguments(apiInputParameters);
			 // below method can't handle the two classLoader for int.class ...basic type
			//, so use getgetDeclaredConstructorHook
			Constructor<?> cons = clss.getDeclaredConstructor(args.getArgumentTypes());
//			Constructor<?> cons = getgetDeclaredConstructorHook(clss.getDeclaredConstructors(),args.getArgumentTypes());
			cons.setAccessible(true);
			obj = cons.newInstance(args.getArgumentValues());
			initInstanceValues(obj, apiClassData);
		}
		return obj;
	}

	private MethodArguments getMethodArguments(
			ApiInputParameters apiInputParameters) throws Exception {
		int num = apiInputParameters.getNum();
		MethodArguments args = new MethodArguments(num);
		for (int i = 0; i < num; i++) {
			ApiInputParameter apiInputParameter = apiInputParameters
					.getParameter(i);
			MethodArgument arg = getArg(apiInputParameter);
			arg.setIndex(i);
			args.setMethodArgument(arg);
		}
		return args;
	}

	private MethodArgument getArg(ApiInputParameter apiInputParameter)
			throws Exception {
		String className = apiInputParameter.getTypeId();
		if (apiInputParameter.isBasicType()) {
			NameValuePair nameValue = apiInputParameter
					.getBaseTypeInputParameter();
			return getArg(nameValue);
		}
		Class<?> argType = loadClass(className);
		ApiInputParameters subApiInputParameters = apiInputParameter
				.getNonBasicTypeInputParameters();
		MethodArguments subArgs = getMethodArguments(subApiInputParameters);
		Constructor<?> cons = argType.getDeclaredConstructor(subArgs.getArgumentTypes());
		cons.setAccessible(true);
		Object argValue = cons.newInstance(subArgs.getArgumentValues());
		MethodArgument arg = new MethodArgument(0, argType, argValue);
		ReturnObject returnObject = apiInputParameter.getReturnObject();
		if(null != returnObject)
			initInstanceValues(argValue, returnObject);
		return arg;
	}

	private MethodArgument getArg(NameValuePair nameValue) throws Exception {
		String basicTypeClassName = nameValue.getTypeIndex();
		Class<?> argType = loadClass(basicTypeClassName);
		String value = nameValue.getValue();
		Class<?> stringClass = loadMockClass("java.lang.String");
		Class<?> argTypeMock = loadMockClass(basicTypeClassName);
		Constructor<?> construct =argTypeMock.getDeclaredConstructor(stringClass);
		construct.setAccessible(true);
		Object argValue = construct.newInstance(value);
		MethodArgument arg = new MethodArgument(0, argType, argValue);
		return arg;
	}

	private void initInstanceValues(Object argValue, ReturnObject returnObject)
			throws Exception {
		Class<?> argType = argValue.getClass();
		Fields fields = returnObject.getFields();
		int num = fields.getNum();
		for (int i = 0; i < num; i++) {
			InternField internField = fields.getField(i);
			String fieldName =internField.getFieldName();
			if (internField.isBasicType()) {
				NameValuePair basicField = internField
						.getBaseasicTypeReturnObject();
				Field field = argType.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(argValue,getArg(basicField).getValue());
			} else {
				ReturnObject subReturnObject = internField
						.getNonBasicTypeReturnObject();
				String subClassName = subReturnObject.getTypeId();
				Object subValue = newInstance(loadMockClass(subClassName),
						subReturnObject);
				initInstanceValues(subValue, subReturnObject);
				Field field  =argType.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(argValue, subValue);
			}
		}
	}

	private void executeCheckpoints(Class<?> returnType,
			ApiCheckpoints expectedCPs, Object actualRO, TestResult testResult)
			throws Exception {
		testResult.setResult(TestResult.SUCCESS);
		StringBuilder details = new StringBuilder();
		StringBuilder message = new StringBuilder();
		int cpNum = expectedCPs.getApiCheckpointsNum();
		for (int i = 0; i < cpNum; i++) {
			ApiCheckpoint checkpoint = expectedCPs.getCheckpoint(i);
			ReturnObject expectedRO = checkpoint.getReturnObject();
			Fields expectedROFields = expectedRO.getFields();
			int fieldsNum = expectedROFields.getNum();
			for (int j = 0; j < fieldsNum; j++) {
				InternField expectedF = expectedROFields.getField(j);
				if (expectedF.isBasicType()) {
					NameValuePair expected = expectedF
							.getBaseasicTypeReturnObject();
					String expectedFieldName = expected.getName();
					String expectedFieldValue = expected.getValue();
					Field field = returnType.getDeclaredField(expectedFieldName);
					field.setAccessible(true);
					String actualFieldValue = field.get(actualRO)
							.toString();
					details.append("checkpoint[").append(i).append("]")
							.append("fields[").append(j)
							.append("]=["+expectedFieldName+"]==>expectedFieldValue=[")
							.append(expectedFieldValue).append("] , ")
							.append("actualFieldValue=[")
							.append(actualFieldValue).append("]");
					if (expectedFieldValue == actualFieldValue) {
						details.append("==> success \n");
					} else if (null == expectedFieldValue) {
						details.append("==> failed \n");
						message.append("checkpoint[").append(i).append("]")
								.append("fields[").append(j)
								.append("]=["+expectedFieldName+"]==>expectedFieldValue=[")
								.append(expectedFieldValue).append("] , ")
								.append("actualFieldValue=[")
								.append(actualFieldValue).append("]")
								.append("==> failed \n");
						;
						testResult.setResult(TestResult.FAILURE);
					} else if (null == actualFieldValue) {
						details.append("==> failed \n");
						message.append("checkpoint[").append(i).append("]")
								.append("fields[").append(j)
								.append("]=["+expectedFieldName+"]==>expectedFieldValue=[")
								.append(expectedFieldValue).append("] , ")
								.append("actualFieldValue=[")
								.append(actualFieldValue).append("]")
								.append("==> failed \n");
						testResult.setResult(TestResult.FAILURE);
					}
					if (expectedFieldValue.equals(actualFieldValue)) {
						details.append("==> success \n");
					} else {
						details.append("==> failed \n");
						message.append("checkpoint[").append(i).append("]")
								.append("fields[").append(j)
								.append("]=["+expectedFieldName+"]==>expectedFieldValue=[")
								.append(expectedFieldValue).append("] , ")
								.append("actualFieldValue=[")
								.append(actualFieldValue).append("]")
								.append("==> failed \n");
						testResult.setResult(TestResult.FAILURE);
					}
				}
			}
		}
		testResult.setDetails(details.toString());
		testResult.setMessage(message.toString());
	}
}