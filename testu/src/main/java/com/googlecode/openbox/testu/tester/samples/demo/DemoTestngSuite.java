package com.googlecode.openbox.testu.tester.samples.demo;

import com.googlecode.openbox.testu.TestngSuite;

public class DemoTestngSuite implements TestngSuite {

	@Override
	public String getSuiteName() {
		return "Demo Suite";
	}

	@Override
	public String getTestModuleName() {
		return "Demo Test";
	}

	@Override
	public String getTestPrefixName() {
		return "Demo";
	}

	@Override
	public String getTestPostfixName() {
		return "";
	}

	@Override
	public Class<?> getSuiteClass() {
		return getClass();
	}

	@Override
	public String getTestngXmlLocation() {
		return "TestngSuites";
	}

}
