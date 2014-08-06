package com.googlecode.openbox.testu.tester.samples.demo;

import com.googlecode.openbox.testu.tools.TestngSuiteBuilder;

public class TestNGTools {
	public static void main(String ...args) throws Exception{
		TestngSuiteBuilder.create(new DemoTestngSuite()).createTestngSuite();
	}

}
