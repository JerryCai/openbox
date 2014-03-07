package com.google.test.openbox.demo.performance.report;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.text.DecimalFormat;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.google.test.openbox.demo.performance.PerformanceDataGroup;

public class JFreeChartPerformanceReport extends AbstractPerformanceReport {

	public static JFreeChartPerformanceReport create(String title,
			String location) {
		return new JFreeChartPerformanceReport(title, location);
	}

	public JFreeChartPerformanceReport(String title, String location) {
		super(title, location);

	}

	public static final float IMAGE_QUALITY = 1.0f;
	public static final int IMAGE_WIDTH = 900;
	public static final int IMAGE_HEIGHT = 700;

	@Override
	public void report(PerformanceDataGroup[] performanceDataGroups) {
		JFreeChart chart = createChart(getTitle(), performanceDataGroups);

		try {
			ChartUtilities.saveChartAsJPEG(getReportFile(), IMAGE_QUALITY,
					chart, IMAGE_WIDTH, IMAGE_HEIGHT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Just for windows usage , if use linux ssh , ignore this feature
		try {
			ApplicationFrame reportFrame = new ApplicationFrame(getTitle());
			ChartPanel chartpanel = new ChartPanel(chart, true, true, true,
					false, true);
			chartpanel
					.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
			reportFrame.setContentPane(chartpanel);
			reportFrame.pack();
			RefineryUtilities.centerFrameOnScreen(reportFrame);
			reportFrame.setVisible(true);
		} catch (Exception e) {
			
		}
	}

	@Override
	public String getReportFileExtention() {
		return "jpg";
	}

	private static JFreeChart createChart(String title,
			PerformanceDataGroup[] performanceDataGroups) {
		XYDataset xydataset = createSystemThroughputDataset(performanceDataGroups);

		NumberAxis numberaxisX = new NumberAxis("ThreadCount");
		numberaxisX.setAutoRangeIncludesZero(true);
		NumberAxis numberaxisY = new NumberAxis("Duration (ms)");
		numberaxisY.setAutoRangeIncludesZero(true);
		DecimalFormat decimalformat = new DecimalFormat("0");
		numberaxisX.setNumberFormatOverride(decimalformat);
		numberaxisY.setNumberFormatOverride(decimalformat);

		XYSplineRenderer xysplinerenderer = new XYSplineRenderer();
		XYPlot xyplot = new XYPlot(xydataset, numberaxisX, numberaxisY,
				xysplinerenderer);
		xyplot.setBackgroundPaint(Color.lightGray);
		xyplot.setDomainGridlinePaint(Color.white);
		xyplot.setRangeGridlinePaint(Color.white);
		xyplot.setRangePannable(false);

		JFreeChart jfreechart = new JFreeChart(title,
				JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
		jfreechart.setBackgroundPaint(Color.white);
		return jfreechart;
	}

	private static XYDataset createSystemThroughputDataset(
			PerformanceDataGroup[] performanceDataGroups) {
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		XYSeries systemThroughoutSeries = new XYSeries("System Throughput");

		XYSeries serverAverageSeries = new XYSeries("Server Average Duration");
		XYSeries clientAverageSeries = new XYSeries("Client Average Duration");

		int progressX = 0;
		for (PerformanceDataGroup performanceDataGroup : performanceDataGroups) {
			int groupCount = performanceDataGroup.getTotalCount();
			long groupDuration = performanceDataGroup.getGroupTimeLine()
					.getDuration();
			progressX = groupCount;
			systemThroughoutSeries.add(progressX, groupDuration);
			serverAverageSeries.add(progressX,
					performanceDataGroup.getServerAverageDuration());
			clientAverageSeries.add(progressX,
					performanceDataGroup.getClientAverageDuration());
		}
		xyseriescollection.addSeries(systemThroughoutSeries);
		xyseriescollection.addSeries(serverAverageSeries);
		xyseriescollection.addSeries(clientAverageSeries);
		return xyseriescollection;
	}

}
