package com.googlecode.openbox.webui.obj;

import java.util.LinkedList;
import java.util.List;

public class Tag {
	private String name;
	private List<Attribute> attributes;

	private Tag(String name) {
		this.name = name;
		this.attributes = new LinkedList<Attribute>();
	}

	public static Tag create(String name) {
		return new Tag(name);
	}

	public void addAttribute(Attribute attribute) {
		attributes.add(attribute);
	}

	public void addAttribute(String attrName, String attrValue) {
		addAttribute(Attribute.create(attrName, attrValue));
	}
	
	public void addContaionsAttribute(String attrName, String attrValue) {
		addAttribute(ContainsAttribute.create(attrName, attrValue));
	}

	public String getName() {
		return name;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public String toXPath() {
		StringBuilder sb = new StringBuilder();
		sb.append("//").append(name).append("[");
		boolean isFirst = true;
		for (Attribute attribute : attributes) {
			if (!isFirst) {
				sb.append(" and ");
			} else {
				isFirst = false;
			}
			sb.append(attribute);

		}
		sb.append("]");
		return sb.toString();
	}

	public String toString() {
		return toXPath();
	}

}
