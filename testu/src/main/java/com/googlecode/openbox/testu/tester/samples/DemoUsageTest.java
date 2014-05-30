package com.googlecode.openbox.testu.tester.samples;

import org.testng.annotations.Test;

import com.googlecode.openbox.testu.tester.BugIDs;
import com.googlecode.openbox.testu.tester.CaseDescription;
import com.googlecode.openbox.testu.tester.CaseModule;
import com.googlecode.openbox.testu.tester.CaseName;
import com.googlecode.openbox.testu.tester.QA;

@QA(name = "Jerry Cai", id = "jerrycai", email = "jerrycai.cn@gmail.com")
@CaseModule(name="AddFooModule",parent="Foo")
public class DemoUsageTest {

	@Test
	@CaseName("Add Foo By Admin User")
	@CaseDescription({
		"Precondition:",
			"1. Admin User login System and get Token .",
		"Steps:",
			"1. Use Admin User Token to call API -addFoo .",
			"2. Check its response whether success . " , 
			"3. If success , Check its return ID whether can get Foo By this ID .",
		"Expected Result: ",
			"1. Admin User can add Foo success .",
			"2. Get Foo by return ID can success ."
		})
	@BugIDs({"BUG0001", "BUG0005" })
	public void testAddFooByAdmin() {

	}
	
	@Test
	@CaseName("Add Foo By Normal User")
	@CaseDescription({
		"Precondition:",
			"1. Normal User login System and get Token .",
		"Steps:",
			"1. Use Normal User Token to call API -addFoo .",
			"2. Check its response whether success . " , 
			"3. If success , Check its return ID whether can get Foo By this ID .",
		"Expected Result: ",
			"1. Normal User can add Foo success .",
			"2. Get Foo by return ID can success ."
		})
	public void testAddFooByNormalUser() {

	}
	
	@Test
	@CaseName("Add Foo By Anonymous User")
	@CaseDescription({
		"Precondition:",
			"1. Anonymous User login System and get Token .",
		"Steps:",
			"1. Use Anonymous User Token to call API -addFoo .",
			"2. Check its response whether success . " , 
		"Expected Result: ",
			"1. Anonymous User can't add Foo and return failed with 401 ."
		})
	public void testAddFooByAnymousUser() {

	}
}
