package com.googlecode.openbox.testu.tester.samples;

import com.googlecode.openbox.testu.tester.TestCasesSelector;

public class DemoTestCasesSelector implements TestCasesSelector {

	@Override
	public String prefix() {
		return "";
	}

	@Override
	public String postfix() {
		return "Test";
	}

}
