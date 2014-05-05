package com.googlecode.openbox.foo.request;


import com.googlecode.openbox.foo.ClientVersion;
import com.googlecode.openbox.http.Request;

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
