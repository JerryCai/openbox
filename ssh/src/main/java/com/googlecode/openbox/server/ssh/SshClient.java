package com.googlecode.openbox.server.ssh;

import com.googlecode.openbox.server.Server;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public interface SshClient {

    String executeSingleCommand(OutputStream output, String command);

    String[] executeCommand(OutputStream output, String command);

    String executeShell(OutputStream output, String shell);

    String executeShellByFTP(OutputStream output, String shell);

    void uploadFile(OutputStream output, String fileName, String fileContent,
                    String serverLocation);

    void uploadFile(OutputStream output, String fileName,
                    InputStream fileContent, String serverLocation);

    void uploadFile(OutputStream output, String fileName, File file,
                    String serverLocation);

    void downloadFile(OutputStream output, String remoteFile, String localFolder, String fileName);

    void downloadFile(OutputStream output, String remoteFile, String localFolder);

    void chmod(OutputStream output, int mode, String path);

    void chown(OutputStream output, String own, String path);

    void mv(OutputStream output, String source, String dest);

    void rm_Rf(OutputStream output, String path);

    boolean testConnection(OutputStream output);

    void uploadResourceLevelFileToServerLocation(String resourcePath, String remoteFolder);

    void uploadResourceLevelFileToServer(String resourcePath, String remoteFilePath);

    void uploadFileToServer(String localFilePath, String remoteFilePath);

    void downloadFileFromServer(String remoteFilePath, String localFilePath);

    void downloadFolderFromServer(String remoteLocation, String localLocation);

    void downloadToRelativeFileFromServerFile(String remoteFilePath, String relativeLocalFilePath);

    void downloadToRelativeLocationFromServerFile(String remoteFilePath, String relativeLocalLocation);

    void downloadToRelativeLocationFromServerFolder(String remoteLocation, String relativeLocalLocation);

    void enableServerSFTP();

    String getApplicationPIDByProcess(String processName);

    String getApplicationPID(String processName);

    boolean isInstalledRpm(String rpmKey);

    String getRpmBuildNumber(String rpmKey);

    String getRpmBuildVersion(String rpmKey);

    void yumInstall(String rpmKey);

    boolean isFileExisted(String filePath);

    String getVariableValue(String variable);

    void startTcpDumpCommand(int port, String dumpPath);

    void startFullTcpDumpCommand(String dumpPath);

    void stopTcpDump();

    void syncNTP();

    void pkill(String processName);

}