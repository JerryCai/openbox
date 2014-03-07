package com.google.test.openbox.foo.request;


import com.google.test.openbox.foo.ClientVersion;
import com.google.test.openbox.http.Request;

public abstract class AbstractFooRequest extends Request {

	private ClientVersion version;

	public AbstractFooRequest(String url, ClientVersion version) {
		super(url);
		this.version = version;
	}

	protected abstract String getRestPath();

	private String getVersionLevelPath() {
		return "/demos/" + version.getVersion() + "/";
	}

	public void setApiPath() {
		getURIBuilder().setPath(getVersionLevelPath() + getRestPath());
	}

}
