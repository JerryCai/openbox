package com.googlecode.openbox.testu.tester;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class TestCaseAnalyzer {
	private Set<String> HANDLED_CLASS_NAMES = new HashSet<String>();
	private static final String REPORT_TITLE = "Test Cases";
	private TestCase root;

	private TestCaseAnalyzer() {
		this.root = TestCase.createTestCaseFromPool(REPORT_TITLE,true,"");
	}

	public static TestCaseAnalyzer newInstance() {
		return new TestCaseAnalyzer();
	}

	public TestCase getTestCaseRoot(){
		return root;
	}
	public void analysis(Method method) {
		Class<?> clss = method.getClass();
		String className = clss.getName();
		String suiteName = null;
		TestCase suiteTestFolder = null;
		TestReportTitle testReportTitle = clss
				.getAnnotation(TestReportTitle.class);
		if (null != testReportTitle) {
			String reportTitle = testReportTitle.value();
			if (!StringUtils.isBlank(reportTitle)) {
				root.setDisplayName(reportTitle);
			}
		}
		CaseSuite caseSuite = clss.getAnnotation(CaseSuite.class);
		if (null != caseSuite) {
			suiteName = caseSuite.name();
			if (StringUtils.isNotBlank(suiteName)) {
				suiteTestFolder = TestCase.createTestCaseFromPool(suiteName,true,"");
			}
		}

		if (!HANDLED_CLASS_NAMES.contains(className)) {
			HANDLED_CLASS_NAMES.add(className);
			String parentModuleName = caseSuite.parent();
			if (StringUtils.isNotBlank(parentModuleName)) {
				TestCase parentModuleNameFolder = TestCase
						.createTestCaseFromPool(parentModuleName,true,"");
				root.addChild(parentModuleNameFolder);
				parentModuleNameFolder.addChild(suiteTestFolder);
			} else {
				root.addChild(suiteTestFolder);
			}
		}
		Owner moduleLevelQA = clss.getAnnotation(Owner.class);
		String keySeed=className;
		CaseName caseName = method.getAnnotation(CaseName.class);
		if (null != caseName && StringUtils.isNoneBlank(caseName.value())) {
			ParentCaseName parentCaseName = method
					.getAnnotation(ParentCaseName.class);
			TestCase parentTestCase = null;
			if (null != parentCaseName
					&& StringUtils.isNotBlank(parentCaseName.value())) {
				parentTestCase = TestCase.createTestCaseFromPool(parentCaseName.value(),false,keySeed);
				suiteTestFolder.addChild(parentTestCase);
			}
			TestCase testCase = TestCase.createTestCaseFromPool(caseName.value(),false,keySeed);
			if (null != parentTestCase) {
				parentTestCase.addChild(testCase);
			} else {
				suiteTestFolder.addChild(testCase);
			}

			Owner caseLevelQA = method.getAnnotation(Owner.class);
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

			Bugs bugs = method.getAnnotation(Bugs.class);
			if (null != bugs) {
				testCase.setBugs(bugs);
			}
		}
	}

}
