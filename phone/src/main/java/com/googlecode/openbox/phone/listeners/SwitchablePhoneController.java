package com.googlecode.openbox.phone.listeners;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.phone.Phone;

public class SwitchablePhoneController {
	private static final Logger logger = LogManager.getLogger();
	
	private Phone phone;
	private Map<PhoneType , DefaultSipListener> phoneListeners;
	
	private PhoneType currentPhoneListenerType;
	
	public static SwitchablePhoneController newInstance(Phone phone){
		return new SwitchablePhoneController(phone);		
	}
	
	private SwitchablePhoneController(Phone phone) {
		this.phone = phone;
		this.phoneListeners = new HashMap<PhoneType,DefaultSipListener>(4);	
		this.phoneListeners.put(PhoneType.AUTO_PICKUP, AutoPickupPhoneListener.createAndRegisterToPhoneListener(phone));
		this.phoneListeners.put(PhoneType.REJECT, AutoRejectIncommingPhoneListener.createAndRegisterToPhoneListener(phone));
		this.phoneListeners.put(PhoneType.NO_ANSWER, NoAnswerIncommingPhoneListener.createAndRegisterToPhoneListener(phone));
	}
	
	public Phone getPhone(){
		return phone;
	}
	
	public DefaultSipListener getSipListener(){
		return phone.getSipListener();
	}
	
	public void presetPhoneListenerType(PhoneType phoneListenerType){
		this.currentPhoneListenerType = phoneListenerType;
		this.phone.setSipListener(this.phoneListeners.get(currentPhoneListenerType));
		if(logger.isInfoEnabled()){
			logger.info("preset "+this.phone+" listener type to ["+currentPhoneListenerType+"]");
		}
	}

	public PhoneType getCurrentPhoneListenerType() {
		return currentPhoneListenerType;
	}

}
