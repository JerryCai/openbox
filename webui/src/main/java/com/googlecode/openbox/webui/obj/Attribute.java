package com.googlecode.openbox.webui.obj;

public class Attribute {
	private String name;
	private String value;

	private Attribute(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public static Attribute create(String name, String value) {
		return new Attribute(name, value);
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
	
	public String toString(){
		return "@"+name+"='"+value+"'";
	}

}
