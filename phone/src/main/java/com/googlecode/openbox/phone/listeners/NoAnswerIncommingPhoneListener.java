package com.googlecode.openbox.phone.listeners;

import java.util.concurrent.TimeUnit;

import net.sourceforge.peers.sip.transport.SipRequest;
import net.sourceforge.peers.sip.transport.SipResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.phone.Phone;
import com.googlecode.openbox.phone.PhoneStatus;
import com.googlecode.openbox.phone.listeners.IncomingCallRecord.ActionType;

public class NoAnswerIncommingPhoneListener extends DefaultSipListener {
	private static final Logger logger = LogManager.getLogger();

	private NoAnswerIncommingPhoneListener(Phone phone) {
		super(phone);
	}

	public static NoAnswerIncommingPhoneListener createAndRegisterToPhoneListener(
			Phone phone) {
		return new NoAnswerIncommingPhoneListener(phone);
	}

	@Override
	public void incomingCall(SipRequest sipRequest, SipResponse provResponse) {
		super.incomingCall(sipRequest, provResponse);
		for (int i = 0; i < 10; i++) {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (logger.isInfoEnabled()) {
				logger.info("incoming call is ring , but no body to answer this incomming call .......");
			}
		}
		this.getCurrentIncomingCallRecord().setActionType(ActionType.NOANSWER);
		setPhoneStatus(PhoneStatus.noAnswer);
	}

	@Override
	public PhoneType getPhoneListenerType() {
		return PhoneType.NO_ANSWER;
	}
	
	@Override
	public PhoneType getType() {
		return PhoneType.NO_ANSWER;
	}
}
