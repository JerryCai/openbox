package com.googlecode.openbox.phone.listeners;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.peers.sip.transport.SipRequest;
import net.sourceforge.peers.sip.transport.SipResponse;

import com.googlecode.openbox.phone.Phone;
import com.googlecode.openbox.phone.PhoneStatus;
import com.googlecode.openbox.phone.listeners.IncomingCallRecord.ActionType;

public class AutoRejectIncommingPhoneListener extends DefaultSipListener {

	private Map<String, Integer> rejecHistory;

	private AutoRejectIncommingPhoneListener(Phone phone) {
		super(phone);
		rejecHistory = new HashMap<String, Integer>();
	}

	public static AutoRejectIncommingPhoneListener createAndRegisterToPhoneListener(
			Phone phone) {
		return new AutoRejectIncommingPhoneListener(phone);
	}

	@Override
	public void incomingCall(SipRequest sipRequest, SipResponse provResponse) {
		super.incomingCall(sipRequest, provResponse);
		getPhone().reject();
		this.getCurrentIncomingCallRecord().setActionType(ActionType.REJECTED);
		String phoneNumber = getIncomingPhoneNumber();
		Integer value = rejecHistory.get(phoneNumber);
		if (null == value) {
			value = 0;
		}
		rejecHistory.put(phoneNumber, ++value);
		setPhoneStatus(PhoneStatus.rejected);

	}

	public int getRejectTotalTimes(String phoneNumber) {
		Integer times = rejecHistory.get(phoneNumber);
		if (null == times) {
			return 0;
		}
		return times;
	}

	@Override
	public PhoneType getPhoneListenerType() {
		return PhoneType.REJECT;
	}
	
	@Override
	public PhoneType getType() {
		return PhoneType.REJECT;
	}
}
