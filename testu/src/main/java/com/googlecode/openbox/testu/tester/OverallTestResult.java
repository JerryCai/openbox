package com.googlecode.openbox.testu.tester;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.uncommons.reportng.ReportNGUtils;

import com.googlecode.openbox.common.IOUtils;
import com.googlecode.openbox.common.UtilsAPI;
import com.googlecode.openbox.testu.tester.exporters.BugListVO;
import com.googlecode.openbox.testu.tester.exporters.BugListVO.BugStatus;

public class OverallTestResult {
	private static final ReportNGUtils HELPER = new ReportNGUtils();

	private String reportTitle = "Default Test Report";
	private List<SuiteResult> suites;
	private ResultRow overallRow;
	private List<BugListVO> bugList;

	public OverallTestResult() {
		this.suites = new LinkedList<SuiteResult>();
	}

	public SuiteResult addSuite(String suiteName) {
		SuiteResult suite = new SuiteResult(suiteName);
		return addSuite(suite);
	}

	public SuiteResult addSuite(SuiteResult suite) {
		suites.add(suite);
		return suite;
	}

	public synchronized ResultRow getTotalRow() {
		if (null == overallRow) {
			int totalPassed = 0;
			int totalSkipped = 0;
			int totalFailed = 0;
			long duration = 0;
			for (SuiteResult suite : suites) {
				for (ResultRow row : suite.getRows()) {
					totalPassed = totalPassed + row.getTotalPassed();
					totalSkipped = totalSkipped + row.getTotalSkipped();
					totalFailed = totalFailed + row.getTotalFailed();
					duration = duration + row.getDuration();
				}
			}
			int total = totalPassed + totalSkipped + totalFailed;
			overallRow = ResultRow.create("Total:&nbsp;&nbsp;&nbsp;&nbsp;"
					+ total, totalPassed, totalSkipped, totalFailed, duration);
		}
		return overallRow;
	}

	public String getHtmlReport() {
		StringBuilder htmlReportBuilder = new StringBuilder();
		ResultRow total = getTotalRow();
		int totalNum = total.getTotalPassed() + total.getTotalFailed()
				+ total.getTotalSkipped();

		htmlReportBuilder
				.append("<font size=\\\"4\\\">Test Report&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>"
						+ getPassRateProgressBar(total))
				.append("&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;Pass Rate: ");
		if (total.getPassRate() != 100) {
			htmlReportBuilder.append("<font color=red >")
					.append(total.getPassRate()).append("%").append("</font>");
		} else {
			htmlReportBuilder.append("<font color=green >")
					.append(total.getPassRate()).append("%").append("</font>");
		}
		htmlReportBuilder.append("&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;")
				.append("Duration: ")
				.append(HELPER.formatDuration(total.getDuration()))
				.append("&nbsp;s&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;")
				.append("Total: ").append(totalNum)
				.append("&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;")
				.append("Passed: ").append("<font color=green >")
				.append(total.getTotalPassed()).append("</font>")
				.append("&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;")
				.append("Skipped: ");
		if (total.getTotalSkipped() > 0) {
			htmlReportBuilder.append("<font color=red >")
					.append(total.getTotalSkipped()).append("</font>");
		} else {
			htmlReportBuilder.append("<font color=green >")
					.append(total.getTotalSkipped()).append("</font>");
		}
		htmlReportBuilder
				.append("&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;Failed: ");

		if (total.getTotalFailed() > 0) {
			htmlReportBuilder.append("<font color=red >")
					.append(total.getTotalFailed()).append("</font>");
		} else {
			htmlReportBuilder.append("<font color=green >")
					.append(total.getTotalFailed()).append("</font>");
		}
		addViewResultDetailsButton(htmlReportBuilder);
		addViewBugListButton(htmlReportBuilder);
		return htmlReportBuilder.toString();
	}

