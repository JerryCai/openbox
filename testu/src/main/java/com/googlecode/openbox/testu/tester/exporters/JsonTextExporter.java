package com.googlecode.openbox.testu.tester.exporters;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.openbox.common.IOUtils;
import com.googlecode.openbox.common.UtilsAPI;
import com.googlecode.openbox.common.context.CommonContext;
import com.googlecode.openbox.testu.tester.Bugs;
import com.googlecode.openbox.testu.tester.CaseDescriptions;
import com.googlecode.openbox.testu.tester.ExpectedResults;
import com.googlecode.openbox.testu.tester.InternTestCasesExporter;
import com.googlecode.openbox.testu.tester.Preconditions;
import com.googlecode.openbox.testu.tester.Owner;
import com.googlecode.openbox.testu.tester.Steps;
import com.googlecode.openbox.testu.tester.TestCase;
import com.googlecode.openbox.testu.tester.TestCaseResults;
import com.googlecode.openbox.testu.tester.TestCaseResults.Result;

public class JsonTextExporter implements InternTestCasesExporter {

	private String exportLocalFile;

	private JsonTextExporter(String exportLocalFile) {
		this.exportLocalFile = exportLocalFile;
	}

	public static JsonTextExporter newInstance(String location) {
		return new JsonTextExporter(location);
	}

	@Override
	public void export(TestCase root, CommonContext context) {
		TestCaseVO testCaseVO = convertToTestCaseVO(root);
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.setPrettyPrinting().create();
		String content = gson.toJson(testCaseVO);
		IOUtils.appendContentToFile(exportLocalFile, content);

	}

	private TestCaseVO convertToTestCaseVO(TestCase testCase) {

		if (null == testCase) {
			return null;
		}

		TestCaseVO testCaseVO = new TestCaseVO();
		testCaseVO.setName(testCase.getDisplayName());
		testCaseVO.setDescriptions(getCaseDescriptions(testCase));
		testCaseVO.setResult(getResult(testCase));
		testCaseVO.setDuration(getDuration(testCase));
		testCaseVO.setLogs(testCase.getLogs());
		testCaseVO.setOwner(getOwner(testCase));
		testCaseVO.setBugs(getBugs(testCase));
		List<TestCase> children = testCase.getChildren();
		if (null != children && children.size() > 0) {
			testCaseVO.setExpanded(true);
			testCaseVO.setLeaf(false);
			for (TestCase child : children) {
				TestCaseVO childTestCaseVO = convertToTestCaseVO(child);
				testCaseVO.addChild(childTestCaseVO);
			}
		} else {
			testCaseVO.setExpanded(true);
			testCaseVO.setLeaf(true);
		}
		return testCaseVO;
	}

	private String getOwner(TestCase testCase) {
		Owner owner = testCase.getOwner();
		if (null != owner) {
			return owner.name();
		}
		return "";
	}

	private String getResult(TestCase testCase) {
		TestCaseResults testCaseResult = testCase.getActualResults();
		if (null != testCaseResult) {
			Result result = testCaseResult.getResult();
			if (null == result) {
				result = Result.SKIP;
			}

			switch (result) {
			case SKIP:
				return "<font color='yellow'>" + result.name() + "</font>";
			case SUCCESS:
				return "<font color='green'>" + result.name() + "</font>";
			case FAILURE:
				return "<font color='red'>" + result.name() + "</font>";
			case SUCCESS_PERCENTAGE_FAILURE:
				return "<font color='green'>" + result.name() + "</font>";
			case STARTED:
				return "<font color='yellow'>" + result.name() + "</font>";

			}
		}
		return "<font color='yellow'>Pending</font>";
	}

	private String getDuration(TestCase testCase) {
		TestCaseResults testCaseResult = testCase.getActualResults();
		if (null != testCaseResult) {
			return testCase.getActualResults().getDuration();
		}
		return "";
	}

	private String getBugs(TestCase testCase) {
		StringBuilder builder = new StringBuilder();
		Bugs bugs = testCase.getBugs();
		if (null != bugs) {
			for (String bugLink : bugs.value()) {
				builder.append("<a href=\"").append(bugLink)
						.append("\" target=\"_blank\">")
						.append(UtilsAPI.getLastPath(bugLink))
						.append("</a><br>");
			}
		}
		return builder.toString();
	}

	private String getCaseDescriptions(TestCase testCase) {
		StringBuilder descriptionsBuilder = new StringBuilder();
		CaseDescriptions caseDescriptions = testCase.getCaseDescriptions();
		if (null != caseDescriptions) {
			for (String description : caseDescriptions.value()) {
				descriptionsBuilder.append(description).append("<br>");
			}
			descriptionsBuilder.append("<br>");
		}
		Preconditions preconditions = testCase.getPreconditions();
		if (null != preconditions) {
			descriptionsBuilder.append("<br>Preconditions : <br>");
			for (String precondition : preconditions.value()) {
				descriptionsBuilder.append(precondition).append("<br>");
			}
			descriptionsBuilder.append("<br>");
		}

		Steps steps = testCase.getSteps();
		if (null != steps) {
			descriptionsBuilder.append("<br>Steps : <br>");
			for (String step : steps.value()) {
				descriptionsBuilder.append(step).append("<br>");
			}
			descriptionsBuilder.append("<br>");
		}

		ExpectedResults expectedResults = testCase.getExpectedResults();
		if (null != expectedResults) {
			descriptionsBuilder.append("<br>Expected Results : <br>");
			for (String expectedResult : expectedResults.value()) {
				descriptionsBuilder.append(expectedResult).append("<br>");
			}
			descriptionsBuilder.append("<br>");
		}
		return descriptionsBuilder.toString();
	}

}
