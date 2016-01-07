package com.googlecode.openbox.phone;

import net.sourceforge.peers.Config;
import net.sourceforge.peers.JavaConfig;
import net.sourceforge.peers.javaxsound.JavaxSoundManager;
import net.sourceforge.peers.media.AbstractSoundManager;
import net.sourceforge.peers.media.MediaMode;
import net.sourceforge.peers.sip.RFC3261;
import net.sourceforge.peers.sip.core.useragent.UserAgent;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.InetAddress;

public class AnonymousPhone extends AbstractPhone {
	private static final Logger logger = LogManager.getLogger();

	private UserAgent userAgent;

	private Config config;

	private AbstractSoundManager soundManager;
	private File tempMediaFile;

	public static AnonymousPhone createPhone(String phoneNumber) {
		return new AnonymousPhone(phoneNumber);
	}

	public AnonymousPhone(String phoneNumber) {
		super();
		try {
			this.config = new JavaConfig();
			int[] freePorts = PhoneUtils.getAvailablePorts(1);
			this.config.setSipPort(freePorts[0]);
			this.config.setRtpPort(0);
			this.config.setMediaMode(MediaMode.captureAndPlayback);
			this.config.setMediaDebug(false);
			this.config.setUserPart(phoneNumber);
			InetAddress localInetAddress = PhoneUtils.getLocalHostLANAddress();
			this.config.setLocalInetAddress(localInetAddress);
			this.config
					.setDomain(localInetAddress.getHostAddress() + RFC3261.SCHEME_SEPARATOR + this.config.getSipPort());

			if (SystemUtils.IS_OS_LINUX) {
//				tempMediaFile = File.createTempFile("temp_javasoftphone_", ".phone");
//				if (logger.isDebugEnabled()) {
//					logger.debug("create temp media file [" + tempMediaFile + "]");
//				}
//				this.config.setMediaFile(tempMediaFile.getAbsolutePath());
//				this.soundManager = new LinuxFileSoundManager(LOGGER);
				
				this.soundManager = new LinuxDtmfSuccessSoundManager();
				logger.warn("detected it on linux system , so disable its real audio");
			} else {
				String peersHome = new File("").getAbsolutePath();
				this.soundManager = new JavaxSoundManager(this.config.isMediaDebug(), LOGGER, peersHome);
			}
		} catch (Exception e) {
			throw new PhoneException("init java sip softphone config error , please check error logs", e);
		}

	}

	@Override
	public void register() {
		try {

			userAgent = new UserAgent(getSipListener(), this.config, LOGGER, soundManager);
		} catch (Exception e) {
			throw new PhoneException("setup java sip softphone error , please check error logs", e);
		}
	}

	@Override
	public void call(final String phoneNumber, final String callId) {
		if (null == getUserAgent().getConfig().getDomain()) {
			throw new PhoneException(
					"It seems Anymous Phone , It can't support dial action , please use invite with SIPURI mode");
		}
	}

	@Override
	public UserAgent getUserAgent() {
		return userAgent;
	}

	@Override
	Config getConfig() {
		return config;
	}

	@Override
	File getTempMediaFile() {
		return tempMediaFile;
	}

	@Override
	public void disableRealVoice() {
		this.soundManager = new LinuxDtmfSuccessSoundManager();
	}
}
