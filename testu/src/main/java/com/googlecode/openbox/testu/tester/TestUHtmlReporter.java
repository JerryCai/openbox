package com.googlecode.openbox.testu.tester;

import java.lang.reflect.Method;
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
	private static final String REPORT_TITLE="Test Cases";
	private static final TestCase 	ROOT = TestCase.createTestCaseFromPool(REPORT_TITLE,true);

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
		//by default , reportNG is auto registed too , This is more easy to use.
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
		htmlTextExporter.export(ROOT, context);
		if (logger.isInfoEnabled()) {
			logger.info("TestU Report done , report location is [" + reportPath
					+ "]");
		}

	}

	private void executeExtendedExporters() {
		for (TestCasesExporter exporter : EXPORTERS.values()) {
			try {
				exporter.export(ROOT);
				if (logger.isInfoEnabled()) {
					logger.info("your registed TestCaseExporter-["
							+ exporter.getClass().getName() + "] is executed success !");
				}
			} catch (Exception e) {
				logger.error("your registed TestCaseExporter-["
						+ exporter.getClass().getName() + "] execute error , please check its implementation !", e);
			}
		}
	}

	private void collectExtendedExporters(Class<?> clss) {
		TestCasesExporters extendedTestCasesExporters = clss.getDeclaredAnnotation(TestCasesExporters.class);
		if(null == extendedTestCasesExporters){
			Class<?> superClass = clss.getSuperclass();
			if(null != superClass ){
				collectExtendedExporters(superClass);
			}
		
		}else{
			for (Class<? extends TestCasesExporter> exporterClass : extendedTestCasesExporters
					.value()) {
				String className = exporterClass.getName();
				if (EXPORTERS.containsKey(className)|| ERROR_EXPORTER_RECORDS.contains(className)) {
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

	private void process(ITestResult tr) {
		ITestNGMethod testngMethod = tr.getMethod();
		Class<?> clss = testngMethod.getRealClass();
		String className = clss.getName();
		String suiteName = null;
		TestCase suiteTestFolder = null;
		TestReportTitle testReportTitle = clss
				.getAnnotation(TestReportTitle.class);
		if (null != testReportTitle) {
			String reportTitle = testReportTitle.value();
			if (!StringUtils.isBlank(reportTitle)) {
				ROOT.setDisplayName(reportTitle);
			}
		}
		CaseSuite caseSuite = clss.getAnnotation(CaseSuite.class);
		if (null != caseSuite) {
			suiteName = caseSuite.name();
			if (StringUtils.isNotBlank(suiteName)) {
				suiteTestFolder = TestCase.createTestCaseFromPool(suiteName,true);
			}
		}

		if (!HANDLED_CLASS_NAMES.contains(className)) {
			HANDLED_CLASS_NAMES.add(className);
			String parentModuleName = caseSuite.parent();
			if (StringUtils.isNotBlank(parentModuleName)) {
				TestCase parentModuleNameFolder = TestCase.createTestCaseFromPool(parentModuleName,true);
				ROOT.addChild(parentModuleNameFolder);
				parentModuleNameFolder.addChild(suiteTestFolder);
			}else{
				ROOT.addChild(suiteTestFolder);
			}
			collectExtendedExporters(clss);
		}
		QA moduleLevelQA = clss.getAnnotation(QA.class);

		Method method = testngMethod.getConstructorOrMethod().getMethod();

		CaseName caseName = method.getAnnotation(CaseName.class);
		if (null != caseName && StringUtils.isNoneBlank(caseName.value())) {
			ParentCaseName parentCaseName = method
					.getAnnotation(ParentCaseName.class);
			TestCase parentTestCase = null;
			if (null != parentCaseName && StringUtils.isNotBlank(parentCaseName.value())) {
				parentTestCase = TestCase.createTestCaseFromPool(parentCaseName.value(),false);
				suiteTestFolder.addChild(parentTestCase);
			}
			TestCase testCase = TestCase.createTestCaseFromPool(caseName.value(),false);
			if(null != parentTestCase){
				parentTestCase.addChild(testCase);
			}else{
				suiteTestFolder.addChild(testCase);
			}
			
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
			testCase.setExpectedResults(method.getAnnotation(ExpectedResults.class));

			Bugs bugs = method.getAnnotation(Bugs.class);
			if (null != bugs) {
				testCase.setBugs(bugs);
			}
			// record test result
			TestCaseResults actualTestResult = TestCaseResults.newInstance();
			actualTestResult.setResult(Result.valueOf(tr.getStatus()));
			actualTestResult.setDuration(""
					+ (tr.getEndMillis() - tr.getStartMillis()) + " ms");
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
