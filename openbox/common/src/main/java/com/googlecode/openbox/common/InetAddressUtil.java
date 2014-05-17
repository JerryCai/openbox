package com.googlecode.openbox.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressUtil {

	public static String getLocalHostIP() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostAddress();
		} catch (UnknownHostException e) {
			throw new RuntimeException("it can't get local host ip error as :",
					e);
		}
	}

	public static String getLocalHostName() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (UnknownHostException e) {
			throw new RuntimeException(
					"it can't get local host name error as :", e);
		}
	}
}
