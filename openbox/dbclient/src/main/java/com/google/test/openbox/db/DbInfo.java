package com.google.test.openbox.db;

public class DbInfo {
	private DbId dbId;
	private String url;
	private String username;
	private String password;
	private String driverClass;

	public DbInfo() {
		this.dbId = DbId.newInstance("default db id with db url is empty");
	}

	public DbInfo(String url, String username, String password) {
		this.url = url;
		this.dbId = DbId.newInstance(this.url);
		this.username = username;
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public DbId getDbId() {
		return dbId;
	}

	public void setDbId(DbId dbId) {
		this.dbId = dbId;
	}
}
