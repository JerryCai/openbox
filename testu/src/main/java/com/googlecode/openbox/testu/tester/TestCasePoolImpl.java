package com.googlecode.openbox.testu.tester;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.testu.TestUException;

public final class TestCasePoolImpl implements TestCasePool {
	private static final Logger logger = LogManager.getLogger();
	private static final TestCase ROOT = TestCase.create("Test Cases");

	private Map<String, TestCase> testCasePool;
	private Map<String, Map<String, String>> treeRelations;
	private boolean _isCaseTreeBuild = false;

	private TestCasePoolImpl() {
		testCasePool = new HashMap<String, TestCase>();
		treeRelations = new HashMap<String, Map<String, String>>();
		testCasePool.put(ROOT.getName(), ROOT);
	}

	public static TestCasePool create() {
		return new TestCasePoolImpl();
	}

	@Override
	public TestCase createTestCase(String caseName) {
		String key = caseName;
		if (!testCasePool.containsKey(key)) {
			testCasePool.put(key, TestCase.create(key));
		} 
		else {
			if (logger.isDebugEnabled()) {
				logger.debug("The test case with caseName =["
						+ caseName
						+ "] has already been used , \nIf this case is not parent test case , You should double check your test case name to keep unique , \nIf it is parent test case , you can ignore this warning message !");
			}
		}
		return testCasePool.get(key);
	}

	public void addCaseTreeRelationship(String parentCaseName,
			String childCaseName) {
		if (null == parentCaseName || "".equals(parentCaseName.trim())) {
			parentCaseName = ROOT.getName();
		}
		if (!treeRelations.containsKey(parentCaseName)) {
			treeRelations.put(parentCaseName, new HashMap<String, String>());
		}
		Map<String, String> children = treeRelations.get(parentCaseName);
		children.put(childCaseName, "");
	}

	@Override
	public Map<String, TestCase> getTestCasePool() {
		return testCasePool;
	}

	public synchronized TestCase exportCaseTreeRoot() {
		if (!_isCaseTreeBuild) {

			for (String parentName : treeRelations.keySet()) {
				TestCase parent = testCasePool.get(parentName);
				if (null == parent) {
					throw TestUException
							.create("Code error as parentName=["
									+ parentName
									+ "] can't create before add case tree relationship, Please check code !");
				}

				Map<String, String> childCaseNames = treeRelations
						.get(parentName);
				for (String childCaseName : childCaseNames.keySet()) {
					TestCase child = testCasePool.get(childCaseName);
					if (null == child) {
						throw TestUException
								.create("Code error as test case ["
										+ childCaseName
										+ "] can't create before add case tree relationship!");
					}
					parent.addChildTestCase(child);
				}
			}
			for (String parentName : treeRelations.keySet()) {
				TestCase parent = testCasePool.get(parentName);
				if (ROOT != parent && null == parent.getParent()) {
					throw TestUException
							.create("parent test case / module --["
									+ parentName
									+ "] can't find in your test cases , Please add or modify and try again !");
				}
			}
			_isCaseTreeBuild = true;
		}
		return ROOT;
	}

}
