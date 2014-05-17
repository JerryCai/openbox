package com.googlecode.openbox.foo;

public enum ClientVersion {
	V1("v1");

	private String version;

	ClientVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public String toString() {
		return getVersion();
	}

}