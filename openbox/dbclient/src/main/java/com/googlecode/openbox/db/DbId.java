package com.googlecode.openbox.db;

public class DbId {
	private String dbId;

	public static DbId newInstance(String dbId) {
		return new DbId(dbId);
	}

	private DbId(String dbId) {
		this.dbId = dbId;
	}

	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

}
