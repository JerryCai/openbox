package com.googlecode.openbox.server.ssh;

import com.googlecode.openbox.common.IOUtils;
import com.jcraft.jsch.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * This is Ssh handler , user for handling SSH related event and requirments
 *
 * @author Jerry Cai
 */
public class DefaultSshClient extends AbstractSshClient {

    private static final Logger logger = LogManager.getLogger();

    public static final String SSH_PROFILE = "source /etc/profile;source ~/.bash_profile;source ~/.bashrc\n";
    public static final String SSH_BEY_KEY = "congrats , done for your shell!";
    public static final String SSH_BEY = "\necho '" + SSH_BEY_KEY + "'";

    private String ip;
    private int port;
    private String username;
    private String password;
    private boolean needSource;

    public DefaultSshClient(String ip, int port, String username,
                            String password) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.needSource = true;
    }

    public static DefaultSshClient newInstance(String ip, int port,
                                               String username, String password) {
        return new DefaultSshClient(ip, port, username, password);
    }

    public Session createSession(OutputStream output) {
        JSch jsch = new JSch();

        Session session = null;
        try {
            session = jsch.getSession(username, ip, port);
            session.setPassword(password);

            UserInfo ui = new SshUserInfo(password);
            session.setUserInfo(ui);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setDaemonThread(false);
            session.connect();
            if (logger.isInfoEnabled()) {
                logger.info("\nssh login success to ip=[" + ip + "],port=["
                        + port + "],username=[" + username
                        + "],password=[*******]");
            }
        } catch (Exception e) {
            throw new SshException("create ssh session failed with ip=[" + ip
                    + "],port=[" + port + "],username=[" + username
                    + "],password=[" + password + "]", e);
        }
        return session;
    }

    public void uploadFile(OutputStream output, String fileName,
                           InputStream fileContent, String serverLocation) {
        try {
            _uploadFile(output, fileName, fileContent, serverLocation);
        } catch (Exception e) {
            enableServerSFTP();
            _uploadFile(output, fileName, fileContent, serverLocation);
        }
    }

    private void _uploadFile(OutputStream output, String fileName,
                             InputStream fileContent, String serverLocation) {
        Session session = null;
        ChannelSftp sftp = null;
        OutputStream out = null;
        try {
            session = createSession(output);
            Channel channel = session.openChannel("sftp");
            channel.setOutputStream(output, true);
            channel.setExtOutputStream(output, true);
            channel.connect();
            Thread.sleep(2000);
            sftp = (ChannelSftp) channel;
            sftp.setFilenameEncoding("UTF-8");
            sftp.cd(serverLocation);
            out = sftp.put(fileName, 777);
            Thread.sleep(2000);
            byte[] buffer = new byte[2048];
            int n = -1;
            while ((n = fileContent.read(buffer, 0, 2048)) != -1) {
                out.write(buffer, 0, n);
            }
            out.flush();
            if (logger.isInfoEnabled()) {
                logger.info("upload file [" + fileName + "] to remote ["
                        + serverLocation + "]success");
            }
        } catch (Exception e) {
            throw new SshException(
                    "[GSSH - SFTP]  Exception:" + e.getMessage(), e);
        } finally {
            if (logger.isInfoEnabled()) {
                logger.info("[GSSH]-SFTP exit status is "
                        + sftp.getExitStatus());
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            closeSession(session, sftp);
        }
    }

    public void downloadFile(OutputStream output, String remoteFile,
                             String localFolder, String fileName) {
        try {
            _downloadFile(output, remoteFile, localFolder, fileName);
        } catch (Exception e) {
            enableServerSFTP();
            _downloadFile(output, remoteFile, localFolder, fileName);
        }
    }

    private void _downloadFile(OutputStream output, String remoteFile,
                               String localFolder, String fileName) {
        Session session = null;
        ChannelSftp sftp = null;
        OutputStream out = null;
        try {
            session = createSession(output);
            Channel channel = session.openChannel("sftp");
            channel.connect();
            Thread.sleep(2000);
            sftp = (ChannelSftp) channel;
            sftp.setFilenameEncoding("UTF-8");
            sftp.get(remoteFile, localFolder + "/" + fileName);
            if (logger.isInfoEnabled()) {
                logger.info("download remote file [" + remoteFile
                        + "] to local [" + localFolder + "] with file name ["
                        + fileName + "]");
            }
        } catch (Exception e) {
            throw new SshException(e);
        } finally {
            if (logger.isInfoEnabled()) {
                logger.info("[GSSH]-SFTP exit status is "
                        + sftp.getExitStatus());
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            closeSession(session, sftp);
        }
    }

    public String executeShell(OutputStream output, String shell) {
        String wrapperShell = wrapperInput(shell);

        Session session = null;
        ChannelShell channel = null;
        InputStream in = null;
        try {
            session = createSession(output);
            channel = (ChannelShell) session.openChannel("shell");
            channel.setOutputStream(output, true);
            channel.setExtOutputStream(output, true);
            channel.setPty(Boolean.FALSE);
            in = IOUtils.getInputStreamFromString(wrapperShell);
            channel.setInputStream(in, true);
            channel.connect();
            boolean exit = false;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                while (in.available() > 0) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(in));
                    String outputString = br.readLine();
                    stringBuffer.append(outputString);
                    if (outputString == null) {
                        break;
                    }
                    output.write(outputString.getBytes());
                    if (outputString.indexOf(SSH_BEY_KEY) != -1) {
                        exit = true;
                        break;
                    }
                }
                if (exit) {
                    if (logger.isInfoEnabled()) {
                        logger.info("##############STOP###############");
                    }
                    closeSession(session, channel);
                    break;
                }
            }

            return stringBuffer.toString();
        } catch (Exception e) {
            throw new SshException(e);
        } finally {
            if (logger.isInfoEnabled()) {
                logger.info("[GSSH]-shell exit status is "
                        + channel.getExitStatus());
            }
            closeSession(session, channel);
        }
    }

    public String[] executeCommand(OutputStream output, String command) {

        Session session = null;
        ChannelExec channel = null;
        InputStream in = null;
        StringBuffer response = new StringBuffer();
        try {
            String wrapperCommand = wrapperInput(command);
            output.write("execute below commands:\n".getBytes());
            output.write(wrapperCommand.getBytes());
            output.flush();
            session = createSession(output);
            channel = (ChannelExec) session.openChannel("exec");
            channel.setOutputStream(output, true);
            channel.setExtOutputStream(output, true);
            channel.setPty(Boolean.FALSE);
            channel.setCommand(wrapperCommand);
            in = channel.getInputStream();
            channel.connect();
            Thread.sleep(1000);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
            char[] buffer = new char[2048];
            while (true) {

                int len = -1;
                while (-1 != (len = br.read(buffer))) {
                    bw.write(buffer, 0, len);
                    bw.flush();
                    response.append(buffer, 0, len);
                }
                if (channel.isEOF()) {
                    break;
                }
                if (!channel.isConnected()) {
                    break;
                }
                if (channel.isClosed()) {
                    break;
                }
                Thread.sleep(1000);
            }
            closeSession(session, channel);
        } catch (Exception e) {
            closeSession(session, channel);
            throw new SshException(e);

        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return response.toString().split("\n");
    }

    public String executeSingleCommand(OutputStream out, String command) {
        StringBuilder response;
        Session session;
        ChannelExec channel;
        InputStream in;
        response = new StringBuilder();
        if (logger.isInfoEnabled()) {
            logger.info("execute below commands:");
            logger.info(command);
        }
        session = null;
        channel = null;
        in = null;
        String s;
        try {
            session = createSession(out);
            channel = (ChannelExec) session.openChannel("exec");
            channel.setOutputStream(out, true);
            channel.setExtOutputStream(out, true);
            channel.setPty(Boolean.FALSE.booleanValue());
            channel.setCommand(command);
            in = channel.getInputStream();
            channel.connect();
//            Thread.sleep(1000L);
            TimeUnit.SECONDS.sleep(1);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            do {
                char buffer[] = new char[2048];
                for (int len = -1; -1 != (len = br.read(buffer)); )
                    response.append(buffer, 0, len);

            } while (!channel.isClosed());
            closeSession(session, channel);
            s = response.toString();
        } catch (Exception e) {
            logger.error(
                    (new StringBuilder("[GSSH]-cmd Exception:")).append(
                            e.getMessage()).toString(), e);
            closeSession(session, channel);
            throw new SshException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioexception) {
                }
            }
        }
        return s;
    }

    @Override
    public void startTcpDumpCommand(int port, String dumpPath) {
        String command = SshUtils.getTcpDumpCommand(getIp(), port, dumpPath);
        executeCommand(System.out, command);
    }

    @Override
    public void startFullTcpDumpCommand(String dumpPath) {
        String command = SshUtils.getFullTcpDumpCommand(getIp(), dumpPath);
        executeCommand(System.out, command);
    }

    public boolean testConnection(OutputStream output) {
        try {
            Session session = createSession(output);
            closeSession(session, null);
            return true;
        } catch (Exception e) {
            logger.error("test ssh connection failed !", e);
            return false;
        }
    }

    private void closeSession(Session session, Channel channel) {
        if (channel != null) {
            channel.disconnect();
            channel = null;
        }
        if (session != null) {
            session.disconnect();
            session = null;
        }
    }

    protected String wrapperInput(String input) {
        if (!needSource) {
            return input;
        }
        String output = fixIEIssue(input);
        return SSH_PROFILE + output + SSH_BEY;

    }

    /**
     * this is fix the IE issue that it's input shell /command auto add '<br>
     * ' if \n
     *
     * @param input
     * @return
     */
    private String fixIEIssue(String input) {
        return StringEscapeUtils.unescapeHtml(input);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isNeedSource() {
        return needSource;
    }

    public void setNeedSource(boolean needSource) {
        this.needSource = needSource;
    }

    public String toString() {
        return "Server Info [" + this.ip + " ," + this.port + ","
                + this.username + "," + this.password + "]";
    }
}
