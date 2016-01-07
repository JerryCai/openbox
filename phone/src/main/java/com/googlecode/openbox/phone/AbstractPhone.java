package com.googlecode.openbox.phone;

import com.googlecode.openbox.phone.listeners.*;
import net.sourceforge.peers.Config;
import net.sourceforge.peers.sip.Utils;
import net.sourceforge.peers.sip.core.useragent.UserAgent;
import net.sourceforge.peers.sip.syntaxencoding.SipUriSyntaxException;
import net.sourceforge.peers.sip.transactionuser.Dialog;
import net.sourceforge.peers.sip.transactionuser.DialogManager;
import net.sourceforge.peers.sip.transport.SipRequest;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.UUID;

public abstract class AbstractPhone implements Phone {
	public static final net.sourceforge.peers.Logger LOGGER = PhoneLogger
			.getInstance();

	private static final Logger logger = LogManager.getLogger();

	public static final int DEFAULT_OPERATION_INTERVAL = 1;

	private int operationInterval;

	private DefaultSipListener sipListener;

	private SipRequest sipRequest;
	
	private String callId;

	abstract Config getConfig();

	abstract File getTempMediaFile();

	public AbstractPhone() {
		this.operationInterval = DEFAULT_OPERATION_INTERVAL;
	}

	@Override
	public String getPhoneNumber() {
		return getConfig().getUserPart();
	}

	@Override
	public String getCallID(){
		return callId;
	}
	
	@Override
	public String getPhoneSipAddress() {
		return getSameRegistedPhoneSipAddress(getUserAgent().getUserpart());

	}

	private String getSameRegistedPhoneSipAddress(String anotherPhoneNumber) {
		return PhoneUtils.getSipUrl(anotherPhoneNumber, getUserAgent()
				.getDomain());
	}

	@Override
	public void setPhoneType(PhoneType phoneListenerType) {
		switch (phoneListenerType) {
		case AUTO_PICKUP:
			AutoPickupPhoneListener.createAndRegisterToPhoneListener(this);
			break;
		case REJECT:
			AutoRejectIncommingPhoneListener
					.createAndRegisterToPhoneListener(this);
			break;
		case NO_ANSWER:
			NoAnswerIncommingPhoneListener
					.createAndRegisterToPhoneListener(this);
			break;
		default:
			logger.warn("PhoneListenerType [" + phoneListenerType.name()
					+ "] can't added, please check !");
		}

	}

	@Override
	public void call(Phone anotherPhone) {
		invite(anotherPhone.getPhoneSipAddress(), null);
	}

	@Override
	public void call(String phoneNumber) {
		call(phoneNumber, null);
	}

	@Override
	public void call(final String phoneNumber, final String callId) {
		invite(getSameRegistedPhoneSipAddress(phoneNumber), callId);
	}

	@Override
	public void invite(String requestUri) {
		invite(requestUri, null);
	}

	@Override
	public void invite(final String requestUri, final String callId) {
		final String nonEmptyCallId = (callId == null ? "phone_"
				+ UUID.randomUUID().toString() : callId);
		this.callId = nonEmptyCallId;
		boolean isSuccess = PhoneThread.execute(new PhoneThread(
				getOperationInterval()) {
			@Override
			public void action() {
				
				try {

					sipRequest = getUserAgent().invite(requestUri,
							nonEmptyCallId);
					if (logger.isInfoEnabled()) {
						logger.info("Phone [" + getPhoneNumber() + "] invite ["
								+ requestUri + "] , callId [" + nonEmptyCallId
								+ "] success");
					}
				} catch (SipUriSyntaxException e) {
					String msg = "Phone [" + getPhoneNumber() + "] invite ["
							+ requestUri + "] , callId [" + nonEmptyCallId
							+ "] error";
					logger.error(msg, e);
					throw new PhoneException(msg, e);
				}
			}

		});

		if (!isSuccess) {
			throw new PhoneException("Phone [" + getPhoneNumber()
					+ "] Thread execute --invite [" + requestUri
					+ "]-- failed !");
		}
	}

	@Override
	public void sendDTMF(final String dtmf) {
		boolean isSuccess = PhoneThread.execute(new PhoneThread(
				getOperationInterval()) {
			@Override
			public void action() {
				char[] digits = dtmf.toCharArray();
				for (char digit : digits) {
					getUserAgent().getMediaManager().sendDtmf(digit);
				}
				if (logger.isInfoEnabled()) {
					logger.info("Phone [" + getPhoneNumber() + "] sendDTMF["
							+ dtmf + "]");
				}
			}

		});

		if (!isSuccess) {
			throw new PhoneException("Phone [" + getPhoneNumber()
					+ "] Thread execute --sendDTMF [" + dtmf + "]-- failed !");
		}

	}

	@Override
	public String getIncomingDTMF() {
		String dtmf = getUserAgent().getSoundManager().getIncomingDTMFs();
		if (logger.isInfoEnabled()) {
			logger.info("\n " + this + " read incoming DTMF: [" + dtmf + "]");
		}
		return dtmf;
	}

	private SipRequest getIncomingSipRequestFor(String what) {
		return getSipListener().getIncomingSipRequestFor(what);
	}

