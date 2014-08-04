package com.googlecode.openbox.testu.tester;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.googlecode.openbox.testu.tester.exporters.HtmlTextExporter;

public class TestUHtmlReporter implements IReporter {
	private static final ReportNGUtils HELPER = new ReportNGUtils();
	private TestCasePool testCasePool;
	private Set<String> handledClassNames;
	private HTMLReporter reportNG = new HTMLReporter();

	public TestUHtmlReporter() {
		this.testCasePool = TestCasePoolImpl.create();
		this.handledClassNames = new HashSet<String>();
	}

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectoryName) {
		// integration with reportNG
		reportNG.generateReport(xmlSuites, suites, outputDirectoryName);
		ITestContext textContext = null;
		OverallTestResult overallTestResult = new OverallTestResult();
		
		for (ISuite suite : suites) {
			SuiteResult suiteResult = overallTestResult.addSuite(suite.getName());
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
				suiteResult.addRow(ResultRow.create(textContext.getName(), passedTests.size(), skippedTests.size(), failedTests.size(),HELPER.getDuration(textContext)));
			}
		}
		if (null != textContext) {
			exportHtmlTestReport(textContext,overallTestResult);
		}
	}

	private void processResults(IResultMap resultMap) {
		for (ITestResult tr : resultMap.getAllResults()) {
			process(tr);
		}
	}

	public void exportHtmlTestReport(ITestContext testContext,OverallTestResult overallTestResult) {
		String testngOutputPath = testContext.getOutputDirectory();
		TestCasesExporter htmlTextExporter = HtmlTextExporter
				.newInstance(UtilsAPI.getParentPath(testngOutputPath,
						IOUtils.PATH_SPLIT)
						+ IOUtils.PATH_SPLIT
						+ "testu"
						+ IOUtils.PATH_SPLIT + "testreport");
		CommonContext context = new BasicContext();
		context.setAttribute(HtmlTextExporter.CONTEXT_ID, overallTestResult);
		htmlTextExporter.export(testCasePool,context);
	}

	private void process(ITestResult tr) {
		ITestNGMethod testngMethod = tr.getMethod();
		Class<?> clss = testngMethod.getRealClass();
		String className = clss.getName();
		String suiteName = null;
		CaseSuite caseSuite = clss.getAnnotation(CaseSuite.class);
		if (null != caseSuite) {
			suiteName = caseSuite.name();
		}

		if (!handledClassNames.contains(className)) {
			handledClassNames.add(className);
			if (null != suiteName) {
				testCasePool.createTestCase(suiteName);
				String parentModuleName = caseSuite.parent();
				if (null != parentModuleName) {
					testCasePool.createTestCase(parentModuleName);
				}
				testCasePool.addCaseTreeRelationship(parentModuleName,
						suiteName);
			}

		}
		QA moduleLevelQA = clss.getAnnotation(QA.class);

		Method method = testngMethod.getConstructorOrMethod().getMethod();

		CaseName caseName = method.getAnnotation(CaseName.class);
		if (null != caseName) {
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
