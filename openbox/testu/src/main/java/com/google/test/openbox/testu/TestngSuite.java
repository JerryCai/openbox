package com.google.test.openbox.testu;

public interface TestngSuite {
	
	String getSuiteName();
	String getTestModuleName();
	String getTestPrefixName();
	Class<?> getSuiteClass();
	String getTestngXmlLocation();
}