	private void addViewResultDetailsButton(StringBuilder htmlReportBuilder) {
		htmlReportBuilder
				.append("&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img style=\\\"cursor:pointer;\\\" alt=\\\"view overall test result\\\" src=\\\"resources/images/view_details_button.jpg\\\" onclick=\\\"Ext.Msg.show({title:'Overall Test Result',width:800,height:600,maximizable:true,maxWidth:1000,maxHeight:800,resizable:true,message: '");
		htmlReportBuilder
				.append("<table id=\\\\'OverallTestResultTable\\\\' style=\\\\\\'display:table\\\\' align=\\\\'center\\\\' class=\\\\'overviewTable\\\\'><tbody><tr><th class=\\\\'header suite\\\\' colspan=\\\\'6\\\\'><div class=\\\\'suiteLinks\\\\'></div>");
		htmlReportBuilder.append("Overall Summary");
		htmlReportBuilder
				.append("</th></tr><tr class=\\\\'columnHeadings\\\\'><td>&nbsp;</td><th>Duration</th><th>Passed</th><th>Skipped</th><th>Failed</th><th>Pass Rate</th></tr>");
		drawRow(htmlReportBuilder, getTotalRow());

		for (SuiteResult suite : suites) {
			htmlReportBuilder
					.append("<table id=\\\\'OverallTestResultTable\\\\' style=\\\\\\'display:table\\\\' align=\\\\'center\\\\' class=\\\\'overviewTable\\\\'><tbody><tr><th class=\\\\'header suite\\\\' colspan=\\\\'6\\\\'><div class=\\\\'suiteLinks\\\\'></div>");
			htmlReportBuilder.append(suite.getSuiteName());
			htmlReportBuilder
					.append("</th></tr><tr class=\\\\'columnHeadings\\\\'><td>&nbsp;</td><th>Duration</th><th>Passed</th><th>Skipped</th><th>Failed</th><th>Pass Rate</th></tr>");
			for (ResultRow row : suite.getRows()) {
				drawRow(htmlReportBuilder, row);
			}
		}
		htmlReportBuilder.append("</table>'});\\\" />");

	}

