package com.googlecode.openbox.phone.managers;

import com.googlecode.openbox.phone.listeners.DefaultSipListener;

public class PairPhoneImpl<CallerListener extends DefaultSipListener, CalleeListener extends DefaultSipListener>
		implements PairPhone<CallerListener, CalleeListener> {

	private CallerListener callerListener;
	private CalleeListener calleeListener;

	private BatchPhones batchPhones;

	private PairPhoneImpl(CallerListener callerListener,
			CalleeListener calleeListener) {
		this.callerListener = callerListener;
		this.calleeListener = calleeListener;
		this.batchPhones = BatchPhonesImpl.newInstance(
				this.callerListener.getPhone(), this.calleeListener.getPhone());
	}

	public static <CallerListener extends DefaultSipListener, CalleeListener extends DefaultSipListener> PairPhone<CallerListener, CalleeListener> newInstance(
			CallerListener callerListener, CalleeListener calleeListener) {
		return new PairPhoneImpl<CallerListener, CalleeListener>(callerListener,
				calleeListener);
	}

	@Override
	public void setOperationInterval(int senconds) {
		batchPhones.setOperationInterval(senconds);
	}

	@Override
	public void register() {
		batchPhones.register();

	}

	@Override
	public void unregister() {
		batchPhones.unregister();

	}

	@Override
	public void dial(String phoneNumber, String callId) {
		batchPhones.dial(phoneNumber, callId);

	}

	@Override
	public void dial(String phoneNumber) {
		batchPhones.dial(phoneNumber);

	}

	@Override
	public void invite(String requestUri, String callId) {
		batchPhones.invite(requestUri, callId);

	}

	@Override
	public void invite(String requestUri) {
		batchPhones.invite(requestUri);
	}

	@Override
	public void reject() {
		batchPhones.reject();

	}

	@Override
	public void pickup() {
		batchPhones.pickup();
	}

	@Override
	public void sendDTMF(String dtmf) {
		batchPhones.sendDTMF(dtmf);
	}

	@Override
	public void hangUp() {
		batchPhones.hangUp();
	}

	@Override
	public void close() {
		batchPhones.close();
	}

	public CalleeListener getCalleeListener() {
		return calleeListener;
	}

	public CallerListener getCallerListener() {
		return callerListener;
	}

}
