package com.google.test.openbox.server.loaders;

import com.google.test.openbox.server.ServerGroup;

public interface ServerGroupLoader {
	
	String getUsername();

	String getPassword();

	int getPort();

	String[] getIps();

	ServerGroup toServerGroup(boolean isConcurrent);

}
