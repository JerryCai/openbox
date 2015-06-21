package com.googlecode.openbox.testu;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import com.googlecode.openbox.common.DateHelper;

@Listeners({ com.googlecode.openbox.testu.tester.TestUHtmlReporter.class })
public abstract class AbstractTestCase extends TestCase {
	private static final Logger logger = LogManager.getLogger();

	public AbstractTestCase(String name) {
		super(name);
	}

	public void setUp() throws Exception {
		super.setUp();
		this.beforeSuite();
		this.beforeGroups();
		this.beforeTest();
		this.beforeClass();
		this.beforeMethod();
	}

	public void tearDown() throws Exception {
		super.tearDown();
		this.afterMethod();
		this.afterClass();
		this.afterTest();
		this.afterGroups();
		this.afterSuite();
	}

	@BeforeSuite
	public void beforeSuite() {
	}

	@AfterSuite
	public void afterSuite() {
	}

	@BeforeGroups
	public void beforeGroups() {

	}

	@AfterGroups
	public void afterGroups() {

	}

	@BeforeTest
	public void beforeTest() {
	}

	@AfterTest
	public void afterTest() {
	}

	@BeforeClass
	public void beforeClass() {

	}

	@AfterClass
	public void afterClass() {

	}

	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
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