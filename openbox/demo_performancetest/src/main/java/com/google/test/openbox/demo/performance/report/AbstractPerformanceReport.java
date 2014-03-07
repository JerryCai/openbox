package com.google.test.openbox.demo.performance.report;

import java.io.File;

import com.google.test.openbox.common.IOUtils;
import com.google.test.openbox.demo.performance.PerformanceReport;

public abstract class AbstractPerformanceReport implements PerformanceReport {

	public static final String DEFAULT_REPORT_LOCATION = new File(".")
			.getAbsolutePath();
	public static final String PD_REPORTNAME = "/PerformanceData.";

	private String title;
	private String location;

	public AbstractPerformanceReport(String title, String location) {
		this.title = title;
		this.location = location;

	}

	public abstract String getReportFileExtention();

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getLocation() {
		return location;
	}

	public File getReportFile() {
		String location = getLocation();
		if (null == location) {
			location = DEFAULT_REPORT_LOCATION;
		}
		String fileName = PD_REPORTNAME + getReportFileExtention();
		File file = new File(location + fileName);
		IOUtils.autoCreateParentDirectory(file);
		return file;

	}
}
