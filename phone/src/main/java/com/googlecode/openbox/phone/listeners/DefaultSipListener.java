package com.googlecode.openbox.phone.listeners;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.peers.sip.RFC3261;
import net.sourceforge.peers.sip.core.useragent.SipListener;
import net.sourceforge.peers.sip.core.useragent.UserAgent;
import net.sourceforge.peers.sip.syntaxencoding.SipHeaderFieldName;
import net.sourceforge.peers.sip.transport.SipRequest;
import net.sourceforge.peers.sip.transport.SipResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.phone.Phone;
import com.googlecode.openbox.phone.PhoneException;
import com.googlecode.openbox.phone.PhoneStatus;

public abstract class DefaultSipListener implements SipListener {
	private static final Logger logger = LogManager.getLogger();

	private Phone phone;
	private PhoneStatus phoneStatus;
	private SipRequest currentSipRequest;

	private List<IncomingCallRecord> incomingHistory;
	private IncomingCallRecord currentIncomingCallRecord;

	public DefaultSipListener(Phone phone) {
		this.phone = phone;
		this.phone.setSipListener(this);
		this.incomingHistory = new LinkedList<IncomingCallRecord>();
	}

	public abstract PhoneType getType();

	public int getTotalIncomingCallTimes() {
		return incomingHistory.size();
	}

	public List<IncomingCallRecord> getIncomingHistory() {
		return incomingHistory;
	}

	public abstract PhoneType getPhoneListenerType();

	@Override
	public void registering(SipRequest sipRequest) {
		setPhoneStatus(PhoneStatus.registering);
		this.currentSipRequest = sipRequest;
		if (logger.isInfoEnabled()) {
			logger.info("" + this.phone + "is " + phoneStatus);
		}

	}

	@Override
	public void registerSuccessful(SipResponse sipResponse) {
		setPhoneStatus(PhoneStatus.registerSuccessful);
		if (logger.isInfoEnabled()) {
			logger.info("" + this.phone + "is " + phoneStatus);
		}
	}

	@Override
	public void registerFailed(SipResponse sipResponse) {
		setPhoneStatus(PhoneStatus.registerFailed);
		if (logger.isInfoEnabled()) {
			logger.info("" + this.phone + "is " + phoneStatus);
		}

	}

	@Override
	public void incomingCall(SipRequest sipRequest, SipResponse provResponse) {
		setPhoneStatus(PhoneStatus.incomingCall);
		if (logger.isInfoEnabled()) {
			logger.info("" + this.phone + "is " + phoneStatus);
		}
		this.currentSipRequest = sipRequest;
		this.currentIncomingCallRecord = IncomingCallRecord
				.newInstance(_getIncomingCallNumber());
		this.incomingHistory.add(currentIncomingCallRecord);
		if (logger.isInfoEnabled()) {
			logger.info("" + getPhone() + " totalIncomingCallTimes=["
					+ getTotalIncomingCallTimes() + "]");
		}
	}

	@Override
	public void remoteHangup(SipRequest sipRequest) {
		setPhoneStatus(PhoneStatus.remoteHangup);
		this.currentSipRequest = sipRequest;
		// this.phone.setSipRequest(null);//remote hangup , set it to null
		if (logger.isInfoEnabled()) {
			logger.info("" + this.phone + "is " + phoneStatus);
		}

	}

	@Override
	public void ringing(SipResponse sipResponse) {
		setPhoneStatus(PhoneStatus.ringing);
		if (logger.isInfoEnabled()) {
			logger.info("" + this.phone + "is " + phoneStatus);
		}
	}

	@Override
	public void calleePickup(SipResponse sipResponse) {
		setPhoneStatus(PhoneStatus.calleePickup);
		if (logger.isInfoEnabled()) {
			logger.info("" + this.phone + "is " + phoneStatus);
		}

	}

	@Override
	public void error(SipResponse sipResponse) {
		setPhoneStatus(PhoneStatus.error);
		if (logger.isInfoEnabled()) {
			logger.info("" + this.phone + "is " + phoneStatus);
		}
	}

	public PhoneStatus getStatus() {
		return phoneStatus;
	}

	protected void setPhoneStatus(PhoneStatus phoneStatus) {
		this.phoneStatus = phoneStatus;
	}

	public SipRequest getCurrentSipRequest() {
		return currentSipRequest;
	}

	public Phone getPhone() {
		return phone;
	}

	public UserAgent getUserAgent() {
		return this.getPhone().getUserAgent();
	}

	public SipRequest getIncomingSipRequestFor(String what) {
		if (getStatus() != PhoneStatus.incomingCall) {
			throw new PhoneException("current " + getPhone() + " status is ["
					+ getStatus() + "] , it isn't [" + PhoneStatus.incomingCall
					+ "], so it can't " + what + " this incoming call");
		}
		SipRequest incomingSipRequest = getCurrentSipRequest();
		if (null == incomingSipRequest) {
			throw new PhoneException("curret " + getPhone() + "status is ["
					+ getStatus()
					+ "] , but its incoming sipRequest is null , can't " + what
					+ " this incoming call !");
		}
		return incomingSipRequest;
	}

	private String _getIncomingCallNumber() {
		String from = getIncomingSipRequestFor("get incoming call number")
				.getSipHeaders().get(new SipHeaderFieldName(RFC3261.HDR_FROM))
				.getValue();
		return from.substring(from.indexOf("<sip:") + 5, from.indexOf("@"));
	}

	public String getIncomingPhoneNumber() {
		return this.currentIncomingCallRecord.getIncomingPhoneNumber();
	}

	public void printIncomingHandleResult(String actionDescription) {
		String phoneNumber = this.currentIncomingCallRecord
				.getIncomingPhoneNumber();
		if (logger.isInfoEnabled()) {
			logger.info("current " + getPhone() + " upcoming phone number is ["
					+ phoneNumber + "],automatically " + actionDescription
					+ " it success .");
		}

	}

	public IncomingCallRecord getCurrentIncomingCallRecord() {
		return currentIncomingCallRecord;
	}

}
