package com.googlecode.openbox.demo.performance.report;

import com.googlecode.openbox.common.IOUtils;
import com.googlecode.openbox.demo.performance.PerformanceDataGroup;

public class TextPerformanceReport extends AbstractPerformanceReport {

	public static TextPerformanceReport create(String title, String location) {
		return new TextPerformanceReport(title, location);
	}

	private TextPerformanceReport(String title, String location) {
		super(title, location);
	}

	@Override
	public void report(PerformanceDataGroup[] performanceDataGroups) {

		for (PerformanceDataGroup performanceDataGroup : performanceDataGroups) {
			IOUtils.appendContentToFile(getReportFile().getAbsolutePath(),
					performanceDataGroup.getPerformanceInfo());
		}
	}

	@Override
	public String getReportFileExtention() {
		return "txt";
	}

}
