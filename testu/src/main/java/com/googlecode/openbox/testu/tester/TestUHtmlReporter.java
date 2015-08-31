package com.googlecode.openbox.testu.tester;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;
import org.uncommons.reportng.HTMLReporter;
import org.uncommons.reportng.ReportNGUtils;

import com.googlecode.openbox.common.IOUtils;
import com.googlecode.openbox.common.UtilsAPI;
import com.googlecode.openbox.common.context.BasicContext;
import com.googlecode.openbox.common.context.CommonContext;
import com.googlecode.openbox.testu.tester.CaseObjectives.CaseObjective;
import com.googlecode.openbox.testu.tester.OverallTestResult.ResultRow;
import com.googlecode.openbox.testu.tester.OverallTestResult.SuiteResult;
import com.googlecode.openbox.testu.tester.TestCaseResults.Result;
import com.googlecode.openbox.testu.tester.exporters.InternHtmlExporter;

public class TestUHtmlReporter implements IReporter {
	private static final Logger logger = LogManager.getLogger();

	private static final ReportNGUtils HELPER = new ReportNGUtils();
	private static final Set<String> HANDLED_CLASS_NAMES = new HashSet<String>();
	private static final HTMLReporter REPORTNG = new HTMLReporter();
	private static final Map<String, TestCasesExporter> EXPORTERS = new HashMap<String, TestCasesExporter>();
	private static boolean _executed = false;
	private static final Set<String> ERROR_EXPORTER_RECORDS = new HashSet<String>();
	private static final String REPORT_TITLE = "Test Cases";
	private static final TestCase ROOT = TestCase.createTestCaseFromPool(
			REPORT_TITLE, true, TestUHtmlReporter.class.getName());
	private static boolean expand = false;
	private static TempForceAllTestResult TEMP_FORCE_ALL_TEST_RESULT = null;

	public TestUHtmlReporter() {

	}

	public synchronized void generateReport(List<XmlSuite> xmlSuites,
			List<ISuite> suites, String outputDirectoryName) {
		if (_executed) {
			// avoid to user multi register the same report to your test
			// freamwork and test cases
			return;
		}
		_executed = true;
		// integration with reportNG,that means , if you add this listerner,
		// by default , reportNG is auto registed too , This is more easy to
		// use.
		REPORTNG.generateReport(xmlSuites, suites, outputDirectoryName);
		ITestContext textContext = null;
		OverallTestResult overallTestResult = new OverallTestResult();

		for (ISuite suite : suites) {
			SuiteResult suiteResult = overallTestResult.addSuite(suite
					.getName());
			for (ISuiteResult result : suite.getResults().values()) {
				textContext = result.getTestContext();
				processResults(textContext.getFailedConfigurations());
				processResults(textContext.getSkippedConfigurations());
				IResultMap failedTests = textContext.getFailedTests();
				processResults(failedTests);
				IResultMap skippedTests = textContext.getSkippedTests();
				processResults(skippedTests);
				IResultMap passedTests = textContext.getPassedTests();
				processResults(passedTests);
				suiteResult.addRow(ResultRow.create(textContext.getName(),
						passedTests.size(), skippedTests.size(),
						failedTests.size(), HELPER.getDuration(textContext)));
			}
		}
		ROOT.analysisOverallTestStatus();
		if (null != textContext) {
			exportHtmlTestReport(textContext, overallTestResult);
		}
		executeExtendedExporters();
	}

	private void processResults(IResultMap resultMap) {
		for (ITestResult tr : resultMap.getAllResults()) {
			process(tr);
		}
	}

	private void exportHtmlTestReport(ITestContext testContext,
			OverallTestResult overallTestResult) {
		String testngOutputPath = testContext.getOutputDirectory();
		String reportPath = UtilsAPI.getParentPath(testngOutputPath,
				IOUtils.PATH_SPLIT)
				+ IOUtils.PATH_SPLIT
				+ "testu"
				+ IOUtils.PATH_SPLIT + "testreport";
		InternTestCasesExporter htmlTextExporter = InternHtmlExporter
				.newInstance(reportPath);
		CommonContext context = new BasicContext();
		context.setAttribute(InternHtmlExporter.CONTEXT_ID, overallTestResult);
		this.recursiveUpdateExpandStatus(expand);
		String reportIndexFile = htmlTextExporter.export(ROOT, context);
		if (logger.isInfoEnabled()) {
			logger.info("TestU Report done , report location is ["
					+ reportIndexFile + "]");
		}

	}

