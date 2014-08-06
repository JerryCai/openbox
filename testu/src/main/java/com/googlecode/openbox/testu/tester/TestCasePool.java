package com.googlecode.openbox.testu.tester;

import java.util.Map;

public interface TestCasePool {
	
	String getTestCaseTitle();
	
	void setTestCaseTitle(String poolTitle);
		
	TestCase createTestCase(String caseName);

	Map<String, TestCase> getTestCasePool();

	void addCaseTreeRelationship(String parentCaseName, String childCaseName);

	TestCase exportCaseTreeRoot();

}