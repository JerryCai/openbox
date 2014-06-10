package com.googlecode.openbox.testu.tester;

public interface TestCasesManager {
	
	void addTestCasesSelector(TestCasesSelector testCasesSelector);
	
	void addTestCasesExporter(TestCasesExporter exporter);
	
	void execute();

}
