package com.googlecode.openbox.http.extention;

import com.googlecode.openbox.http.ExecutorMonitor;
import com.googlecode.openbox.http.RequestProxy;

public interface AppClientExtendable {

	void registerProxy(RequestProxy proxy);

	void unregisterProxy(RequestProxy proxy);

	void cleanAllProxy();

	void registerMonitor(ExecutorMonitor monitor);

	void unregisterMonitor(ExecutorMonitor monitor);

	void cleanAllMonitor();

}