	private void addViewBugListButton(StringBuilder htmlReportBuilder) {
		htmlReportBuilder
				.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img style=\\\"cursor:pointer;\\\" alt=\\\"view all bug list\\\" src=\\\"resources/images/view_bugs.png\\\" onclick=\\\"Ext.Msg.show({title:'Bug List',width:800,height:600,maximizable:true,maxWidth:1000,maxHeight:800,resizable:true,message: '");
		htmlReportBuilder
				.append("<table id=\\\\'BugListTable\\\\' style=\\\\\\'display:table\\\\' align=\\\\'center\\\\' class=\\\\'overviewTable\\\\'><tbody><tr><th class=\\\\'header suite\\\\' colspan=\\\\'6\\\\'><div class=\\\\'suiteLinks\\\\'></div>");
		htmlReportBuilder.append("Bug Report");
		htmlReportBuilder
				.append("</th></tr><tr class=\\\\'columnHeadings\\\\'><th>Bug Link</th><th>Status</th><th>Owner</th></tr>");
		if (null != bugList) {
			for (BugListVO bugListVO : getMergedBugList().values()) {
				drawBugRow(htmlReportBuilder, bugListVO);
			}
		}
		htmlReportBuilder.append("</table>'});\\\" />");

	}

	private String getPassRateProgressBar(ResultRow total) {
		return "<progress value=" + total.getPassRate()
				+ " max=100></progress>";
	}

	private void drawRow(StringBuilder htmlReportBuilder, ResultRow row) {
		htmlReportBuilder
				.append("<tr class=\\\\'test\\\\'><td class=\\\\'test\\\\'>")
				.append(row.getTestName())
				.append("</td><td class=\\\\'duration\\\\'>")
				.append(HELPER.formatDuration(row.getDuration()))
				.append("&nbsp;s</td><td class=\\\\'passed number\\\\'>")
				.append(row.getTotalPassed());
		if (row.getTotalSkipped() > 0) {
			htmlReportBuilder
					.append("</td> <td class=\\\\'skipped number\\\\'>");
		} else {
			htmlReportBuilder.append("</td> <td class=\\\\'zero number\\\\'>");
		}
		htmlReportBuilder.append(row.getTotalSkipped());
		if (row.getTotalFailed() > 0) {
			htmlReportBuilder.append("</td><td class=\\\\'failed number\\\\'>");
		} else {
			htmlReportBuilder.append("</td> <td class=\\\\'zero number\\\\'>");
		}
		htmlReportBuilder.append(row.getTotalFailed())
				.append("</td><td class=\\\\'passRate\\\\'>")
				.append(row.getPassRate()).append("%</td></tr>");
	}

	private void drawBugRow(StringBuilder htmlReportBuilder, BugListVO bugListVO) {
		String bugLink = bugListVO.getBugLink();

		htmlReportBuilder
				.append("<tr class=\\\\'test\\\\'><td class=\\\\'test\\\\'>")
				.append("<a href=\\\\'").append(bugLink)
				.append("\\\\' target=\\\\'_blank\\\\'>")
				.append(UtilsAPI.getLastPath(bugLink)).append("</a></td>");

		BugStatus bugStatus = bugListVO.getBugStatus();
		switch (bugStatus) {
		case Fixed:
			htmlReportBuilder.append("<td class=\\\\'passed number\\\\'>");
			break;
		case Open:
			htmlReportBuilder.append("<td class=\\\\'failed number\\\\'>");
			break;
		default:
			htmlReportBuilder.append("<td class=\\\\'skipped number\\\\'>");

		}

		htmlReportBuilder.append(bugStatus.name()).append("</td>");

		htmlReportBuilder.append("<td class=\\\\'duration\\\\'>")
				.append(bugListVO.getOwner()).append("</td></tr>");

	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public static class SuiteResult {
		private String suiteName;
		private List<ResultRow> rows;

		public SuiteResult(String suiteName) {
			this.rows = new LinkedList<ResultRow>();
			this.suiteName = suiteName;
		}

		public void addRow(ResultRow row) {
			rows.add(row);
		}

		public String getSuiteName() {
			return suiteName;
		}

		public List<ResultRow> getRows() {
			return rows;
		}

	}

	public static class ResultRow {
		private String testName;
		private int totalPassed = 0;
		private int totalSkipped = 0;
		private int totalFailed = 0;
		private long duration;
		private int passRate;

		private ResultRow() {

		}

		public static ResultRow create(String testName, int totalPassed,
				int totalSkipped, int totalFailed, long duration) {
			ResultRow instance = new ResultRow();
			instance.setTestName(testName);
			instance.setTotalPassed(totalPassed);
			instance.setTotalFailed(totalFailed);
			instance.setTotalSkipped(totalSkipped);
			instance.setDuration(duration);
			int totalTests = totalPassed + totalFailed + totalSkipped;
			int passRate = 100;
			if (totalTests > 0) {
				passRate = (totalPassed * 100) / totalTests;
			}
			instance.setPassRate(passRate);
			return instance;

		}

		public String getTestName() {
			return testName;
		}

		public void setTestName(String testName) {
			this.testName = testName;
		}

		public int getTotalPassed() {
			return totalPassed;
		}

		public void setTotalPassed(int totalPassed) {
			this.totalPassed = totalPassed;
		}

		public int getTotalSkipped() {
			return totalSkipped;
		}

		public void setTotalSkipped(int totalSkipped) {
			this.totalSkipped = totalSkipped;
		}

		public int getTotalFailed() {
			return totalFailed;
		}

		public void setTotalFailed(int totalFailed) {
			this.totalFailed = totalFailed;
		}

		public long getDuration() {
			return duration;
		}

		public void setDuration(long duration) {
			this.duration = duration;
		}

		public int getPassRate() {
			return passRate;
		}

		public void setPassRate(int passRate) {
			this.passRate = passRate;
		}
	}

	public List<BugListVO> getBugList() {
		return bugList;
	}

	public void setBugList(List<BugListVO> bugList) {
		this.bugList = bugList;
	}
	
	public Map<String,BugListVO> getMergedBugList(){
		Map<String,BugListVO> mergedBugList = new LinkedHashMap<String,BugListVO>();
		for(BugListVO bugVO : bugList){
			String key = bugVO.getBugLink();
			if(!mergedBugList.containsKey(key) ){
				mergedBugList.put(key, bugVO);
			}else if(bugVO.getBugStatus() != BugStatus.Fixed){
				switch(mergedBugList.get(key).getBugStatus() ){
					case Fixed :
					case Skiped:
						mergedBugList.put(key, bugVO);
						break;
				default:
					break;
							
				}
			}
		}
		return mergedBugList;
	}
}
