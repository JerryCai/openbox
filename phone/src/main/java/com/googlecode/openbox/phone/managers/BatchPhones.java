package com.googlecode.openbox.phone.managers;

public interface BatchPhones {

	void setOperationInterval(int senconds);

	void register();

	void unregister();

	void dial(String phoneNumber, String callId);

	void dial(String phoneNumber);

	void invite(String requestUri, String callId);

	void invite(String requestUri);

	void reject();

	void pickup();

	void sendDTMF(String dtmf);

	void hangUp();

	void close();

}
