package com.googlecode.openbox.phone.managers;

import com.googlecode.openbox.phone.listeners.DefaultSipListener;

public interface PairPhone<CallerListener extends DefaultSipListener, CalleeListener extends DefaultSipListener>
		extends BatchPhones {

	CalleeListener getCalleeListener();

	CallerListener getCallerListener();

}