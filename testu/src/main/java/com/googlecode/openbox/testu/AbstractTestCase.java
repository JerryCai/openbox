package com.googlecode.openbox.testu;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import com.googlecode.openbox.common.DateHelper;
import com.googlecode.openbox.testu.tester.TestCasesExporters;
@TestCasesExporters({com.googlecode.openbox.testu.tester.samples.DemoExtendedExporter.class})
@Listeners({ com.googlecode.openbox.testu.tester.TestUHtmlReporter.class})
public abstract class AbstractTestCase extends TestCase {
	private static final Logger logger = LogManager.getLogger();

	public AbstractTestCase(String name) {
		super(name);
	}

	public void setUp() {
		try {
			super.setUp();
		} catch (Exception e) {
			throw TestUException.create("setup failed !!!", e);
		}
	}

	public void tearDown() {
		try {
			super.tearDown();
		} catch (Exception e) {
			throw TestUException.create("tear down failed !!!", e);
		}
	}

	@BeforeTest
	public void beforeTest() {
	}

	@AfterTest
	public void afterTest() {
	}

	private String getTestClassName() {
		String className = this.getClass().getName();
		return className.substring(className.lastIndexOf(".") + 1);
	}

	protected String getCaseName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "_Threads_" + Thread.currentThread().getId();
	}

	public void assertTestResult(TestResult testResult) {
		if (testResult.isSuccess()) {
			if (logger.isInfoEnabled()) {
				logger.info("\n\r^^^^^^^---test case [" + getTestClassName()
						+ "~~" + getCaseName() + "] passed---^^^^^^^^");
			}
		} else {
			logger.error("\n\r!!!!!!----test case [" + getTestClassName()
					+ "~~" + getCaseName() + "] failed---!!!!!!!!");
		}
		Assert.assertTrue(testResult.isSuccess(), testResult.getText());
	}

	protected String getTimeString() {
		return DateHelper.getTimeString();
	}

}