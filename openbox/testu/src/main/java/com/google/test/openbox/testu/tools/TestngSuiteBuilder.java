package com.google.test.openbox.testu.tools;

import java.io.File;
import java.net.URL;

import com.google.test.openbox.common.IOUtils;
import com.google.test.openbox.testu.TestngSuite;

public class TestngSuiteBuilder {
	public static final String CLASS_POSTFIX = ".class";

	private StringBuilder builder;
	private String suiteName = "Default Suite";
	private String testModuleName = "Default Test";
	private String testPrefixName = "TestCase";
	private Class<?> suiteClass;
	private String testngXmlLocation = "src/test/resources/suites/testng_default.xml";

	private TestngSuiteBuilder(String suiteName, String testModuleName,
			String testPrefixName, Class<?> suiteClass, String testngXmlLocation) {
		this.builder = new StringBuilder();
		this.suiteName = suiteName;
		this.testModuleName = testModuleName;
		this.testPrefixName = testPrefixName;
		this.suiteClass = suiteClass;
		this.testngXmlLocation = testngXmlLocation;
	}

	public static TestngSuiteBuilder create(String suiteName,
			String testModuleName, String testPrefixName, Class<?> suiteClass,
			String testngXmlLocation) {
		return new TestngSuiteBuilder(suiteName, testModuleName,
				testPrefixName, suiteClass, testngXmlLocation);
	}

	public static TestngSuiteBuilder create(TestngSuite suite) {
		return create(suite.getSuiteName(), suite.getTestModuleName(),
				suite.getTestPrefixName(), suite.getSuiteClass(),
				suite.getTestngXmlLocation());

	}

	private void appendLine(String s) {
		builder.append(s).append("\n");
	}

	private void build() throws Exception {
		String startPackage = suiteClass.getPackage().getName();
		startPackage = startPackage.replaceAll("\\.", "\\\\");
		URL url = suiteClass.getResource(".");
		File file = new File(url.toURI());
		builder.delete(0, builder.length());
		appendLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		appendLine("<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\">");
		appendLine("<suite name=\"" + suiteName + "\" parallel=\"none\">");
		appendLine("  <test name=\"" + testModuleName
				+ "\" preserve-order=\"true\">");
		appendLine("    <classes>");
		searchTestngClass(file, startPackage);
		appendLine("    </classes>");
		appendLine("  </test> <!-- Test -->");
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
				if (strName.startsWith(testPrefixName)) {
					String path = file.getPath();
					int pp = path.indexOf(startPackage);
					int endPP = path.indexOf(CLASS_POSTFIX);
					String className = path.substring(pp, endPP);
					className = className.replaceAll("\\\\", ".");
					appendLine("      <class name=\"" + className + "\"/>");
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
