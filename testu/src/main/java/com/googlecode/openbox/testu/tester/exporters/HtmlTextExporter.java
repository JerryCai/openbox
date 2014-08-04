package com.googlecode.openbox.testu.tester.exporters;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.openbox.common.IOUtils;
import com.googlecode.openbox.common.UtilsAPI;
import com.googlecode.openbox.common.context.CommonContext;
import com.googlecode.openbox.config.ContentLoader;
import com.googlecode.openbox.testu.TestUException;
import com.googlecode.openbox.testu.tester.ActualResults;
import com.googlecode.openbox.testu.tester.Bugs;
import com.googlecode.openbox.testu.tester.CaseDescriptions;
import com.googlecode.openbox.testu.tester.ExpectedResults;
import com.googlecode.openbox.testu.tester.OverallTestResult;
import com.googlecode.openbox.testu.tester.Preconditions;
import com.googlecode.openbox.testu.tester.QA;
import com.googlecode.openbox.testu.tester.Steps;
import com.googlecode.openbox.testu.tester.TestCase;
import com.googlecode.openbox.testu.tester.TestCasePool;
import com.googlecode.openbox.testu.tester.TestCaseResults;
import com.googlecode.openbox.testu.tester.TestCaseResults.Result;
import com.googlecode.openbox.testu.tester.TestCasesExporter;

public class HtmlTextExporter implements TestCasesExporter {
	public static final String CONTEXT_ID="OverallTestResult";
	private String exportLocalFile;

	private HtmlTextExporter(String exportLocalFile) {
		this.exportLocalFile = exportLocalFile;
	}

	public static HtmlTextExporter newInstance(String location) {
		return new HtmlTextExporter(location);
	}

	@Override
	public void export(TestCasePool testCasePool,CommonContext context) {
		if(StringUtils.isEmpty(exportLocalFile)){
			exportLocalFile = UtilsAPI.getCurrentWorkingPath()+IOUtils.PATH_SPLIT+"testreport";
		}
		
		URL url = getClass().getClassLoader().getResource("reporter/html");
		URLConnection urlConnection = null;
		try {
			urlConnection = url.openConnection();
		} catch (IOException e) {
			throw TestUException.create("get reporter/html url from target or jar error !", e);
		}
		
		if(urlConnection instanceof JarURLConnection){
			try {
				IOUtils.copyFolderFromJar((JarURLConnection)urlConnection, exportLocalFile);
			} catch (IOException e) {
				throw TestUException.create("copy testu html report framework resource from jar error !", e);
			}
		}else{
			IOUtils.copyFolder(url.getFile(), exportLocalFile);
		}
		

		String appJsFile = exportLocalFile + IOUtils.PATH_SPLIT+ "app.js";
		IOUtils.deleteFile(appJsFile);
		
		String header = ContentLoader.getContent("reporter/html/header.fm");
		IOUtils.appendContentToFile(appJsFile, header);
		
		Object object = context.getAttribute(CONTEXT_ID);
		if(null ==object){
			throw TestUException.create("HtmlTextExporter need a context object as ID-- OverallTestResult ,Please check it for your caller ! ");
		}
		OverallTestResult overallTestResult = (OverallTestResult)object;
		IOUtils.appendContentToFile(appJsFile, overallTestResult.getHtmlReport());
		
		String start = ContentLoader.getContent("reporter/html/start.fm");
		IOUtils.appendContentToFile(appJsFile, start);
		
		TestCase rootTestCase = testCasePool.exportCaseTreeRoot();
		TestCaseVO testCaseVO = convertToTestCaseVO(rootTestCase);
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String content = gson.toJson(testCaseVO);
		IOUtils.appendContentToFile(appJsFile, content.replaceAll("'", "\'"));
		
		String end = ContentLoader.getContent("reporter/html/end.fm");
		IOUtils.appendContentToFile(appJsFile, end);
	}

