package com.googlecode.openbox.testu.tester.exporters;

public class BugListVO {
	public enum BugStatus{
		Fixed,Open,Skiped;
	}
	
	private String bugLink;
	private BugStatus bugStatus;
	private String owner;
	
	public String getBugLink() {
		return bugLink;
	}
	public void setBugLink(String bugLink) {
		this.bugLink = bugLink;
	}
	public BugStatus getBugStatus() {
		return bugStatus;
	}
	public void setBugStatus(BugStatus bugStatus) {
		this.bugStatus = bugStatus;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}

}
