package com.googlecode.openbox.testu.tester;

import java.util.LinkedList;
import java.util.List;


public class TestCase {
	private String name;
	private QA owner;
	private CaseDescriptions caseDescriptions;
	private TestCase parent;
	private List<TestCase> children;
	private Preconditions preconditions;
	private Steps steps;
	private ExpectedResults expectedResults;
	private TestCaseResults actualResults;
	private String logs;
		
	private TestCase(String name){
		this.name = name;
		this.children = new LinkedList<TestCase>();
	}
	
	public static TestCase create(String caseName){
		return new TestCase(caseName);
	}
		
	public void addChildTestCase(TestCase child) {
		child.setParent(this);
		children.add(child);
	}
	
	public void removeChildTestCase(TestCase child){
		child.setParent(null);
		children.remove(child);
	}

	public String getCaseTreePath() {
		String parentPath = "";
		if (null != parent) {
			parentPath = parent.getCaseTreePath();
		}
		return parentPath + "->" + getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public TestCase getParent() {
		return parent;
	}

	public void setParent(TestCase parent) {
		this.parent = parent;
	}

	public List<TestCase> getChildren() {
		return children;
	}

	public void setChildren(List<TestCase> children) {
		this.children = children;
	}

	public Preconditions getPreconditions() {
		return preconditions;
	}

	public void setPreconditions(Preconditions preconditions) {
		this.preconditions = preconditions;
	}

	public Steps getSteps() {
		return steps;
	}

	public void setSteps(Steps steps) {
		this.steps = steps;
	}

	public ExpectedResults getExpectedResults() {
		return expectedResults;
	}

	public void setExpectedResults(ExpectedResults expectedResults) {
		this.expectedResults = expectedResults;
	}

	public TestCaseResults getActualResults() {
		return actualResults;
	}

	public void setActualResults(TestCaseResults actualResults) {
		this.actualResults = actualResults;
	}

	public String getLogs() {
		return logs;
	}

	public void setLogs(String logs) {
		this.logs = logs;
	}

	public CaseDescriptions getCaseDescriptions() {
		return caseDescriptions;
	}

	public QA getOwner() {
		if(null != owner){
			return owner;
		}
		TestCase parent = getParent();
		if(null != parent){
			return parent.getOwner();
		}
		return null;
	}

	public void setOwner(QA owner) {
		this.owner = owner;
	}

	public void setCaseDescriptions(CaseDescriptions caseDescriptions) {
		this.caseDescriptions = caseDescriptions;
	}
	
	public int getCaseLevel(){
		TestCase parent = getParent();
		if(null != parent){
			return parent.getCaseLevel()+1;
		}
		return 0;
	}

}
