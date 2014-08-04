package com.googlecode.openbox.testu.tester;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.testu.TestUException;

public class DefaultTestCasesManager implements TestCasesManager {
	private static final Logger logger = LogManager.getLogger();

	public static final String CLASS_POSTFIX = ".class";

	private List<TestCasesSelector> selectors;
	private List<TestCasesExporter> exporters;
	private TestCasePool testCasePool;

	private DefaultTestCasesManager() {
		this.selectors = new LinkedList<TestCasesSelector>();
		this.exporters = new LinkedList<TestCasesExporter>();
		this.testCasePool = TestCasePoolImpl.create();
	}

	public static DefaultTestCasesManager newInstance() {
		return new DefaultTestCasesManager();
	}

	@Override
	public void addTestCasesSelector(TestCasesSelector testCasesSelector) {
		selectors.add(testCasesSelector);
	}

	@Override
	public void addTestCasesExporter(TestCasesExporter exporter) {
		exporters.add(exporter);
	}

	@Override
	public void execute() {
		for (TestCasesSelector selector : selectors) {
			execute(selector);
		}
		testCasePool.exportCaseTreeRoot();
		for (TestCasesExporter exporter : exporters) {
			exporter.export(testCasePool,null);
		}
	}

	private void execute(TestCasesSelector selector) {
		String startPackage = selector.getClass().getPackage().getName();
		startPackage = startPackage.replaceAll("\\.", "\\\\");
		URL url = selector.getClass().getResource(".");
		File file = null;
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			throw TestUException.create("url [" + url + "] isn't incorrect !",
					e);
		}

		String prefix = selector.prefix();
		String postfix = selector.postfix();
		searchTestngClass(file, startPackage, prefix, postfix);
	}

	private void searchTestngClass(File file, String startPackage,
			String prefix, String postfix) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File tmpFile : files) {
				searchTestngClass(tmpFile, startPackage, prefix, postfix);
			}
		} else {
			String name = file.getName();
			if (name.indexOf("$") < 0) {
				int op = name.lastIndexOf(".");
				String strName = name.substring(0, op);
				if (strName.startsWith(prefix) && strName.endsWith(postfix)) {
					String path = file.getPath();
					int pp = path.indexOf(startPackage);
					int endPP = path.indexOf(CLASS_POSTFIX);
					String className = path.substring(pp, endPP);
					className = className.replaceAll("\\\\", ".");
					process(className);
				}
			}
		}
	}

	private void process(String testClassName) {
		Class<?> clss = null;
		try {
			clss = Class.forName(testClassName);
		} catch (ClassNotFoundException e) {
			String msg = "the test class [" + testClassName
					+ "] can't be found !";
			logger.error(msg, e);
			throw TestUException.create(msg, e);
		}
		String suiteName = null;
		CaseSuite caseSuite = clss.getAnnotation(CaseSuite.class);
		if (null != caseSuite) {
			suiteName = caseSuite.name();
			testCasePool.createTestCase(suiteName);
			String parentModuleName = caseSuite.parent();
			if (null != parentModuleName) {
				testCasePool.createTestCase(parentModuleName);
			}
			testCasePool.addCaseTreeRelationship(parentModuleName, suiteName);
		}

		QA moduleLevelQA = clss.getAnnotation(QA.class);
		
		Method[] methods = clss.getDeclaredMethods();
		for (Method method : methods) {
			CaseName caseName = method.getAnnotation(CaseName.class);
			if (null == caseName) {
				// skip this method
				continue;
			}

			ParentCaseName parentCaseName = method
					.getAnnotation(ParentCaseName.class);
			String parentName = null;
			String testCaseName = caseName.value();
			if (null != parentCaseName) {
				testCasePool.createTestCase(parentCaseName.value());
				parentName = parentCaseName.value();
				testCasePool.addCaseTreeRelationship(suiteName, parentName);
			} else {
				parentName = suiteName;
			}

			TestCase testCase = testCasePool.createTestCase(testCaseName);
			testCasePool.addCaseTreeRelationship(parentName, testCaseName);
			QA caseLevelQA = method.getAnnotation(QA.class);
			if (null != caseLevelQA) {
				testCase.setOwner(caseLevelQA);
			} else if (null != moduleLevelQA) {
				testCase.setOwner(moduleLevelQA);
			}
			testCase.setCaseDescriptions(method
					.getAnnotation(CaseDescriptions.class));
			testCase.setPreconditions(method.getAnnotation(Preconditions.class));
			testCase.setSteps(method.getAnnotation(Steps.class));
			testCase.setExpectedResults(method
					.getAnnotation(ExpectedResults.class));
			ActualResults actualResults = method
					.getAnnotation(ActualResults.class);
			if (null != actualResults) {
				TestCaseResults actualTestResult = TestCaseResults
						.newInstance();
				actualTestResult.setActualResults(actualResults);
				testCase.setActualResults(actualTestResult);
			}

			Bugs bugs = method.getAnnotation(Bugs.class);
			if (null != bugs) {
				testCase.setBugs(bugs);
			}
		}

	}

}
