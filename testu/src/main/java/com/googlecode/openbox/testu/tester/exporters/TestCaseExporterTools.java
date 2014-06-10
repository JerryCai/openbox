package com.googlecode.openbox.testu.tester.exporters;

import com.googlecode.openbox.testu.tester.DefaultTestCasesManager;
import com.googlecode.openbox.testu.tester.TestCasesManager;
import com.googlecode.openbox.testu.tester.samples.DemoTestCasesSelector;

public class TestCaseExporterTools {

	public static void main(String... args) {
		TestCasesManager tcm = DefaultTestCasesManager.newInstance();
		tcm.addTestCasesSelector(new DemoTestCasesSelector());
		tcm.addTestCasesExporter(TextExporter.newInstance("D:\\TestCases.txt"));
		tcm.execute();

	}

}