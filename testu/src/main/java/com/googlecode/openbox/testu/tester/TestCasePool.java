package com.googlecode.openbox.testu.tester;

import java.util.Map;

public interface TestCasePool {

	TestCase createTestCase(String caseName);

	Map<String, TestCase> getTestCasePool();

	void addCaseTreeRelationship(String parentCaseName, String childCaseName);

	TestCase exportCaseTreeRoot();

}