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
import com.googlecode.openbox.testu.tester.ParentCaseName;
import com.googlecode.openbox.testu.tester.QA;

@CaseSuite(name="Foo1",parent="Foo")
public class DemoUsageParentSuiteTest1 extends AbstractTestCase{
	private static final Logger logger = LogManager.getLogger();

	
	public DemoUsageParentSuiteTest1(String name) {
		super(name);
	}


	public void testAddFooByAdmin1() {
		logger.info("Step 1 :  login with admin user .");
		logger.info("Step 2 : Try to add the user jerry1 .");
		logger.info("Step 3 : try to get this added user by admin !");
		logger.info("Step 4 : logout ....");
		Assert.assertTrue("Demo failed by me ..... ",  false);

	}
	@Test
	@ParentCaseName("Add Foo")
	@CaseName("Add Foo By Normal User xxxx")
	@CaseDescriptions({
			"Precondition:",
			"1. Normal User login System and get Token .",
			"Steps:",
			"1. Use Normal User Token to call API -addFoo .",
			"2. Check its response whether success . ",
			"3. If success , Check its return ID whether can get Foo By this ID .",
			"Expected Result: ", "1. Normal User can add Foo success .",
			"2. Get Foo by return ID can success ." })
	@Bugs({"http://bug.com/view/bug004","http://bug.com/view/bug003"})
	public void testAddFooByNormalUser1() {
		logger.info("Step 1 :  login with admin user .");
		logger.info("Step 2 : Try to add the user jerry1 .");
		logger.info("Step 3 : try to get this added user by admin !");
		logger.info("Step 4 : logout ....");
		Assert.assertTrue("Demo failed by me ..... ",  true);

	}
	@Test
	@ParentCaseName("Add Foo")
	@CaseName("Add Foo By Normal User xxxx11")
	@CaseDescriptions({
			"Precondition:",
			"1. Normal User login System and get Token .",
			"Steps:",
			"1. Use Normal User Token to call API -addFoo .",
			"2. Check its response whether success . ",
			"3. If success , Check its return ID whether can get Foo By this ID .",
			"Expected Result: ", "1. Normal User can add Foo success .",
			"2. Get Foo by return ID can success ." })
	@Bugs({"http://bug.com/view/bug011","http://bug.com/view/bug012"})
	@QA(name = "Jerry Cai", id = "jerrycai", email = "jerrycai.cn@gmail.com")
	public void testAddFooByAnymousUser1() {
		logger.info("Step 1 :  login with admin user .");
		logger.info("Step 2 : Try to add the user jerry1 .");
		logger.info("Step 3 : try to get this added user by admin !");
		logger.info("Step 4 : logout ....");
		Assert.assertEquals("The ID doesn't matched !!!","uuyyyy", "xxxxxxx");
	}
	
	@Test
	@ParentCaseName("Add Foo")
	@CaseName("Add Foo By Normal User xxxx22")
	@CaseDescriptions({
			"Precondition:",
			"1. Normal User login System and get Token .",
			"Steps:",
			"1. Use Normal User Token to call API -addFoo .",
			"2. Check its response whether success . ",
			"3. If success , Check its return ID whether can get Foo By this ID .",
			"Expected Result: ", "1. Normal User can add Foo success .",
			"2. Get Foo by return ID can success ." })
	@Bugs({"http://bug.com/view/bug011","http://bug.com/view/bug012"})
	public void testAddFooByFreeUser1() {
		Assert.fail("This shouldn't failed ! it is bug !");
	}
	@Test
	@CaseName("Add Foo By Normal User xxxx33")
	@CaseDescriptions({
			"Precondition:",
			"1. Normal User login System and get Token .",
			"Steps:",
			"1. Use Normal User Token to call API -addFoo .",
			"2. Check its response whether success . ",
			"3. If success , Check its return ID whether can get Foo By this ID .",
			"Expected Result: ", "1. Normal User can add Foo success .",
			"2. Get Foo by return ID can success ." })
	@Bugs({"http://bug.com/view/bug055","http://bug.com/view/bug066"})
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
