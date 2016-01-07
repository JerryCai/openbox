package com.googlecode.openbox.phone;

import net.sourceforge.peers.sip.RFC3261;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.*;

public class PhoneUtils {
	


	public static String getLocalHostIp() {
		String ip = "localhost";
		try {
			ip = getLocalHostLANAddress().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}

	public static InetAddress getLocalHostLANAddress()
			throws UnknownHostException {
		try {
			InetAddress candidateAddress = null;
			// Iterate all NICs (network interface cards)...

			Enumeration<NetworkInterface> ifaces = NetworkInterface
					.getNetworkInterfaces();

			List<NetworkInterface> ifacesList = Collections.list(ifaces);
			Collections.sort(ifacesList, new Comparator<NetworkInterface>(){
				@Override
				public int compare(NetworkInterface o1, NetworkInterface o2) {
					return o1.getIndex() - o2.getIndex();
				}});
			for (NetworkInterface iface : ifacesList) {
				// Iterate all IP addresses assigned to each card...
				for (Enumeration<InetAddress> inetAddrs = iface
						.getInetAddresses(); inetAddrs.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs
							.nextElement();
					if (!inetAddr.isLoopbackAddress()) {

						if (inetAddr.isSiteLocalAddress()) {
							// Found non-loopback site-local address. Return it
							// immediately...
							return inetAddr;
						} else if (candidateAddress == null) {
							// Found non-loopback address, but not necessarily
							// site-local.
							// Store it as a candidate to be returned if
							// site-local address is not subsequently found...
							candidateAddress = inetAddr;
							// Note that we don't repeatedly assign non-loopback
							// non-site-local addresses as candidates,
							// only the first. For subsequent iterations,
							// candidate will be non-null.
						}
					}
				}
			}
			if (candidateAddress != null) {
				// We did not find a site-local address, but we found some other
				// non-loopback address.
				// Server might have a non-site-local address assigned to its
				// NIC (or it might be running
				// IPv6 which deprecates the "site-local" concept).
				// Return this non-loopback candidate address...
				return candidateAddress;
			}
			// At this point, we did not find a non-loopback address.
			// Fall back to returning whatever InetAddress.getLocalHost()
			// returns...
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			if (jdkSuppliedAddress == null) {
				throw new UnknownHostException(
						"The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
			}
			return jdkSuppliedAddress;
		} catch (Exception e) {
			UnknownHostException unknownHostException = new UnknownHostException(
					"Failed to determine LAN address: " + e);
			unknownHostException.initCause(e);
			throw unknownHostException;
		}
	}


	public static int[] getAvailablePorts(int portNumber) throws IOException {
		int[] result = new int[portNumber];
		List<ServerSocket> servers = new ArrayList<ServerSocket>(portNumber);
		ServerSocket tempServer = null;

		for (int i = 0; i < portNumber; i++) {
			try {
				tempServer = new ServerSocket(0);
				servers.add(tempServer);
				result[i] = tempServer.getLocalPort();
			} finally {
				for (ServerSocket server : servers) {
					try {
						server.close();
					} catch (IOException e) {
						e.printStackTrace();
						// Continue closing servers.
					}
				}
			}
		}
		return result;
	}

	public static String getSipUrl(String phoneNumber,String domain){
		return RFC3261.SIP_SCHEME + RFC3261.SCHEME_SEPARATOR
				+ phoneNumber + RFC3261.AT + domain;
	}
}
