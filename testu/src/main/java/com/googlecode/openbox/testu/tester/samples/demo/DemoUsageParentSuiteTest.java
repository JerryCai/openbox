package com.googlecode.openbox.testu.tester.samples.demo;

import org.testng.annotations.Test;

import com.googlecode.openbox.testu.tester.BugIDs;
import com.googlecode.openbox.testu.tester.CaseDescriptions;
import com.googlecode.openbox.testu.tester.CaseName;
import com.googlecode.openbox.testu.tester.CaseSuite;
import com.googlecode.openbox.testu.tester.ExpectedResults;
import com.googlecode.openbox.testu.tester.ParentCaseName;
import com.googlecode.openbox.testu.tester.Preconditions;
import com.googlecode.openbox.testu.tester.Steps;

@CaseSuite(name="Foo")
public class DemoUsageParentSuiteTest {

	
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
	@BugIDs({ "BUG0001", "BUG0005" })
	public void testAddFooByAdmin1() {

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
		
	}
	
	@Test
	@CaseName("Add Foo 1 By Free User")
	@CaseDescriptions({ "Precondition:",
			"1. Anonymous User login System and get Token .", "Steps:",
			"1. Use Anonymous User Token to call API -addFoo .",
			"2. Check its response whether success . ", "Expected Result: ",
			"1. Anonymous User can't Add Foo 1 and return failed with 401 ." })
	public void testAddFooByFreeUser1() {

	}
	
	
	@Test
	@CaseName("Add Foo 1")
	@CaseDescriptions({ 
		"This is parent Test Cases ",
		"Precondition:",
			"1. Anonymous User login System and get Token ."})
	public void testAddFoo1() {

	}
	

}
