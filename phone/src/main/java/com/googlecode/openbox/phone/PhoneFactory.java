package com.googlecode.openbox.phone;

import com.googlecode.openbox.phone.listeners.PhoneType;

public final class PhoneFactory {
	private PhoneFactory() {
	}

	public static RegistedPhone createRegistedPhone(String domain,
			String phoneNumber, String password) {
		return RegistedPhone.createPhone(domain, phoneNumber, password);
	}

	public static AnonymousPhone createAnymousPhone(String phoneNumber) {
		AnonymousPhone anymousPhone = AnonymousPhone.createPhone(phoneNumber);
		anymousPhone.setPhoneType(PhoneType.AUTO_PICKUP);
		return anymousPhone;
	}

}
