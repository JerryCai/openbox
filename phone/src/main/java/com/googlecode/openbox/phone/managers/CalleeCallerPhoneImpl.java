package com.googlecode.openbox.phone.managers;

import com.googlecode.openbox.phone.Phone;
import com.googlecode.openbox.phone.listeners.SwitchablePhoneController;

public class CalleeCallerPhoneImpl implements CalleeCallerPhone {

	private Phone callee;
	private Phone caller;

	private BatchPhones batchPhones;

	private SwitchablePhoneController calleeSwitchablePhoneController;
	private SwitchablePhoneController callerSwitchablePhoneController;

	private CalleeCallerPhoneImpl(Phone callee, Phone caller) {
		this.callee = callee;
		this.caller = caller;
		this.batchPhones = BatchPhonesImpl.newInstance(callee, caller);
		this.calleeSwitchablePhoneController = SwitchablePhoneController
				.newInstance(callee);
		this.callerSwitchablePhoneController = SwitchablePhoneController
				.newInstance(caller);
	}

	public static CalleeCallerPhoneImpl newInstance(Phone callee, Phone caller) {
		return new CalleeCallerPhoneImpl(callee, caller);
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

	@Override
	public SwitchablePhoneController getCalleeSwitchablePhoneController() {
		return calleeSwitchablePhoneController;
	}

	@Override
	public SwitchablePhoneController getCallerSwitchablePhoneController() {
		return callerSwitchablePhoneController;

	}

	@Override
	public Phone getCallerPhone() {
		return caller;
	}

	@Override
	public Phone getCalleePhone() {
		return callee;
	}

}