	private void executeExtendedExporters() {
		for (TestCasesExporter exporter : EXPORTERS.values()) {
			try {
				long startTime = System.currentTimeMillis();
				exporter.export(ROOT);
				long endTime = System.currentTimeMillis();
				long duration = endTime - startTime;
				String durationStr = null;
				if (duration >= 1000) {
					durationStr = ", duration [" + (duration / 1000) + "] s";
				} else {
					durationStr = ", duration [" + duration + "] ms";
				}

				if (logger.isInfoEnabled()) {
					logger.info("your registed TestCaseExporter-["
							+ exporter.getClass().getName()
							+ "] is executed success" + durationStr);
				}
			} catch (Exception e) {
				logger.error(
						"your registed TestCaseExporter-["
								+ exporter.getClass().getName()
								+ "] execute error , please check its implementation !",
						e);
			}
		}
	}

	private void collectExtendedExporters(Class<?> clss) {
		TestCasesExporters extendedTestCasesExporters = clss
				.getAnnotation(TestCasesExporters.class);
		if (null == extendedTestCasesExporters) {
			Class<?> superClass = clss.getSuperclass();
			if (null != superClass) {
				collectExtendedExporters(superClass);
			}

		} else {
			for (Class<? extends TestCasesExporter> exporterClass : extendedTestCasesExporters
					.value()) {
				String className = exporterClass.getName();
				if (EXPORTERS.containsKey(className)
						|| ERROR_EXPORTER_RECORDS.contains(className)) {
					continue;
				}
				try {
					EXPORTERS.put(className, exporterClass.newInstance());
					if (logger.isInfoEnabled()) {
						logger.info("Your registed TestCaseExporter-["
								+ className + "] is loaded success !");
					}
				} catch (Exception e) {
					ERROR_EXPORTER_RECORDS.add(className);
					logger.error(
							"You registered TestCaseExporter["
									+ className
									+ "] can't be build up by clss.newInstance(), Please check your constuct and its implementation, skip to use this test case exporter",
							e);
				}
			}
		}

	}

	public abstract class TreeRecursiveVisitor {
		abstract void action(TestCase testCase);

		public void recursiveVisit(TestCase root) {
			action(root);
			for (TestCase subTestCase : root.getChildren()) {
				recursiveVisit(subTestCase);
			}

		}

	}

	private void recursiveUpdateExpandStatus(final boolean expand) {

		new TreeRecursiveVisitor() {

			@Override
			void action(TestCase testCase) {
				testCase.setExpand(expand);
			}

		}.recursiveVisit(ROOT);
		ROOT.setExpand(true);

	}

	private String[] getMergedCaseObjectives(CaseObjectives... caseObjectiveSet) {
		List<String> caseObjectivesList = new ArrayList<String>();

		for (int i = 0; i < caseObjectiveSet.length; i++) {
			CaseObjectives caseObjectives = caseObjectiveSet[i];
			if (null != caseObjectives) {
				CaseObjective[] caseObjectiveValues = caseObjectives.value();
				for (CaseObjective caseObjective : caseObjectiveValues) {
					if (!caseObjectivesList.contains(caseObjective.name())) {
						caseObjectivesList.add(caseObjective.name());
					}
				}
				String[] others = caseObjectives.others();
				for (String other : others) {
					if (!caseObjectivesList.contains(other)) {
						caseObjectivesList.add(other);
					}
				}
				break;
			}
		}
		int size = caseObjectivesList.size();
		String[] mergedValues = new String[size];
		for (int i = 0; i < size; i++) {
			mergedValues[i] = caseObjectivesList.get(i);
		}

		return mergedValues;
	}

