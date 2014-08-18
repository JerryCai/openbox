package com.googlecode.openbox.testu.tester;

public interface TestCasesManager {
	
	void addTestCasesSelector(TestCasesSelector testCasesSelector);
	
	void addTestCasesExporter(InternTestCasesExporter exporter);
	
	void execute();

}
