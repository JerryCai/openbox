package com.googlecode.openbox.server.ssh;

import com.googlecode.openbox.common.IOUtils;
import com.googlecode.openbox.common.UtilsAPI;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Random;

public abstract class AbstractSshClient implements SshClient {
    private static final Logger logger = LogManager.getLogger();

    public final String TEMP_PATH = "/var";
    public final String LATEEST_EXEC_SHELL_DEBUG = "/var/latest_exec_debug.sh";


    public void uploadFile(OutputStream output, String fileName, File file,
                           String serverLocation) {
        if (logger.isInfoEnabled()) {
            logger.info("sftp upload file [" + file + "] to target location ["
                    + serverLocation + "] with file name is [" + fileName + "]");
        }

        InputStream inputStream = null;
        try {
            inputStream = IOUtils.getFileInputStream(file);
            uploadFile(output, fileName, inputStream, serverLocation);
        } catch (Exception e) {
            String message = "[GSSH-FTP] ERROR as with below errors logs:";
            throw new SshException(message, e);
        } finally {
            IOUtils.closeInputStream(inputStream);
        }
    }

    public void uploadFile(OutputStream output, String fileName,
                           String fileContent, String serverLocation) {
        InputStream bis = new ByteArrayInputStream(fileContent.getBytes());
        try {
            uploadFile(output, fileName, bis, serverLocation);
        } finally {
            if (null != bis) {
                try {
                    bis.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void downloadFile(OutputStream output, String remoteFile,
                             String localFolder) {
        File rf = new File(remoteFile);
        downloadFile(output, remoteFile, localFolder, rf.getName());
    }

    public String executeShellByFTP(OutputStream output, InputStream shell) {
        Random random = new Random();

        String shellName = "tempshell_" + System.currentTimeMillis()
                + random.nextInt() + ".sh";
        String shellFile = TEMP_PATH + "/" + shellName;
        try {
            uploadFile(output, shellName, shell, TEMP_PATH);
            chmod(output, 777, shellFile);
            return executeSingleCommand(output, "sh " + shellFile);
        } finally {
            rm_Rf(output, LATEEST_EXEC_SHELL_DEBUG);
            mv(output, shellFile, LATEEST_EXEC_SHELL_DEBUG);
        }
    }

    public String executeShellByFTP(OutputStream output, String shell) {
        Random random = new Random();
        if (logger.isInfoEnabled()) {
            logger.info("execute shell as : ");
            logger.info(shell);
        }
        String shellName = "tempshell_" + System.currentTimeMillis()
                + random.nextInt() + ".sh";

        String shellFile = TEMP_PATH + "/" + shellName;
        try {
            uploadFile(output, shellName, shell, TEMP_PATH);
            chmod(output, 777, shellFile);
            return executeSingleCommand(output, "sh " + shellFile);
        } finally {
            rm_Rf(output, LATEEST_EXEC_SHELL_DEBUG);
            mv(output, shellFile, LATEEST_EXEC_SHELL_DEBUG);
        }
    }

    public void chmod(OutputStream output, int mode, String path) {
        executeCommand(output, "chmod " + mode + " " + path);
    }

    public void chown(OutputStream output, String own, String path) {
        executeCommand(output, "chown " + own + " " + path);
    }

    public void mv(OutputStream output, String source, String dest) {
        executeCommand(output, "mv " + source + " " + dest);
    }

    public void rm_Rf(OutputStream output, String path) {
        executeCommand(output, "rm -rf " + path);
    }

    public void executeCommand(OutputStream output, InputStream command) {
        String content = IOUtils.getStringFromStream(command);
        executeCommand(output, content);
    }

    public void uploadResourceLevelFileToServerLocation(String resourcePath, String remoteFolder) {
        String fileName = UtilsAPI.getLastPath(resourcePath, File.separator);
        String remoteFilePath = remoteFolder + File.separator + fileName;
        uploadResourceLevelFileToServer(resourcePath, remoteFilePath);
    }

    public void uploadResourceLevelFileToServer(String resourcePath, String remoteFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            String fileName = UtilsAPI.getLastPath(remoteFilePath, "/");
            String remoteLocation = UtilsAPI.getParentPath(remoteFilePath, "/");
            uploadFile(System.out, fileName, inputStream, remoteLocation);
        } finally {
            IOUtils.closeInputStream(inputStream);
        }
    }

    public void uploadFileToServer(String localFilePath, String remoteFilePath) {
        String remoteFolder = UtilsAPI.getParentPath(remoteFilePath, "/");
        String remoteFileName = UtilsAPI.getLastPath(remoteFilePath, "/");
        uploadFile(System.out, remoteFileName, new File(localFilePath), remoteFolder);
    }

    public void downloadFileFromServer(String remoteFilePath, String localFilePath) {
        String localFolder = UtilsAPI.getParentPath(localFilePath, "/");
        String localFileName = UtilsAPI.getLastPath(localFilePath, "/");
        downloadFile(System.out, remoteFilePath, localFolder, localFileName);
    }

    public void downloadFolderFromServer(String remoteLocation, String localLocation) {
        //zip -r /opt/webex/dsagent/java/emma_report.zip /opt/webex/dsagent/java/emma_report

        String location = UtilsAPI.getParentPath(remoteLocation, "/");
        String folderName = UtilsAPI.getLastPath(remoteLocation, "/");
        String fileName = folderName + ".zip";

        String localZipedFilePath = localLocation + "/" + fileName;

        String zipCommands = "cd " + location + " && zip -r " + fileName + " " + folderName;
        String remoteZipedFilePath = location + "/" + fileName;
        executeCommand(System.out, zipCommands);
        downloadFileFromServer(remoteZipedFilePath, localZipedFilePath);
        executeCommand(System.out, "rm -rf " + remoteZipedFilePath);
        String unzipDownloadedCommand = " cd " + localLocation + " && unzip -o " + fileName;
        SshUtils.executeCommandOnLocalLinux(unzipDownloadedCommand);
        SshUtils.executeCommandOnLocalLinux("rm -rf " + localZipedFilePath);
    }

    @Override
    public void downloadToRelativeFileFromServerFile(String remoteFilePath, String relativeLocalFilePath) {
        String localFilePath = AbstractSshClient.class.getResource(File.separator + relativeLocalFilePath).getFile();
        downloadFileFromServer(remoteFilePath, localFilePath);
    }

    @Override
    public void downloadToRelativeLocationFromServerFile(String remoteFilePath, String relativeLocalLocation) {
        String fileName = UtilsAPI.getLastPath(remoteFilePath, "/");
        String localFilePath = AbstractSshClient.class.getResource(File.separator + relativeLocalLocation).getFile() + File.separator + fileName;
        downloadFileFromServer(remoteFilePath, localFilePath);
    }

    @Override
    public void downloadToRelativeLocationFromServerFolder(String remoteLocation, String relativeLocalLocation) {
        String localLocation = AbstractSshClient.class.getResource(File.separator + relativeLocalLocation).getFile();
        downloadFolderFromServer(remoteLocation, localLocation);
    }

    public void enableServerSFTP() {
        String command = "grep -q 'Subsystem' /etc/ssh/sshd_config || ( sed -i '$a \\UsePAM yes\\nPermitUserEnvironment yes\\nSubsystem sftp /usr/libexec/openssh/sftp-server' /etc/ssh/sshd_config && /sbin/service sshd restart )";
        executeCommand(System.out, command);
    }


    public String getApplicationPIDByProcess(String processName) {
        String command = "ps -e | grep " + processName + " | xargs | cut -d' ' -f1 | xargs";
        String result = executeSingleCommand(System.out, command);
        if (StringUtils.isBlank(result)) {
            throw new SshException("get [" + processName + "] pid by command [" + command + "] on server:"
                    + this + " result is empty or null, maybe service is shutdown or config incorrect!");
        }

        return result;
    }

    public String getApplicationPID(String processName) {
        String wrappedProcessName = "[" + processName.substring(0, 1) + "]" + processName.substring(1);
        String command = "ps -ef | grep " + wrappedProcessName + " | cut -d: -f1";
        String result = executeSingleCommand(System.out, command);
        if (StringUtils.isBlank(result)) {
            throw new SshException("get [" + processName + "] pid by command [" + command + "] on server:"
                    + this + " result is empty or null, maybe service is shutdown or config incorrect!");
        }

        String pid = result.substring(result.indexOf(" ")).trim();
        return pid.substring(0, pid.indexOf(" "));
    }


    public boolean isInstalledRpm(String rpmKey) {
        String rpmQaResposne = executeSingleCommand(System.out, "rpm -qa | grep " + rpmKey + " ");
        return SshUtils.isCommandResponseNotEmpty(rpmQaResposne);
    }

    @Override
    public String getRpmBuildNumber(String rpmKey) {
        String rpmVersonCommand = "rpm -qa | grep "+rpmKey+" | head -1 | awk -F- '{print $NF}' | cut -d . -f1";
        String response =  executeSingleCommand(System.out,rpmVersonCommand);
        if (StringUtils.isNoneBlank(response)) {
            return response.trim();
        }

        return "not found";
    }
   @Override
    public String getRpmBuildVersion(String rpmKey) {
        String rpmVersonCommand = "rpm -qa | grep "+rpmKey+" | head -1 | cut -d- -f2 ";
        String response =  executeSingleCommand(System.out,rpmVersonCommand);
        if (StringUtils.isNoneBlank(response)) {
            return response.trim();
        }

        return "not found";
    }

    public void yumInstall(String rpmKey) {
        if (isInstalledRpm(rpmKey)) {
            return;
        }
        String commands = "yum install -y " + rpmKey;
        if (StringUtils.isNotBlank(commands)) {
            executeCommand(System.out, commands);
        }
        if (!isInstalledRpm(rpmKey)) {
            throw new RuntimeException("Try auto yum install by [" + commands + "] failed , Please fix or you can manual install it !");
        }
    }

    public boolean isFileExisted(String filePath) {

        String response = "OKOKOK";

        String command = "test -f " + filePath + " && echo '" + response + "' ";
        String result = executeSingleCommand(System.out, command);
        return result.contains(response);
    }

    public String getVariableValue(String variable) {
        String command = "echo ${" + variable + "}";
        return executeCommand(System.out, command)[0];
    }


    @Override
    public void stopTcpDump() {
        pkill("tcpdump");
    }

    @Override
    public void syncNTP() {
        String command = SshUtils.getNtpdSyncCommand();
        executeCommand(System.out, command);
    }

    @Override
    public void pkill(String processName) {
        String command = "pkill " + processName;
        executeCommand(System.out, command);
    }
}
