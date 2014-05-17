package com.googlecode.openbox.server.loaders;

import com.googlecode.openbox.server.ServerGroup;

public interface ServerGroupLoader {
	
	String getUsername();

	String getPassword();

	int getPort();

	String[] getIps();

	ServerGroup toServerGroup(boolean isConcurrent);

}
