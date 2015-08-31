package com.googlecode.openbox.phone.listeners;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.peers.sip.transport.SipRequest;
import net.sourceforge.peers.sip.transport.SipResponse;

import com.googlecode.openbox.phone.Phone;
import com.googlecode.openbox.phone.PhoneStatus;
import com.googlecode.openbox.phone.listeners.IncomingCallRecord.ActionType;

public class AutoPickupPhoneListener extends DefaultSipListener {

	private Map<String, Integer> pickupHistory;

	public AutoPickupPhoneListener(Phone phone) {
		super(phone);
		pickupHistory = new HashMap<String, Integer>();
	}

	public static AutoPickupPhoneListener createAndRegisterToPhoneListener(
			Phone phone) {
		return new AutoPickupPhoneListener(phone);
	}

	@Override
	public void incomingCall(SipRequest sipRequest, SipResponse provResponse) {
		super.incomingCall(sipRequest, provResponse);
		this.getPhone().setSipRequest(sipRequest);
		this.getPhone().pickup();
		this.getCurrentIncomingCallRecord().setActionType(ActionType.PICKUPED);
		String phoneNumber = getIncomingPhoneNumber();
		Integer value = pickupHistory.get(phoneNumber);
		if (null == value) {
			value = 0;
		}
		pickupHistory.put(phoneNumber, ++value);
		setPhoneStatus(PhoneStatus.accepted);
	}

	public int getPickupTotalTimes(String phoneNumber) {
		Integer times = pickupHistory.get(phoneNumber);
		if (null == times) {
			return 0;
		}
		return times;
	}

	@Override
	public PhoneType getPhoneListenerType() {
		return PhoneType.AUTO_PICKUP;
	}

	@Override
	public PhoneType getType() {
		return PhoneType.AUTO_PICKUP;
	}
}
