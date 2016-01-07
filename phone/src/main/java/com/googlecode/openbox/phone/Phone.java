package com.googlecode.openbox.phone;

import com.googlecode.openbox.phone.listeners.DefaultSipListener;
import com.googlecode.openbox.phone.listeners.PhoneType;
import net.sourceforge.peers.sip.core.useragent.UserAgent;
import net.sourceforge.peers.sip.transport.SipRequest;

public interface Phone {

	void setOperationInterval(int senconds);

	void disableRealVoiceOnWindows();

	void disableRealVoice();

	UserAgent getUserAgent();
	
	String getCallID();

	String getPhoneNumber();

	String getPhoneSipAddress();

	DefaultSipListener getSipListener();

	void setSipListener(DefaultSipListener sipListener);

	void register();

	void setPhoneType(PhoneType phoneListenerType);

	void unregister();

	void call(Phone anotherPhone);

	void call(String phoneNumber, String callId);

	void call(String phoneNumber);

	void invite(String requestUri, String callId);

	void invite(String requestUri);

	void reject();

	void pickup();

	SipRequest getSipRequest();

	void setSipRequest(SipRequest sipRequest);

	void sendDTMF(String dtmf);

	String getIncomingDTMF();

	void hangUp();

	boolean isRegistered();

	void close();

}
