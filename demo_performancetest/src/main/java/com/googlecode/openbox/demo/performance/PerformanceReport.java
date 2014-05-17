package com.googlecode.openbox.demo.performance;

public interface PerformanceReport {

	String getTitle();

	String getLocation();

	void report(PerformanceDataGroup[] performanceDataGroups);

}
