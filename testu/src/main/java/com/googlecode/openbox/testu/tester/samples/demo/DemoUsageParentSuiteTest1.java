package com.googlecode.openbox.testu.tester.samples.demo;

import junit.framework.Assert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.testu.AbstractTestCase;
import com.googlecode.openbox.testu.tester.CaseSuite;

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


	public void testAddFooByNormalUser1() {
		logger.info("Step 1 :  login with admin user .");
		logger.info("Step 2 : Try to add the user jerry1 .");
		logger.info("Step 3 : try to get this added user by admin !");
		logger.info("Step 4 : logout ....");
		Assert.assertTrue("Demo failed by me ..... ",  true);

	}


	public void testAddFooByAnymousUser1() {
		logger.info("Step 1 :  login with admin user .");
		logger.info("Step 2 : Try to add the user jerry1 .");
		logger.info("Step 3 : try to get this added user by admin !");
		logger.info("Step 4 : logout ....");
		Assert.assertEquals("The ID doesn't matched !!!","uuyyyy", "xxxxxxx");
	}
	

	public void testAddFooByFreeUser1() {
		Assert.fail("This shouldn't failed ! it is bug !");
	}
	

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
