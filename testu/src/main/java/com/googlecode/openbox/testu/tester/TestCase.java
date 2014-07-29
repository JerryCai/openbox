package com.googlecode.openbox.testu.tester;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class TestCase {
	@Expose
	private String name;
	@Expose
	private Tester owner;
	@Expose(serialize = false, deserialize = false)
	private QA qa;
	@Expose
	private String[] descriptions;
	@Expose(serialize = false, deserialize = false)
	private CaseDescriptions caseDescriptions;
	@Expose(serialize = false, deserialize = false)
	private TestCase parent;
	@Expose
	private List<TestCase> children;
	@Expose
	private String[] preconditions;
	@Expose(serialize = false, deserialize = false)
	private Preconditions casePreconditions;
	@Expose
	private String[] steps;
	private Steps caseSteps;
	@Expose(serialize = false, deserialize = false)
	private String[] expectedResults;
	@Expose(serialize = false, deserialize = false)
	private ExpectedResults caseExpectedResults;
	@Expose(serialize = false, deserialize = false)
	private Bugs bugLinks;
	@Expose
	private TestCaseResults caseActualResults;
	@Expose
	private String logs;

	private TestCase(String name) {
		this.name = name;
		this.children = new LinkedList<TestCase>();
	}

	public static TestCase create(String caseName) {
		return new TestCase(caseName);
	}

	public void addChildTestCase(TestCase child) {
		child.setParent(this);
		children.add(child);
	}

	public void removeChildTestCase(TestCase child) {
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
		return casePreconditions;
	}

	public void setPreconditions(Preconditions preconditions) {
		this.casePreconditions = preconditions;
		if (null != preconditions) {
			this.preconditions = preconditions.value();
		}
	}

	public Steps getSteps() {
		return caseSteps;
	}

	public void setSteps(Steps steps) {
		this.caseSteps = steps;
		if(null != steps){
			this.steps = steps.value();
		}
	}

	public ExpectedResults getExpectedResults() {
		return caseExpectedResults;
	}

	public void setExpectedResults(ExpectedResults expectedResults) {
		this.caseExpectedResults = expectedResults;
		if (null != expectedResults) {
			this.expectedResults = expectedResults.value();
		}
	}

	public TestCaseResults getActualResults() {
		return caseActualResults;
	}

	public void setActualResults(TestCaseResults actualResults) {
		this.caseActualResults = actualResults;
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
		if (null != qa) {
			return qa;
		}
		TestCase parent = getParent();
		if (null != parent) {
			return parent.getOwner();
		}
		return null;
	}

	public void setOwner(QA owner) {
		this.qa = owner;
		if(null!= owner){
			this.owner = new Tester();
			this.owner.setName(owner.name());
			this.owner.setEmail(owner.email());
			this.owner.setId(owner.id());
		}
	}

	public Bugs getBugs() {
		return bugLinks;
	}

	public void setBugs(Bugs bugs) {
		this.bugLinks = bugs;
	}

	public void setCaseDescriptions(CaseDescriptions caseDescriptions) {
		this.caseDescriptions = caseDescriptions;
		if(null != caseDescriptions){
			descriptions = caseDescriptions.value();
		}
	}

	public int getCaseLevel() {
		TestCase parent = getParent();
		if (null != parent) {
			return parent.getCaseLevel() + 1;
		}
		return 0;
	}

}
