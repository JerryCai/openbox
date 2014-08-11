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
import com.googlecode.openbox.testu.tester.ParentCaseName;
import com.googlecode.openbox.testu.tester.Preconditions;
import com.googlecode.openbox.testu.tester.QA;
import com.googlecode.openbox.testu.tester.Steps;
@CaseSuite(name="AddFooModule",parent="Foo")
public class DemoUsageTest extends AbstractTestCase{
	private static final Logger logger = LogManager.getLogger();

	public DemoUsageTest(String name) {
		super(name);
	}

	@Test
	@ParentCaseName("Add Foo")
	@CaseName("Add Foo By Admin User")
	@CaseDescriptions({
			"Precondition:",
			"1. Admin User login System and get Token .",
			"Steps:",
			"1. Use Admin User Token to call API -addFoo .",
			"2. Check its response whether success . ",
			"3. If success , Check its return ID whether can get Foo By this ID .",
			"Expected Result: ", "1. Admin User can add Foo success .",
			"2. Get Foo by return ID can success ." })
	@QA(name = "Jerry Cai", id = "jerrycai", email = "jerrycai.cn@gmail.com")
	@Bugs({"http://bug.com/view/bug004","http://bug.com/view/bug003"})
	public void testAddFooByAdmin() {
		logger.info("Step 1 :  login with admin user .");
		logger.info("Step 2 : Try to add the user jerry1 .");
		logger.info("Step 3 : try to get this added user by admin !");
		logger.info("Step 4 : logout ....");

	}

	@Test
	@ParentCaseName("Add Foo")
	@CaseName("Add Foo By Normal User")
	@CaseDescriptions({
			"Precondition:",
			"1. Normal User login System and get Token .",
			"Steps:",
			"1. Use Normal User Token to call API -addFoo .",
			"2. Check its response whether success . ",
			"3. If success , Check its return ID whether can get Foo By this ID .",
			"Expected Result: ", "1. Normal User can add Foo success .",
			"2. Get Foo by return ID can success ." })
	@Bugs({"http://bug.com/view/bug002","http://bug.com/view/bug005"})
	public void testAddFooByNormalUser() {
		Assert.assertEquals("The ID doesn't matched !!!",40, 50);


	}

	@Test
	@ParentCaseName("Add Foo")
	@CaseName("Add Foo By Anonymous User")
	@Preconditions({
		"1. Free User login System ",
		"2. Use free user login token to call API "
	})
	@Steps({
		"1. Use Anonymous User Token to call API -addFoo .",
		"2. Check its response whether success . "
	})
	@ExpectedResults({
		"1. Anonymous User can't add Foo and return failed with 401 .",
		"2. User can get this add foo success ."
	})
	public void testAddFooByAnymousUser() {
		Assert.assertEquals("The ID doesn't matched !!!",true, false);

		
	}
	
	@Test
	@CaseName("Add Foo By Free User")
	@CaseDescriptions({ "Precondition:",
			"1. Anonymous User login System and get Token .", "Steps:",
			"1. Use Anonymous User Token to call API -addFoo .",
			"2. Check its response whether success . ", "Expected Result: ",
			"1. Anonymous User can't add Foo and return failed with 401 ." })
	public void testAddFooByFreeUser() {
		logger.info("Step 1 :  login with admin user .");
		logger.info("Step 2 : Try to add the user jerry1 .");
		logger.info("Step 3 : try to get this added user by admin !");
		logger.info("Step 4 : logout ....");

	}
	
	
	@Test
	@CaseName("Add Foo")
	@CaseDescriptions({ 
		"This is parent Test Cases ",
		"Precondition:",
			"1. Anonymous User login System and get Token ."})
	@Bugs({"http://bug.com/view/bug001","http://bug.com/view/bug009"})
	public void testAddFoo() {

	}
}
