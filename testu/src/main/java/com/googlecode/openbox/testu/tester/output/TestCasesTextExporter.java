package com.googlecode.openbox.testu.tester.output;

import com.googlecode.openbox.testu.tester.TestCasesExporter;

public class TestCasesTextExporter implements TestCasesExporter {

	private String location;
	
	private TestCasesTextExporter(String location){
		this.location = location;
	}
	
	
	@Override
	public void exportTestCases(String... testngSuiteFiles) {
		
		
	}

}
