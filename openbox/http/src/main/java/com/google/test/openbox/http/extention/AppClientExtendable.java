package com.google.test.openbox.http.extention;

import com.google.test.openbox.http.ExecutorMonitor;
import com.google.test.openbox.http.RequestProxy;

public interface AppClientExtendable {

	void registerProxy(RequestProxy proxy);

	void unregisterProxy(RequestProxy proxy);

	void cleanAllProxy();

	void registerMonitor(ExecutorMonitor monitor);

	void unregisterMonitor(ExecutorMonitor monitor);

	void cleanAllMonitor();

}
