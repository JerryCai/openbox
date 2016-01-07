package com.googlecode.openbox.phone;

import com.googlecode.openbox.phone.listeners.DefaultSipListener;
import net.sourceforge.peers.Config;
import net.sourceforge.peers.JavaConfig;
import net.sourceforge.peers.javaxsound.JavaxSoundManager;
import net.sourceforge.peers.media.AbstractSoundManager;
import net.sourceforge.peers.media.MediaMode;
import net.sourceforge.peers.sip.core.useragent.UserAgent;
import net.sourceforge.peers.sip.syntaxencoding.SipURI;
import net.sourceforge.peers.sip.syntaxencoding.SipUriSyntaxException;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class RegistedPhone extends AbstractPhone {
    private static final Logger logger = LogManager.getLogger();
    private UserAgent userAgent;
    private AbstractSoundManager soundManager;
    private Config config;
    private File tempMediaFile;

    public static RegistedPhone createPhone(String domain, String phoneNumber, String password) {
        return new RegistedPhone(domain, phoneNumber, password);
    }

    public static RegistedPhone createPhoneWithCustomSipListener(String domain, String phoneNumber, String password,
                                                                 DefaultSipListener sipListener) {
        return new RegistedPhone(domain, phoneNumber, password, sipListener);
    }

    public RegistedPhone(String domain, String phoneNumber, String password) {
        initPhoneConfig(domain, phoneNumber, password);
    }

    public RegistedPhone(String domain, String phoneNumber, String password, DefaultSipListener sipListener) {
        super();
        initPhoneConfig(domain, phoneNumber, password);
        this.setSipListener(sipListener);
    }

    private void initPhoneConfig(String domain, String phoneNumber, String password) {
        try {
            this.config = new JavaConfig();
            this.config.setDomain(domain);
            this.config.setUserPart(phoneNumber);
            this.config.setPassword(password);
            this.config.setOutboundProxy(new SipURI("sip:" + domain));
            this.config.setSipPort(0);
            this.config.setRtpPort(0);
            this.config.setMediaMode(MediaMode.captureAndPlayback);
            this.config.setMediaDebug(false);
            this.config.setLocalInetAddress(PhoneUtils.getLocalHostLANAddress());

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
            throw new PhoneException("init java sip softphone error , please check error logs", e);
        }
    }

    @Override
    public void register() {
        try {

            this.userAgent = new UserAgent(getSipListener(), this.config, LOGGER, soundManager);
            boolean isSuccess = PhoneThread.execute(new PhoneThread(this.getOperationInterval()) {

                @Override
                public void action() {
                    try {
                        userAgent.register();
                        if (logger.isInfoEnabled()) {
                            logger.info("Phone [" + getPhoneNumber() + "] setup java softphone and registed success ");
                        }
                    } catch (SipUriSyntaxException e) {
                        logger.error("Phone [" + getPhoneNumber() + "] setup java softphone and registed error", e);
                        throw new PhoneException(e);
                    }

                }

            });
            if (!isSuccess) {
                throw new PhoneException(
                        "Phone [" + getPhoneNumber() + "]  Thread execute --register phone-- failed !");
            }

        } catch (Exception e) {
            throw new PhoneException(
                    "Phone [" + getPhoneNumber() + "] registing java sip softphone error , please check error logs", e);
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
