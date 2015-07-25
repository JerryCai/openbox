package com.googlecode.openbox.testu.tester;

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
	private String keySeed;
	private boolean isSuite;
	private String displayName;
	private int level;
	private TestCase parent;
	@Expose
	private List<TestCase> children;
	@Expose
	private Tester owner;
	@Expose(serialize = false, deserialize = false)
	private Owner qa;
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
	
	private int totalTested;
	private int totalPassed;
	private int totalFailed;
	private int totalSkiped;
	private long totalDuration;
	private boolean expand;
	private Boolean testCaseAutomated;

	private TestCase(String name, boolean isFolder, String keySeed) {
		this.name = name;
		this.keySeed = keySeed;
		this.isSuite = isFolder;
		this.level = 0;
		this.parent = null;
		this.children = new LinkedList<TestCase>();
		this.expand = false;
		this.testCaseAutomated = Boolean.TRUE;
	}

	public static TestCase create(String name, boolean isFolder, String keySeed) {
		return new TestCase(name, isFolder, keySeed);
	}

	public static TestCase createTestCaseFromPool(String name,
			boolean isFolder, String keySeed) {
		TestCase tree = create(name, isFolder, keySeed);
		String key = tree.getKey();
		if (!NODE_POOL.containsKey(key)) {
			NODE_POOL.put(key, tree);
		}
		return NODE_POOL.get(key);
	}
	void analysisOverallTestStatus(){
		if(isSuite()){
			_analysisChildren();
		}else{
			_analysisCurrent();
			if(getChildren().size() > 0 ){
				_analysisChildren();
			}
		}
	}
	
	private void _analysisChildren(){
		for(TestCase child : getChildren()){
			child.analysisOverallTestStatus();
			this.totalDuration += child.getTotalDuration();
			this.totalTested += child.getTotalTested();
			this.totalPassed += child.getTotalPassed();
			this.totalFailed += child.getTotalFailed();
			this.totalSkiped += child.getTotalSkiped();
		}
	}
	
	private void _analysisCurrent(){
		this.totalTested = 0;		
		this.totalPassed = 0;
		this.totalFailed = 0;
		this.totalSkiped=0;
		if(null == getActualResults()){
			return;
		}
		this.totalTested = 1;
		switch(getActualResults().getResult()){
		case SUCCESS : 
				this.totalPassed = 1;
				break;
		case SKIP:
			this.totalSkiped = 1;
			break;
		default:
			this.totalFailed =1;
		}
	}

	public void addChild(TestCase child) {
		if (null == child) {
			return;
		}
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
		return keySeed + "!" + name + "@" + isSuite;
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

	public Owner getOwner() {
		if (null != qa) {
			return qa;
		}
		TestCase parent = getParent();
		if (null != parent) {
			return parent.getOwner();
		}
		return null;
	}

	public void setOwner(Owner owner) {
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
			descriptionsBuilder.append("Steps : \n");
			for (String step : steps.value()) {
				descriptionsBuilder.append(step).append("\n");
			}
			descriptionsBuilder.append("\n");
		}

		ExpectedResults expectedResults = getExpectedResults();
		if (null != expectedResults) {
			descriptionsBuilder.append("Expected Results : \n");
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

	public boolean isSuite() {
		return isSuite;
	}
	
	public int getTotalTested() {
		return totalTested;
	}

	public int getTotalPassed() {
		return totalPassed;
	}

	public int getTotalFailed() {
		return totalFailed;
	}

	public int getTotalSkiped() {
		return totalSkiped;
	}

	public long getTotalDuration() {
		return totalDuration;
	}

	void setTotalDuration(long totalDuration) {
		this.totalDuration = totalDuration;
	}

	public boolean isExpand() {
		return expand;
	}

	public void setExpand(boolean expand) {
		this.expand = expand;
	}

	public Boolean isTestCaseAutomated() {
		return testCaseAutomated;
	}

	public void setTestCaseAutomated(Boolean testCaseAutomated) {
		this.testCaseAutomated = testCaseAutomated;
	}

	public String toString() {
		return "\n" + getCaseTreePath() + "\n[\n" + this.getName() + "\n"
				+ this.getTextDescriptions() + "\n]\n";
	}

}
