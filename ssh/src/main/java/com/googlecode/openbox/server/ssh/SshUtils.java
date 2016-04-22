package com.googlecode.openbox.server.ssh;

import com.googlecode.openbox.common.InputStreamConsumer;
import com.googlecode.openbox.server.Server;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SshUtils {
    private static final Logger logger = LogManager.getLogger();

    private SshUtils() {

    }

    public static void uploadResourceLevelFileToServerLocation(Server server, String resourcePath, String remoteFolder) {
        server.getSshClient().uploadResourceLevelFileToServerLocation(resourcePath,remoteFolder);
    }

    public static void uploadResourceLevelFileToServer(Server server, String resourcePath, String remoteFilePath) {
        server.getSshClient().uploadResourceLevelFileToServer(resourcePath,remoteFilePath);
    }

    public static void uploadFileToServer(Server server, String localFilePath, String remoteFilePath) {
        server.getSshClient().uploadFileToServer(localFilePath, remoteFilePath);
    }

    public static void downloadFileToServer(Server server, String remoteFilePath, String localFilePath) {
       server.getSshClient().downloadFileFromServer(remoteFilePath, localFilePath);
    }

    public static void downloadFolderFromServer(Server server, String remoteLocation, String localLocation) {
       server.getSshClient().downloadFolderFromServer(remoteLocation, localLocation);
    }

    public static void executeCommandOnLocalLinux(String command) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("start to execute local command [/bin/bash -c '"+command+"']");
            }

            ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", command);
            pb.redirectError();
            Process p = pb.start();
            new Thread(new InputStreamConsumer(p.getInputStream(),System.out)).start();
            int result = p.waitFor();
            if (logger.isInfoEnabled()) {
                logger.info("execute local command [/bin/bash -c "+command+"] result ["+result+"]");
            }
        } catch (Exception e) {
            logger.error("execute local command [/bin/bash -c "+command+"] result [error]",e);
            throw new SshException("Execute local linux command [" + command + "] error!", e);

        }
    }

    public static void enableServerSFTP(Server server) {
        server.getSshClient().enableServerSFTP();
    }

    public static String getApplicationPIDByProcess(Server server, String processName) {
        return server.getSshClient().getApplicationPIDByProcess(processName);
    }

    public static String getApplicationPID(Server server, String processName) {
        return server.getSshClient().getApplicationPID(processName);
    }


    public static String getTcpDumpCommand(String ip, int port, String dumpPath) {
        return "tcpdump -i eth0 host " + ip + " and port " + port + " and tcp -X -s 1000 -w " + dumpPath
                + " & \n echo tcp port " + port + " dumping";
    }

    public static String getFullTcpDumpCommand(String ip, String dumpPath) {
        return "tcpdump -i eth0 host " + ip + " -X -s 1000 -w " + dumpPath + " & \n echo full tcp dumping";
    }

    public static void syncNtp(Server server) {
        server.getSshClient().syncNTP();
    }

    public static void startTcpDump(Server server) {
        server.getSshClient().stopTcpDump();
    }

    public static void stopTcpDump(Server server) {
        server.getSshClient().stopTcpDump();
    }

    public static void pkill(Server server, String processName) {
        server.getSshClient().pkill(processName);
    }

    public static String getNtpdSyncCommand() {
        return "service ntpd stop ; ntpd -gq ; service ntpd start";
    }

    public static boolean isInstalledRpm(Server server, String rpmKey) {
        return server.getSshClient().isInstalledRpm(rpmKey);
    }

    public static String getRpmVersion(Server server, String rpmKey) {
        return server.getSshClient().getRpmBuildNumber(rpmKey);
    }

    public static boolean isCommandResponseNotEmpty(String rpmQaResposne) {
        if (StringUtils.isBlank(rpmQaResposne)) {
            return false;
        }
        return true;
    }

    public static void yumInstall(Server server, String rpmKey) {
        server.getSshClient().yumInstall(rpmKey);
    }

    public static boolean isFileExisted(Server server, String filePath) {
        return server.getSshClient().isFileExisted(filePath);
    }

    public static String getVariableValue(Server server, String variable) {
        return server.getSshClient().getVariableValue(variable);
    }

    public static String[] parseCommandResponses(String response, String[] commands){
        String[] responses = new String[commands.length];
        for(int i = 0 ; i< commands.length ; i++) {
            String command = commands[i];
            responses[i] = parseCommandResponse(response, command);
        }
        return responses;
    }

    public static String parseCommandResponse(String response, String command) {
        int start = response.lastIndexOf(command)+command.length()+1;
        int end = response.indexOf("[", start);
        try {
            return response.substring(start, end).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
