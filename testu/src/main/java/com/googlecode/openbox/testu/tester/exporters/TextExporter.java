package com.googlecode.openbox.testu.tester.exporters;

import com.googlecode.openbox.common.IOUtils;
import com.googlecode.openbox.common.context.CommonContext;
import com.googlecode.openbox.testu.tester.CaseDescriptions;
import com.googlecode.openbox.testu.tester.ExpectedResults;
import com.googlecode.openbox.testu.tester.InternTestCasesExporter;
import com.googlecode.openbox.testu.tester.Preconditions;
import com.googlecode.openbox.testu.tester.Steps;
import com.googlecode.openbox.testu.tester.TestCase;

public class TextExporter implements InternTestCasesExporter {

	private String exportLocalFile;

	private TextExporter(String exportLocalFile) {
		this.exportLocalFile = exportLocalFile;
	}

	public static TextExporter newInstance(String location) {
		return new TextExporter(location);
	}

	@Override
	public String export(TestCase root, CommonContext context) {
		exportIntern(root);
		return exportLocalFile;
	}

	private void exportIntern(TestCase testCase) {
		StringBuilder sb = new StringBuilder();
		int level = testCase.getCaseLevel();
		String caseName = testCase.getDisplayName();
		appendCaseLine(sb, level, caseName);
		CaseDescriptions caseDescriptions = testCase.getCaseDescriptions();
		if (null != caseDescriptions) {
			for (String description : caseDescriptions.value()) {
				appendCaseLine(sb, level + 1, description);
			}
		}

		Preconditions preconditions = testCase.getPreconditions();
		if (null != preconditions) {
			appendCaseLine(sb, level + 1, "Preconditions:");
			for (String precondition : preconditions.value()) {
				appendCaseLine(sb, level + 2, precondition);
			}
		}

		Steps steps = testCase.getSteps();
		if (null != steps) {
			appendCaseLine(sb, level + 1, "Steps:");
			for (String step : steps.value()) {
				appendCaseLine(sb, level + 2, step);
			}
		}

		ExpectedResults expectedResults = testCase.getExpectedResults();
		if (null != expectedResults) {
			appendCaseLine(sb, level + 1, "ExpectedResults:");
			for (String expectedResult : expectedResults.value()) {
				appendCaseLine(sb, level + 2, expectedResult);
			}
		}
		sb.append("\n");
		IOUtils.appendContentToFile(exportLocalFile, sb.toString());
		sb = null;
		for (TestCase childCase : testCase.getChildren()) {
			exportIntern(childCase);
		}
	}

	private void appendCaseLine(StringBuilder sb, int caseLevel, String line) {
		for (int i = 0; i < caseLevel; i++) {
			sb.append("  ");
		}
		sb.append(line).append("\n");
	}

}
