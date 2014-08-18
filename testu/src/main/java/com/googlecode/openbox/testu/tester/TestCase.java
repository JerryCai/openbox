package com.googlecode.openbox.testu.tester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.annotations.Expose;
import com.googlecode.openbox.testu.tester.exporters.BugListVO;
import com.googlecode.openbox.testu.tester.exporters.BugListVO.BugStatus;

public class TestCase {
	private static final Map<String, TestCase> NODE_POOL = new HashMap<String, TestCase>();
	@Expose
	private String name;
	private boolean isFolder;
	private String displayName;
	private int level;
	private TestCase parent;
	@Expose
	private List<TestCase> children;
	@Expose
	private Tester owner;
	@Expose(serialize = false, deserialize = false)
	private QA qa;
	@Expose
	private String[] descriptions;
	@Expose(serialize = false, deserialize = false)
	private CaseDescriptions caseDescriptions;
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

	private TestCase(String name, boolean isFolder) {
		this.name = name;
		this.isFolder = isFolder;
		this.level = 0;
		this.parent = null;
		this.children = new ArrayList<TestCase>();
	}

	public static TestCase create(String name, boolean isFolder) {
		return new TestCase(name, isFolder);
	}

	public static TestCase createTestCaseFromPool(String name, boolean isFolder) {
		TestCase tree = create(name, isFolder);
		String key = tree.getKey();
		if (!NODE_POOL.containsKey(key)) {
			NODE_POOL.put(key, tree);
		}
		return NODE_POOL.get(key);
	}

	public void addChild(TestCase child) {
		if (children.contains(child)) {
			return;
		}
		if (child.getParent() != null) {
			child.getParent().getChildren().remove(child);
		}
		child.setParent(this);
		child.grow();
		this.children.add(child);
	}

	public String getPath() {
		if (null != parent) {
			return parent.getPath() + "/" + getName();
		}
		return "/" + getName();
	}

	public String getKey() {
		return name + "_" + isFolder;
	}

	private void grow() {
		int increase = 1;
		if (null != parent) {
			increase = increase + parent.getLevel();
		}
		this.level = this.level + increase;
		for (TestCase child : children) {
			child.grow();
		}
	}

	public void collectKeys(Set<String> keys) {
		if (null == keys) {
			throw new RuntimeException(
					"collect keys error as your keys is null , Please new a instance for it! ");
		}
		keys.add(getKey());
		for (TestCase child : children) {
			child.collectKeys(keys);
		}
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public TestCase getParent() {
		return parent;
	}

	private void setParent(TestCase parent) {
		this.parent = parent;
	}

	public List<TestCase> getChildren() {
		return children;
	}

	public String getCaseTreePath() {
		return this.getPath();
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
		if (null != steps) {
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
		if (null != owner) {
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
		if (null != caseDescriptions) {
			descriptions = caseDescriptions.value();
		}
	}

	public String getTextDescriptions() {
		StringBuilder descriptionsBuilder = new StringBuilder();
		CaseDescriptions caseDescriptions = getCaseDescriptions();
		if (null != caseDescriptions) {
			for (String description : caseDescriptions.value()) {
				descriptionsBuilder.append(description).append("\n");
			}
			descriptionsBuilder.append("\n");
		}
		Preconditions preconditions = getPreconditions();
		if (null != preconditions) {
			descriptionsBuilder.append("\nPreconditions : \n");
			for (String precondition : preconditions.value()) {
				descriptionsBuilder.append(precondition).append("\n");
			}
			descriptionsBuilder.append("\n");
		}

		Steps steps = getSteps();
		if (null != steps) {
			descriptionsBuilder.append("\nSteps : \n");
			for (String step : steps.value()) {
				descriptionsBuilder.append(step).append("\n");
			}
			descriptionsBuilder.append("\n");
		}

		ExpectedResults expectedResults = getExpectedResults();
		if (null != expectedResults) {
			descriptionsBuilder.append("<br>Expected Results : \n");
			for (String expectedResult : expectedResults.value()) {
				descriptionsBuilder.append(expectedResult).append("\n");
			}
			descriptionsBuilder.append("\n");
		}
		return descriptionsBuilder.toString();
	}

	public List<BugListVO> getBugList() {
		List<BugListVO> bugList = new LinkedList<BugListVO>();
		Bugs bugs = getBugs();
		if (null == bugs) {
			return bugList;
		}
		for (String bugLink : bugs.value()) {
			BugListVO bugListVO = new BugListVO();
			bugListVO.setBugLink(bugLink);
			if (null != getOwner()) {
				bugListVO.setOwner(getOwner().name());
			} else {
				bugListVO.setOwner("");
			}
			if (null != getActualResults()) {
				switch (getActualResults().getResult()) {
				case SUCCESS:
					bugListVO.setBugStatus(BugStatus.Fixed);
					break;
				case FAILURE:
					bugListVO.setBugStatus(BugStatus.Open);
					break;
				default:
					bugListVO.setBugStatus(BugStatus.Skiped);
				}
			}
			bugList.add(bugListVO);

		}
		return bugList;
	}

	public int getCaseLevel() {
		TestCase parent = getParent();
		if (null != parent) {
			return parent.getCaseLevel() + 1;
		}
		return 0;
	}

	public String getDisplayName() {
		return displayName == null ? getName() : displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String toString() {
		return "\n" + getCaseTreePath() + "\n[\n" + this.getName() + "\n"
				+ this.getTextDescriptions() + "\n]\n";
	}

}