	private void printIncomingHandleResult(String actionDescription) {
		getSipListener().printIncomingHandleResult(actionDescription);

	}

	abstract class IncomingCallHandler {
		private String actionDescription;

		public IncomingCallHandler(String actionDescription) {
			this.actionDescription = actionDescription;
		}

		abstract void handleIncoming(UserAgent userAgent,
				SipRequest incomingSipRequest);

		public void execute() {
			boolean isSuccess = PhoneThread.execute(new PhoneThread(
					getOperationInterval()) {
				@Override
				public void action() {
					SipRequest incomingSipRequest = getIncomingSipRequestFor(actionDescription);
					handleIncoming(getUserAgent(), incomingSipRequest);
					printIncomingHandleResult(actionDescription);
				}

			});

			if (!isSuccess) {
				throw new PhoneException("Phone [" + getPhoneNumber()
						+ "] Thread execute --" + actionDescription
						+ "-- failed !");
			}
		}

	}

	@Override
	public void reject() {
		new IncomingCallHandler("reject") {

			@Override
			void handleIncoming(UserAgent userAgent,
					SipRequest incomingSipRequest) {
				callId = Utils.getMessageCallId(incomingSipRequest);
				userAgent.rejectCall(incomingSipRequest);
			}

		}.execute();
	}

	@Override
	public void pickup() {
		new IncomingCallHandler("pick up") {

			@Override
			void handleIncoming(UserAgent userAgent,
					SipRequest incomingSipRequest) {
				callId = Utils.getMessageCallId(incomingSipRequest);
				DialogManager dialogManager = getUserAgent().getDialogManager();
				Dialog dialog = dialogManager.getDialog(callId);
				userAgent.acceptCall(incomingSipRequest, dialog);
			}

		}.execute();
	}

	@Override
	public void hangUp() {
		boolean isSuccess = PhoneThread.execute(new PhoneThread(
				getOperationInterval()) {
			@Override
			public void action() {
				if (null != sipRequest) {
					try {
						getUserAgent().terminate(sipRequest);
					} catch (Exception e) {
						// TODO is need to throw exception actually
						logger.error(
								"hang up phone error , ignore this error and continue !",
								e);
					}
					if (logger.isInfoEnabled()) {
						logger.info("Phone [" + getPhoneNumber() + "] hangUp");
					}
				} else {
					logger.warn("Phone [" + getPhoneNumber()
							+ "] skip to hangup since sipRequest is null");
				}
				sipRequest = null;
			}

		});

		if (!isSuccess) {
			throw new PhoneException("Phone [" + getPhoneNumber()
					+ "] Thread execute --hangup-- failed !");
		}

	}

	@Override
	public void unregister() {
		boolean isSuccess = PhoneThread.execute(new PhoneThread(this
				.getOperationInterval()) {

			@Override
			public void action() {
				try {
					if (getUserAgent().isRegistered()) {
						getUserAgent().unregister();
						if (logger.isInfoEnabled()) {
							logger.info("Phone [" + getPhoneNumber()
									+ "] unregisted success ");
						}
					}
				} catch (SipUriSyntaxException e) {
					logger.error("Phone [" + getPhoneNumber()
							+ "] unregisted error", e);
					throw new PhoneException(e);
				}

			}

		});
		if (!isSuccess) {
			throw new PhoneException("Phone [" + getPhoneNumber()
					+ "] Thread execute --unregister phone-- failed !");
		}
	}

	@Override
	public boolean isRegistered() {
		return getUserAgent().isRegistered();
	}

	@Override
	public void close() {
		try {
			unregister();
		} finally {
			boolean isSuccess = PhoneThread.execute(new PhoneThread(
					getOperationInterval()) {
				@Override
				public void action() {
					getUserAgent().close();
					if (logger.isInfoEnabled()) {
						logger.info("Phone [" + getPhoneNumber()
								+ "] close and exit now !");
					}
					File file = getTempMediaFile();
					if (null != file) {
						file.deleteOnExit();
					}
				}

			});

			if (!isSuccess) {
				throw new PhoneException("Phone [" + getPhoneNumber()
						+ "] Thread execute --close-- failed !");
			}
		}
	}

	@Override
	public void disableRealVoiceOnWindows() {
		if (SystemUtils.IS_OS_WINDOWS) {
			disableRealVoice();
			logger.warn(
					"phone is setup on Windows OS , you manual disabled its real voice like on Linux VM System, actually this is only required when multi-threads runing on Windows VM system");

		}
	}

	public int getOperationInterval() {
		return operationInterval;
	}

	public void setOperationInterval(int operationInterval) {
		this.operationInterval = operationInterval;
	}

	public SipRequest getSipRequest() {
		return sipRequest;
	}

	public void setSipRequest(SipRequest sipRequest) {
		this.sipRequest = sipRequest;
	}

	public DefaultSipListener getSipListener() {
		return sipListener;
	}

	public void setSipListener(DefaultSipListener sipListener) {
		this.sipListener = sipListener;
	}

	public String toString() {
		return "PHONE[" + getPhoneNumber() + "]";
	}

}
