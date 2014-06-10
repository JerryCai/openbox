package com.googlecode.openbox.testu.tester.samples.demo;

import org.testng.annotations.Test;

import com.googlecode.openbox.testu.tester.BugIDs;
import com.googlecode.openbox.testu.tester.CaseDescriptions;
import com.googlecode.openbox.testu.tester.CaseName;
import com.googlecode.openbox.testu.tester.CaseSuite;
import com.googlecode.openbox.testu.tester.ExpectedResults;
import com.googlecode.openbox.testu.tester.ParentCaseName;
import com.googlecode.openbox.testu.tester.Preconditions;
import com.googlecode.openbox.testu.tester.QA;
import com.googlecode.openbox.testu.tester.Steps;

@CaseSuite(name="AddFooModule",parent="Foo")
@QA(name = "Jerry Cai", id = "jerrycai", email = "jerrycai.cn@gmail.com")
public class DemoUsageTest {

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
	@BugIDs({ "BUG0001", "BUG0005" })
	public void testAddFooByAdmin() {

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
	public void testAddFooByNormalUser() {

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
		
	}
	
	@Test
	@CaseName("Add Foo By Free User")
	@CaseDescriptions({ "Precondition:",
			"1. Anonymous User login System and get Token .", "Steps:",
			"1. Use Anonymous User Token to call API -addFoo .",
			"2. Check its response whether success . ", "Expected Result: ",
			"1. Anonymous User can't add Foo and return failed with 401 ." })
	public void testAddFooByFreeUser() {

	}
	
	
	@Test
	@CaseName("Add Foo")
	@CaseDescriptions({ 
		"This is parent Test Cases ",
		"Precondition:",
			"1. Anonymous User login System and get Token ."})
	public void testAddFoo() {

	}
}
