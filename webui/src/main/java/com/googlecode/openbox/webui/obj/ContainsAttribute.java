package com.googlecode.openbox.webui.obj;

public class ContainsAttribute extends Attribute {

	public ContainsAttribute(String name, String value) {
		super(name, value);
	}
	
	public static Attribute create(String name, String value) {
		return new ContainsAttribute(name, value);
	}
	
	public String toString(){
		return "contains(@"+getName()+",'"+getValue()+"')";
	}

}