	private TestCaseVO convertToTestCaseVO(TestCase testCase) {
		
		if(null == testCase){
			return null;
		}
		
		TestCaseVO testCaseVO= new TestCaseVO();
		testCaseVO.setName(testCase.getName());
		testCaseVO.setDescriptions(getCaseDescriptions(testCase));
		testCaseVO.setResult(getResult(testCase));
		testCaseVO.setMsg(getMessage(testCase));
		testCaseVO.setDuration(getDuration(testCase));
		testCaseVO.setLogs(handleSpecialCharacters(testCase.getLogs()));
		testCaseVO.setOwner(getOwner(testCase));
		testCaseVO.setBugs(getBugs(testCase));
		List<TestCase> children = testCase.getChildren();
		if(null!= children && children.size()>0){
			testCaseVO.setExpanded(true);
			testCaseVO.setLeaf(false);
			for(TestCase child : children){
				TestCaseVO childTestCaseVO = convertToTestCaseVO(child);
				testCaseVO.addChild(childTestCaseVO);
			}
		}else{
			testCaseVO.setExpanded(true);
			testCaseVO.setLeaf(true);
		}
		return testCaseVO;
	}
	
	private String getOwner(TestCase testCase){
		QA owner = testCase.getOwner();
		if(null!= owner){
			return owner.name();
		}
		return "";
	}
	private String getResult(TestCase testCase){
		TestCaseResults testCaseResult = testCase.getActualResults();
		if(null != testCaseResult){
			Result result = testCaseResult.getResult();
			if(null == result){
				result = Result.SKIP;
			}
			
			switch(result){
				case SKIP : return "<font color='yellow'>"+result.name()+"</font>";
				case SUCCESS: return "<font color='green'>"+result.name()+"</font>";
				case FAILURE: return "<font color='red'>"+result.name()+"</font>";
				case SUCCESS_PERCENTAGE_FAILURE: return "<font color='green'>"+result.name()+"</font>";
				case STARTED: return "<font color='yellow'>"+result.name()+"</font>";

			}
		}
		return  "";
	}
	
	private String getMessage(TestCase testCase){
		TestCaseResults testCaseResult = testCase.getActualResults();
		StringBuilder msgBuilder = new StringBuilder();
		
		if(null != testCaseResult){
			ActualResults actualResults =  testCaseResult.getActualResults();
			if(null != actualResults){
				msgBuilder.append("Actual Result:\n");
				for(String actualResult:actualResults.value()){
					msgBuilder.append(actualResult).append("\n");
				}
			}
			String msg = testCaseResult.getMsg();
			if(null != msg){
				msgBuilder.append(msg);
			}
		}
		return handleSpecialCharacters(msgBuilder.toString());
	}
	
	private String getDuration(TestCase testCase){
		TestCaseResults testCaseResult = testCase.getActualResults();
		if(null != testCaseResult){
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
	
	private String getCaseDescriptions(TestCase testCase){
		StringBuilder descriptionsBuilder= new StringBuilder();
		CaseDescriptions caseDescriptions = testCase.getCaseDescriptions();
		if(null != caseDescriptions){
			for(String description : caseDescriptions.value()){
				descriptionsBuilder.append(description).append("<br>");
			}
			descriptionsBuilder.append("<br>");
		}
		Preconditions preconditions = testCase.getPreconditions();
		if(null != preconditions){
			descriptionsBuilder.append("<br>Preconditions : <br>");
			for(String precondition : preconditions.value()){
				descriptionsBuilder.append(precondition).append("<br>");
			}
			descriptionsBuilder.append("<br>");
		}
		
		Steps steps = testCase.getSteps();
		if(null != steps){
			descriptionsBuilder.append("<br>Steps : <br>");
			for(String step : steps.value()){
				descriptionsBuilder.append(step).append("<br>");
			}
			descriptionsBuilder.append("<br>");
		}
		
		ExpectedResults expectedResults = testCase.getExpectedResults();
		if(null != expectedResults){
			descriptionsBuilder.append("<br>Expected Results : <br>");
			for(String expectedResult : expectedResults.value()){
				descriptionsBuilder.append(expectedResult).append("<br>");
			}
			descriptionsBuilder.append("<br>");
		}
		return descriptionsBuilder.toString();
	}

	private static String handleSpecialCharacters(String input){
		if(null == input){
			return "";
		}
		return StringUtils.replaceEach(input,new String[]{"'","\"","\n","\r"},new String[]{"","","\\n","\\r"});
	}
	public static void main(String ...args){
		String input = "we can't \nfind it !";
		System.out.println(handleSpecialCharacters(input));
		
	}
}
