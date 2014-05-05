package com.googlecode.openbox.demo.performance;

import com.googlecode.openbox.common.ToolUtils;
import com.googlecode.openbox.demo.performance.report.JFreeChartPerformanceReport;
import com.googlecode.openbox.demo.performance.report.TextPerformanceReport;

public class CommandMain {

	public static void main(String... args) {

		String url;
		String dcName;

		int requiredArgs = 2;

		if (args.length < requiredArgs) {
			ToolUtils.printUsage();
			return;
		}
		url = args[0].trim();
		dcName = args[1].trim();
		int from = 1;
		int to = 201;
		int step = 10;

		if (args.length <= 5) {
			for (int i = 2; i < args.length; i++) {

				try {
					int value = Integer.parseInt(args[i]);
					switch (i) {
					case 2: {
						from = value;
						break;
					}
					case 3: {
						to = value;
						break;
					}
					case 4: {
						step = value;
						break;
					}

					}
				} catch (Exception e) {
					ToolUtils.printUsage();
					e.printStackTrace();
					return;
				}
			}
		} else {
			ToolUtils.printUsage();
			return;
		}

		int[] groups = RunManager.generateGroups(from, to, step);

		RunManager runManager = RunManager.create(url, dcName, groups);
		runManager.addReporter(TextPerformanceReport.create(null, null));
		runManager.addReporter(JFreeChartPerformanceReport.create(
				"Performance Report", null));
		runManager.start();
	}

}
