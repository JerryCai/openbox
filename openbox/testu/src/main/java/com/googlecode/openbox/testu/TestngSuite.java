package com.googlecode.openbox.testu;

public interface TestngSuite {

	String getSuiteName();

	String getTestModuleName();

	String getTestPrefixName();

	String getTestPostfixName();

	Class<?> getSuiteClass();

	String getTestngXmlLocation();
}
