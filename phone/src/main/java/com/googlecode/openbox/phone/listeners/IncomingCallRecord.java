package com.googlecode.openbox.phone.listeners;

public class IncomingCallRecord {
	enum ActionType {
		NOHANDLE, PICKUPED, REJECTED, NOANSWER;
	}

	private ActionType actionType;
	private String incomingCallNumber;
	private long incomingTime;

	public IncomingCallRecord(String incomingCallNumber) {
		this.actionType = ActionType.NOHANDLE;
		this.incomingCallNumber = incomingCallNumber;
		this.incomingTime = System.currentTimeMillis();
	}

	public static IncomingCallRecord newInstance(String incomingCallNumber) {
		return new IncomingCallRecord(incomingCallNumber);
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public String getIncomingPhoneNumber() {
		return incomingCallNumber;
	}

	public long getIncomingTime() {
		return incomingTime;
	}

}