	private void process(ITestResult tr) {
		ITestNGMethod testngMethod = tr.getMethod();
		Class<?> clss = testngMethod.getRealClass();
		String className = clss.getName();
		String suiteName = null;
		TestCase suiteTestFolder = null;
		@SuppressWarnings("deprecation")
		TestReportTitle testReportTitle = clss
				.getAnnotation(TestReportTitle.class);
		if (null != testReportTitle) {
			@SuppressWarnings("deprecation")
			String reportTitle = testReportTitle.value();
			if (!StringUtils.isBlank(reportTitle)) {
				ROOT.setDisplayName(reportTitle);
			}
		}
		TestReport testReport = clss.getAnnotation(TestReport.class);
		if (null != testReport) {
			String reportTitle = testReport.title();
			if (!StringUtils.isBlank(reportTitle)) {
				ROOT.setDisplayName(reportTitle);
				expand = testReport.expand();
			}
		}
		Boolean isTestCaseAutomated = Boolean.TRUE;

		TestCaseAutomated testCaseAutomated = clss
				.getAnnotation(TestCaseAutomated.class);
		if (null != testCaseAutomated) {
			isTestCaseAutomated = Boolean.valueOf(testCaseAutomated.value());
		}

		CaseObjectives classLevelCaseObjectives = clss
				.getAnnotation(CaseObjectives.class);

		if (null == TEMP_FORCE_ALL_TEST_RESULT) {
			TEMP_FORCE_ALL_TEST_RESULT = clss
					.getAnnotation(TempForceAllTestResult.class);
			if (null != TEMP_FORCE_ALL_TEST_RESULT) {
				if (logger.isWarnEnabled()) {
					logger.warn("\nPlease Notice : You mark all test result by annotation [@TempForceAllTestResult(Result."
							+ TEMP_FORCE_ALL_TEST_RESULT
							+ ")] ! \nIf you need to get actual test result , Please remove this Annotation");
				}
			}
		}

		CaseSuite caseSuite = clss.getAnnotation(CaseSuite.class);
		Owner moduleLevelQA = clss.getAnnotation(Owner.class);
		String parentModuleName = null;
		if (null != caseSuite) {
			parentModuleName = caseSuite.parent();
			suiteName = caseSuite.name();
			if (StringUtils.isNotBlank(suiteName)) {
				suiteTestFolder = TestCase.createTestCaseFromPool(suiteName,
						true, TestUHtmlReporter.class.getName());
				if (null != moduleLevelQA) {
					suiteTestFolder.setOwner(moduleLevelQA);
				}
			}
		}

		if (!HANDLED_CLASS_NAMES.contains(className)) {
			HANDLED_CLASS_NAMES.add(className);
			if (StringUtils.isNotBlank(parentModuleName)) {
				TestCase parentModuleNameFolder = TestCase
						.createTestCaseFromPool(parentModuleName, true,
								TestUHtmlReporter.class.getName());
				ROOT.addChild(parentModuleNameFolder);
				parentModuleNameFolder.addChild(suiteTestFolder);
			} else {
				ROOT.addChild(suiteTestFolder);
			}
			collectExtendedExporters(clss);
		}

		Method method = testngMethod.getConstructorOrMethod().getMethod();
		String keySeed = className;
		TestCaseAutomated methodLevelTestCaseAutomated = method
				.getAnnotation(TestCaseAutomated.class);
		if (null != methodLevelTestCaseAutomated) {
			isTestCaseAutomated = Boolean.valueOf(methodLevelTestCaseAutomated
					.value());
		}

		CaseObjectives methodLevelCaseObjectives = method
				.getAnnotation(CaseObjectives.class);

		CaseName caseName = method.getAnnotation(CaseName.class);
		if (null != caseName && StringUtils.isNoneBlank(caseName.value())) {
			ParentCaseName parentCaseName = method
					.getAnnotation(ParentCaseName.class);
			TestCase parentTestCase = null;
			if (null != parentCaseName
					&& StringUtils.isNotBlank(parentCaseName.value())) {
				parentTestCase = TestCase.createTestCaseFromPool(
						parentCaseName.value(), false, keySeed);
				if (null != suiteTestFolder) {
					suiteTestFolder.addChild(parentTestCase);
				} else {
					ROOT.addChild(parentTestCase);
				}
			}
			TestCase testCase = TestCase.createTestCaseFromPool(
					caseName.value(), false, keySeed);
			testCase.setTestCaseAutomated(isTestCaseAutomated);
			String[] caseObjectives = getMergedCaseObjectives(
					methodLevelCaseObjectives, classLevelCaseObjectives);
			testCase.setCaseObjectives(caseObjectives);
			if (null != parentTestCase) {
				parentTestCase.addChild(testCase);
			} else if (null != suiteTestFolder) {
				suiteTestFolder.addChild(testCase);
			} else {
				ROOT.addChild(testCase);
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
			// record test result
			TestCaseResults actualTestResult = TestCaseResults.newInstance();

			if (null == TEMP_FORCE_ALL_TEST_RESULT) {
				actualTestResult.setResult(Result.valueOf(tr.getStatus()));
			} else {
				actualTestResult.setResult(TEMP_FORCE_ALL_TEST_RESULT.value());
			}
			testCase.setTotalDuration(tr.getEndMillis() - tr.getStartMillis());
			StringBuilder msgBuilder = new StringBuilder();
			msgBuilder.append("test class: [").append(className).append("]\n")
					.append("test name: [").append(method.getName())
					.append("]\n").append("execute result: [")
					.append(actualTestResult.getResult()).append("]\n")
					.append("message: [").append(tr.getThrowable())
					.append("]\n");
			actualTestResult.setMsg(msgBuilder.toString());
			ActualResults actualResults = method
					.getAnnotation(ActualResults.class);
			if (null != actualResults) {
				actualTestResult.setActualResults(actualResults);
			}
			testCase.setActualResults(actualTestResult);

			// record logs
			List<String> logs = Reporter.getOutput(tr);
			StringBuilder logsBuilder = new StringBuilder();
			for (String log : logs) {
				logsBuilder.append(log).append("\n");
			}
			testCase.setLogs(logsBuilder.toString());

		}
	}

}
