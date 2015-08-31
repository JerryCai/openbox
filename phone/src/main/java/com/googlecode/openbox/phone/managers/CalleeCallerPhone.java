package com.googlecode.openbox.phone.managers;

import com.googlecode.openbox.phone.Phone;
import com.googlecode.openbox.phone.listeners.SwitchablePhoneController;

public interface CalleeCallerPhone
		extends BatchPhones {
	
	Phone getCallerPhone();
	
	Phone getCalleePhone();

	SwitchablePhoneController getCalleeSwitchablePhoneController();

	SwitchablePhoneController getCallerSwitchablePhoneController();

}