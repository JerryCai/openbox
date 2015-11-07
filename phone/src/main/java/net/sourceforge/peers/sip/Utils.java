/*
    This file is part of Peers, a java SIP softphone.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Copyright 2007, 2008, 2009, 2010 Yohann Martineau 
*/

package net.sourceforge.peers.sip;

import java.net.InetAddress;

import net.sourceforge.peers.sip.core.useragent.UAS;
import net.sourceforge.peers.sip.syntaxencoding.SipHeaderFieldMultiValue;
import net.sourceforge.peers.sip.syntaxencoding.SipHeaderFieldName;
import net.sourceforge.peers.sip.syntaxencoding.SipHeaderFieldValue;
import net.sourceforge.peers.sip.syntaxencoding.SipHeaders;
import net.sourceforge.peers.sip.transport.SipMessage;

public class Utils {

	public static final String PEERSHOME_SYSTEM_PROPERTY = "peers.home";
	public static final String DEFAULT_PEERS_HOME = ".";

	public final static SipHeaderFieldValue getTopVia(SipMessage sipMessage) {
		SipHeaders sipHeaders = sipMessage.getSipHeaders();
		SipHeaderFieldName viaName = new SipHeaderFieldName(RFC3261.HDR_VIA);
		SipHeaderFieldValue via = sipHeaders.get(viaName);
		if (via instanceof SipHeaderFieldMultiValue) {
			via = ((SipHeaderFieldMultiValue) via).getValues().get(0);
		}
		return via;
	}

	public final static String generateTag() {
		return randomString(8);
	}

	public final static String generateCallID(InetAddress inetAddress) {
		// TODO make a hash using current time millis, public ip @, private @,
		// and a random string
		StringBuffer buf = new StringBuffer();
		buf.append(randomString(8));
		buf.append('-');
		buf.append(String.valueOf(System.currentTimeMillis()));
		buf.append('@');
		buf.append(inetAddress.getHostName());
		return buf.toString();
	}

	public final static String generateBranchId() {
		StringBuffer buf = new StringBuffer();
		buf.append(RFC3261.BRANCHID_MAGIC_COOKIE);
		// TODO must be unique across space and time...
		buf.append(randomString(9));
		return buf.toString();
	}

	public final static String getMessageCallId(SipMessage sipMessage) {
		SipHeaderFieldValue callId = sipMessage.getSipHeaders().get(new SipHeaderFieldName(RFC3261.HDR_CALLID));
		return callId.getValue();
	}

	public final static String randomString(int length) {
		String chars = "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIFKLMNOPRSTUVWXYZ" + "0123456789";
		StringBuffer buf = new StringBuffer(length);
		for (int i = 0; i < length; ++i) {
			int pos = (int) Math.round(Math.random() * (chars.length() - 1));
			buf.append(chars.charAt(pos));
		}
		return buf.toString();
	}

	public final static void copyHeader(SipMessage src, SipMessage dst, String name) {
		SipHeaderFieldName sipHeaderFieldName = new SipHeaderFieldName(name);
		SipHeaderFieldValue sipHeaderFieldValue = src.getSipHeaders().get(sipHeaderFieldName);
		if (sipHeaderFieldValue != null) {
			dst.getSipHeaders().add(sipHeaderFieldName, sipHeaderFieldValue);
		}
	}

	public final static String getUserPart(String sipUri) {
		int start = sipUri.indexOf(RFC3261.SCHEME_SEPARATOR);
		int end = sipUri.indexOf(RFC3261.AT);
		return sipUri.substring(start + 1, end);
	}

	/**
	 * adds Max-Forwards Supported and Require headers
	 * 
	 * @param headers
	 */
	public final static void addCommonHeaders(SipHeaders headers) {
		// Max-Forwards

		headers.add(new SipHeaderFieldName(RFC3261.HDR_MAX_FORWARDS),
				new SipHeaderFieldValue(String.valueOf(RFC3261.DEFAULT_MAXFORWARDS)));

		// TODO Supported and Require
	}

	public final static String generateAllowHeader() {
		StringBuffer buf = new StringBuffer();
		for (String supportedMethod : UAS.SUPPORTED_METHODS) {
			buf.append(supportedMethod);
			buf.append(", ");
		}
		int bufLength = buf.length();
		buf.delete(bufLength - 2, bufLength);
		return buf.toString();
	}

	public final static String getSipUrlFromHeader(SipMessage sipMessage, String headerName) {
		// <sip:001@10.224.89.189;user=phone> to sip:001@10.224.89.189
		// <sip:5510571803@hf3givr.qa.webex.com>;tag=d466496deee07b0f+10.224.89.189
		SipHeaders sipHeaders = sipMessage.getSipHeaders();
		SipHeaderFieldName viaName = new SipHeaderFieldName(headerName);
		SipHeaderFieldValue via = sipHeaders.get(viaName);
		if (via instanceof SipHeaderFieldMultiValue) {
			via = ((SipHeaderFieldMultiValue) via).getValues().get(0);
		}
		return via.getValue().substring(via.getValue().indexOf(RFC3261.LEFT_ANGLE_BRACKET) + 1,
				via.getValue().indexOf(RFC3261.RIGHT_ANGLE_BRACKET)).split(RFC3261.PARAM_SEPARATOR)[0];
	}

	public final static String getSipHeaderFullValue(SipMessage sipMessage, String headerName) {
		// sip:1788121@10.140.202.192:53436;tag=VK5rhHYFAg
		String valueStr = sipMessage.getSipHeaders().get(new SipHeaderFieldName(headerName)).toString();
		if(valueStr.indexOf(RFC3261.LEFT_ANGLE_BRACKET) >=0){
			return valueStr;
		}
		int pos = valueStr.indexOf(RFC3261.PARAM_SEPARATOR);
		if (pos > 0) {
			return RFC3261.LEFT_ANGLE_BRACKET + valueStr.substring(0, pos) + RFC3261.RIGHT_ANGLE_BRACKET
					+ valueStr.substring(pos);
		}
		return RFC3261.LEFT_ANGLE_BRACKET + valueStr + RFC3261.RIGHT_ANGLE_BRACKET;
	}

	public static void resetSipRequestHeader(SipMessage sipMessage, String headerName, String headerValue) {
		SipHeaderFieldName name = new SipHeaderFieldName(headerName);
		sipMessage.getSipHeaders().add(name, new SipHeaderFieldValue(headerValue));
	}
	
	public static void resetSipRequestHeader(SipMessage sipMessage, SipHeaderFieldName headerName, SipHeaderFieldValue headerValue) {
		sipMessage.getSipHeaders().add(headerName, headerValue);
	}
}