package com.googlecode.openbox.testu.tools;

import java.io.File;
import java.net.URL;

import com.googlecode.openbox.common.IOUtils;
import com.googlecode.openbox.testu.TestngSuite;

public class TestngSuiteBuilder {
	public static final String CLASS_POSTFIX = ".class";

	private StringBuilder builder;
	private String suiteName;
	private String testModuleName;
	private String testPrefixName;
	private String testPostfixName;
	private Class<?> suiteClass;
	private String testngXmlLocation;

	private TestngSuiteBuilder(String suiteName, String testModuleName,
			String testPrefixName, String testPostfixName, Class<?> suiteClass,
			String testngXmlLocation) {
		this.builder = new StringBuilder();
		this.suiteName = suiteName;
		this.testModuleName = testModuleName;
		this.testPrefixName = testPrefixName;
		this.testPostfixName = testPostfixName;
		this.suiteClass = suiteClass;
		this.testngXmlLocation = testngXmlLocation;
	}

	public static TestngSuiteBuilder create(String suiteName,
			String testModuleName, String testPrefixName,
			String testPostfixName, Class<?> suiteClass,
			String testngXmlLocation) {
		return new TestngSuiteBuilder(suiteName, testModuleName,
				testPrefixName, testPostfixName, suiteClass, testngXmlLocation);
	}

	public static TestngSuiteBuilder create(TestngSuite suite) {
		return create(suite.getSuiteName(), suite.getTestModuleName(),
				suite.getTestPrefixName(), suite.getTestPostfixName(),
				suite.getSuiteClass(), suite.getTestngXmlLocation());

	}

	private void appendLine(String s) {
		builder.append(s).append("\n");
	}

	private void build() throws Exception {
		String startPackage = suiteClass.getPackage().getName();
		System.out.println(startPackage);
		startPackage = startPackage.replaceAll("\\.", "\\" + File.separator);
		System.out.println(startPackage);
		URL url = suiteClass.getResource(".");
		File file = new File(url.toURI());
		builder.delete(0, builder.length());
		appendLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		appendLine("<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\">");
		appendLine("<suite name=\"" + suiteName + "\" parallel=\"none\">");
		searchTestngClass(file, startPackage);
		appendLine("</suite> <!-- Suite -->");
	}

	private void searchTestngClass(File file, String startPackage)
			throws Exception {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File tmpFile : files) {
				searchTestngClass(tmpFile, startPackage);
			}
		} else {
			String name = file.getName();
			if (name.indexOf("$") < 0) {
				int op = name.lastIndexOf(".");
				String strName = name.substring(0, op);
				if (strName.startsWith(testPrefixName)
						&& strName.endsWith(testPostfixName)) {
					String path = file.getPath();
					int pp = path.indexOf(startPackage);
					int endPP = path.indexOf(CLASS_POSTFIX);
					String className = path.substring(pp, endPP);
					className = className.replaceAll("\\\\", ".");
					appendLine("  <test name=\"" + strName
							+ "\" preserve-order=\"true\">");
					appendLine("    <classes>");
					appendLine("      <class name=\"" + className + "\"/>");
					appendLine("    </classes>");
					appendLine("  </test> <!-- Test -->");

				}
			}
		}
	}

	public String createTestngSuite() throws Exception {
		build();
		System.out.println(builder.toString());
		String path = new File("").getAbsolutePath() + getTestngXmlLocation();
		path = path.replaceAll("\\\\", "/");
		IOUtils.writeInfoToFile(path, builder.toString());
		return builder.toString();
	}

	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public String getTestModuleName() {
		return testModuleName;
	}

	public void setTestModuleName(String testModuleName) {
		this.testModuleName = testModuleName;
	}

	public String getTestPrefixName() {
		return testPrefixName;
	}

	public void setTestPrefixName(String testPrefixName) {
		this.testPrefixName = testPrefixName;
	}

	public Class<?> getSuiteClass() {
		return suiteClass;
	}

	public void setSuiteClass(Class<?> suiteClass) {
		this.suiteClass = suiteClass;
	}

	public String getTestngXmlLocation() {
		return testngXmlLocation;
	}

	public void setTestngXmlLocation(String testngXmlLocation) {
		this.testngXmlLocation = testngXmlLocation;
	}

}
