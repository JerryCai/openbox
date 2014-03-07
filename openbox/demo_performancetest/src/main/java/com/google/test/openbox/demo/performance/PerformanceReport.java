package com.google.test.openbox.demo.performance;

public interface PerformanceReport {

	String getTitle();

	String getLocation();

	void report(PerformanceDataGroup[] performanceDataGroups);

}
