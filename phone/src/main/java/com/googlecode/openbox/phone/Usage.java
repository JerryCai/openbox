package com.googlecode.openbox.phone;

import java.util.concurrent.TimeUnit;

import com.googlecode.openbox.phone.listeners.PhoneType;

public class Usage {

	public static void main(String... args) throws InterruptedException {
		int senconds = 3;
//		Phone phoneCaller = PhoneFactory.createRegistedPhone("10.224.27.131:5060","86181018", "86181018");
//		Phone phoneCallee = PhoneFactory.createRegistedPhone("10.224.27.131:5060","86181019", "86181019");
		
		Phone phoneCaller = PhoneFactory.createAnymousPhone("80000000001");
		Phone phoneCallee = PhoneFactory.createAnymousPhone("80000000002");
		
		phoneCaller.setOperationInterval(senconds);
		phoneCallee.setOperationInterval(senconds);
		
		phoneCaller.setPhoneType(PhoneType.AUTO_PICKUP);
		phoneCallee.setPhoneType(PhoneType.AUTO_PICKUP);
		phoneCaller.register();
		phoneCallee.register();
		phoneCaller.invite(phoneCallee.getPhoneSipAddress());
		phoneCaller.sendDTMF("0123456789*#0");
		phoneCallee.sendDTMF("1");
		TimeUnit.SECONDS.sleep(10);
		phoneCallee.getIncomingDTMF();
		phoneCaller.getIncomingDTMF();
		phoneCaller.hangUp();
		phoneCallee.hangUp();
		phoneCaller.close();
		phoneCallee.close();
		System.exit(0);
	}

}
