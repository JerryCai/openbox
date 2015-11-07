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
    
    Copyright 2007-2013 Yohann Martineau 
 */

package net.sourceforge.peers.sip.core.useragent.handlers;

import net.sourceforge.peers.Logger;
import net.sourceforge.peers.sip.RFC3261;
import net.sourceforge.peers.sip.core.useragent.RequestManager;
import net.sourceforge.peers.sip.core.useragent.SipListener;
import net.sourceforge.peers.sip.core.useragent.UserAgent;
import net.sourceforge.peers.sip.syntaxencoding.SipHeaderFieldName;
import net.sourceforge.peers.sip.syntaxencoding.SipHeaderFieldValue;
import net.sourceforge.peers.sip.syntaxencoding.SipUriSyntaxException;
import net.sourceforge.peers.sip.transaction.InviteServerTransaction;
import net.sourceforge.peers.sip.transaction.ServerTransaction;
import net.sourceforge.peers.sip.transaction.TransactionManager;
import net.sourceforge.peers.sip.transactionuser.Dialog;
import net.sourceforge.peers.sip.transactionuser.DialogManager;
import net.sourceforge.peers.sip.transport.SipRequest;
import net.sourceforge.peers.sip.transport.SipResponse;
import net.sourceforge.peers.sip.transport.TransportManager;

public class ReferHandler extends ByeHandler {
	public static final String NOTIFY_PENDING_EXPIRES_180 = "pending";
	public static final String NOTIFY_TERMINATED_REASON_NORESOURCE = "terminated";

	public ReferHandler(UserAgent userAgent, DialogManager dialogManager, TransactionManager transactionManager,
			TransportManager transportManager, Logger logger) {
		super(userAgent, dialogManager, transactionManager, transportManager, logger);
	}

	public void handleInitialRefer(SipRequest sipRequest) {

		// generate 180 Ringing
		SipResponse sipResponse = buildGenericResponse(sipRequest, RFC3261.CODE_180_RINGING,
				RFC3261.REASON_180_RINGING);
		Dialog dialog = buildDialogForUas(sipResponse, sipRequest);
		// here dialog is already stored in dialogs in DialogManager
		InviteServerTransaction inviteServerTransaction = (InviteServerTransaction) transactionManager
				.createServerTransaction(sipResponse, userAgent.getSipPort(), RFC3261.TRANSPORT_UDP, this, sipRequest);
		inviteServerTransaction.start();
		inviteServerTransaction.receivedRequest(sipRequest);
		dialog.receivedOrSent2xx();
	}

	public void handleBye(SipRequest sipRequest, Dialog dialog) {
		sendAcceptedResponse(sipRequest, dialog);
		sendNotifyRequest(sipRequest, NOTIFY_PENDING_EXPIRES_180+";expires=180",dialog);
		sendReferTo(sipRequest, dialog);
		sendNotifyRequest(sipRequest, NOTIFY_TERMINATED_REASON_NORESOURCE+";reason=noresource",dialog);
		sendByeResponse(sipRequest, dialog);
	}

	protected void sendNotifyRequest(SipRequest  referRequest, String subscriptionState,Dialog dialog) {
		try {
			userAgent.notify(referRequest, subscriptionState);
		} catch (SipUriSyntaxException e) {
			logger.error("NOTIFY error", e);
			e.printStackTrace();
		}
	}

	protected void sendByeResponse(SipRequest sipRequest, Dialog dialog) {
		dialog.receivedOrSentBye();
		
		
		String addrSpec = sipRequest.getRequestUri().toString();
		userAgent.getPeers().remove(addrSpec);
		dialogManager.removeDialog(dialog.getId());
		logger.debug("removed dialog " + dialog.getId());
		userAgent.getMediaManager().stopSession();

		SipResponse sipResponse = RequestManager.generateResponse(sipRequest, dialog, RFC3261.CODE_200_OK,
				RFC3261.REASON_200_OK);
		ServerTransaction serverTransaction = transactionManager.createServerTransaction(sipResponse,
				userAgent.getSipPort(), RFC3261.TRANSPORT_UDP, this, sipRequest);
		serverTransaction.start();
		serverTransaction.receivedRequest(sipRequest);
		serverTransaction.sendReponse(sipResponse);
		dialogManager.removeDialog(dialog.getId());
		SipListener sipListener = userAgent.getSipListener();
		if (sipListener != null) {
			sipListener.remoteHangup(sipRequest);
		}
	}

	protected void sendAcceptedResponse(SipRequest sipRequest, Dialog dialog) {
		SipResponse sipResponse = RequestManager.generateResponse(sipRequest, dialog, RFC3261.CODE_202_ACCEPTED,
				RFC3261.REASON_202_ACCEPTED);
		ServerTransaction serverTransaction = transactionManager.createServerTransaction(sipResponse,
				userAgent.getSipPort(), RFC3261.TRANSPORT_UDP, this, sipRequest);

		serverTransaction.sendReponse(sipResponse);
		serverTransaction.start();
		serverTransaction.receivedRequest(sipRequest);
	}

	protected void sendNotifyResponse(SipRequest sipRequest, Dialog dialog, String subscriptionState) {
		SipResponse sipNotifyPendingResponse = RequestManager.generateResponse(sipRequest, dialog, RFC3261.CODE_200_OK,
				RFC3261.REASON_200_OK);
		sipNotifyPendingResponse.getSipHeaders().add(new SipHeaderFieldName(RFC3261.HDR_SUBSCRIPTION_STATE),
				new SipHeaderFieldValue(subscriptionState));
		sipNotifyPendingResponse.getSipHeaders().add(new SipHeaderFieldName(RFC3261.HDR_EVENT),
				new SipHeaderFieldValue("refer"));
		ServerTransaction notifyPendingServerTransaction = transactionManager.createServerTransaction(
				sipNotifyPendingResponse, userAgent.getSipPort(), RFC3261.TRANSPORT_UDP, this, sipRequest);
		notifyPendingServerTransaction.start();
//		notifyPendingServerTransaction.sendReponse(sipNotifyPendingResponse);
		notifyPendingServerTransaction.receivedRequest(sipRequest);
	}

	private void sendReferTo(SipRequest sipRequest, Dialog dialog) {
		
		SipHeaderFieldValue reInviteSipAddress = sipRequest.getSipHeaders()
				.get(new SipHeaderFieldName(RFC3261.HDR_REFER_TO));
		if (null == reInviteSipAddress) {
			return;
		}
		String reInviteSipUrl = reInviteSipAddress.getValue();
		dialog.setRemoteUri(reInviteSipUrl);
		try {
			userAgent.invite(reInviteSipUrl, dialog.getCallId() + "_" + RFC3261.HDR_REFER_TO);
		} catch (SipUriSyntaxException e) {
			e.printStackTrace();
		}
	}

}
