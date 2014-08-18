package com.googlecode.openbox.testu.tester.samples;

import com.googlecode.openbox.testu.tester.TestCase;
import com.googlecode.openbox.testu.tester.TestCasesExporter;

public class DemoExtendedExporter implements TestCasesExporter {
	
	public DemoExtendedExporter(){
		System.out.println("I am Created success !!!");
	}

	public DemoExtendedExporter(int str){
		System.out.println("I am Created success ["+str+"]!!!");
	}
	
	@Override
	public void export(TestCase root) {
		System.out.println("I am executed success !!!");
	}

}
