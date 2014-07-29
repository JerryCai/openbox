package com.googlecode.openbox.testu.tester.exporters;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class TestCaseVO {

	@Expose
	private String name;
	@Expose
	private String descriptions;
	@Expose
	private List<TestCaseVO> children;
	@Expose
	private String result;
	@Expose
	private String msg;
	@Expose
	private String owner;
	@Expose
	private String bugs;
	@Expose
	private String duration;
	@Expose
	private String logs;
	@Expose
	private boolean expanded;
	@Expose
	private boolean leaf;
	
	public void addChild(TestCaseVO child){
		if(null == this.children){
			this.children = new LinkedList<TestCaseVO>();
		}
		this.children.add(child);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}
	public List<TestCaseVO> getChildren() {
		return children;
	}
	public void setChildren(List<TestCaseVO> children) {
		this.children = children;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getBugs() {
		return bugs;
	}
	public void setBugs(String bugs) {
		this.bugs = bugs;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getLogs() {
		return logs;
	}
	public void setLogs(String logs) {
		this.logs = logs;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
	
}
