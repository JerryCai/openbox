package com.googlecode.openbox.testu.tester.samples.demo;

import junit.framework.Assert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.googlecode.openbox.testu.AbstractTestCase;
import com.googlecode.openbox.testu.tester.Bugs;
import com.googlecode.openbox.testu.tester.CaseDescriptions;
import com.googlecode.openbox.testu.tester.CaseName;
import com.googlecode.openbox.testu.tester.CaseSuite;
import com.googlecode.openbox.testu.tester.ExpectedResults;
import com.googlecode.openbox.testu.tester.Owner;
import com.googlecode.openbox.testu.tester.ParentCaseName;
import com.googlecode.openbox.testu.tester.Preconditions;
import com.googlecode.openbox.testu.tester.Steps;
import com.googlecode.openbox.testu.tester.TestReportTitle;
@TestReportTitle("Test Demo Foo")
@CaseSuite(name="Foo")
@Owner(name = "Jerry Cai", id = "jerrycai", email = "jerrycai.cn@gmail.com")
public class DemoUsageParentSuiteTest extends AbstractTestCase{
	private static final Logger logger = LogManager.getLogger();

	
	public DemoUsageParentSuiteTest(String name) {
		super(name);
	}

	@Test
	@ParentCaseName("Add Foo 1")
	@CaseName("Add Foo 1 By Admin User")
	@CaseDescriptions({
			"Precondition:",
			"1. Admin User login System and get Token .",
			"Steps:",
			"1. Use Admin User Token to call API -addFoo .",
			"2. Check its response whether success . ",
			"3. If success , Check its return ID whether can get Foo By this ID .",
			"Expected Result: ", "1. Admin User can Add Foo 1 success .",
			"2. Get Foo by return ID can success ." })
	@Bugs({"http://bug.com/view/bug004","http://bug.com/view/bug003"})
	public void testAddFooByAdmin1() {
		logger.info("Step 1 :  login with admin user .");
		logger.info("Step 2 : Try to add the user jerry1 .");
		logger.info("Step 3 : try to get this added user by admin !");
		logger.info("Step 4 : logout ....");
		Assert.assertTrue("Demo failed by me ..... ",  false);

	}

	@Test
	@ParentCaseName("Add Foo 1")
	@CaseName("Add Foo 1 By Normal User")
	@CaseDescriptions({
			"Precondition:",
			"1. Normal User login System and get Token .",
			"Steps:",
			"1. Use Normal User Token to call API -addFoo .",
			"2. Check its response whether success . ",
			"3. If success , Check its return ID whether can get Foo By this ID .",
			"Expected Result: ", "1. Normal User can Add Foo 1 success .",
			"2. Get Foo by return ID can success ." })
	public void testAddFooByNormalUser1() {
		logger.info("Step 1 :  login with admin user .");
		logger.info("Step 2 : Try to add the user jerry1 .");
		logger.info("Step 3 : try to get this added user by admin !");
		logger.info("Step 4 : logout ....");
		Assert.assertTrue("Demo failed by me ..... ",  true);

	}

	@Test
	@ParentCaseName("Add Foo 1")
	@CaseName("Add Foo 1 By Anonymous User")
	@Preconditions({
		"1. Free User login System ",
		"2. Use free user login token to call API "
	})
	@Steps({
		"1. Use Anonymous User Token to call API -addFoo .",
		"2. Check its response whether success . "
	})
	@ExpectedResults({
		"1. Anonymous User can't Add Foo 1 and return failed with 401 .",
		"2. User can get this Add Foo 1 success ."
	})
	public void testAddFooByAnymousUser1() {
		logger.info("Step 1 :  login with admin user .");
		logger.info("Step 2 : Try to add the user jerry1 .");
		logger.info("Step 3 : try to get this added user by admin !");
		logger.info("Step 4 : logout ....");
		Assert.assertEquals("The ID doesn't matched !!!","uuyyyy", "xxxxxxx");
	}
	
	@Test
	@CaseName("Add Foo 1 By Free User")
	@CaseDescriptions({ "Precondition:",
			"1. Anonymous User login System and get Token .", "Steps:",
			"1. Use Anonymous User Token to call API -addFoo .",
			"2. Check its response whether success . ", "Expected Result: ",
			"1. Anonymous User can't Add Foo 1 and return failed with 401 ." })
	public void testAddFooByFreeUser1() {
		Assert.fail("This shouldn't failed ! it is bug !");
	}
	
	
	@Test
	@CaseName("Add Foo 1")
	@CaseDescriptions({ 
		"This is parent Test Cases ",
		"Precondition:",
			"1. Anonymous User login System and get Token ."})
	public void testAddFoo1() {
		for(int i =0 ; i<20;i++){
			logger.info("=============["+i+"?/,.<>`~!<br>@#$%^&*()-_+=;:'\"\\{]=============");
			logger.info("Step 1 :  login with admin user .");
			logger.info("Step 2 : Try to add the user jerry1 .");
			logger.info("Step 3 : try to get this added user by admin !");
			logger.info("Step 4 : logout ....");
		}
		

	}
	

}
