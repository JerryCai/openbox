package com.googlecode.openbox.testu.tester.samples;

import java.util.concurrent.TimeUnit;

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
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("I am executed success !!!");
	}

}
