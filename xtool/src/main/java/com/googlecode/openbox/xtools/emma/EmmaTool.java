package com.googlecode.openbox.xtools.emma;

import com.googlecode.openbox.common.UtilsAPI;
import com.googlecode.openbox.server.Server;
import com.googlecode.openbox.server.ServerGroup;
import com.googlecode.openbox.server.ssh.SshUtils;
import com.googlecode.openbox.xtools.CodeCoverageTool;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by jerrycai on 3/29/16.
 */
public class EmmaTool extends CodeCoverageTool {

    private static final Logger logger = LogManager.getLogger();

    private String emmaFilterParams;
    private String extraNeedMergeFileList;

    public interface Config {

        boolean isEnable();

        ServerGroup serverGroup();

        String appPath();

        String getExtraNeedMergeFileList();

        String emmaFilterParams();

        String getRunUserAndGroup();

        String getStartCommands();

        String getStopCommands();

        String getStopEnvironmentCommands();

        String getRecoverEnvironmentCommands();

        String getExtraEmmaParams();
    }

    private static EmmaTool newInstance(ServerGroup serverGroup, String appPath, boolean enable, String stopEnvironmentCommands, String recoverEnvironmentCommands, String startCommands, String stopCommands, String userAndGroup, String emmFilterParams, String extraEmmaParams, String extraNeedMergeFileList) {
        return new EmmaTool(serverGroup, appPath, enable, stopEnvironmentCommands, recoverEnvironmentCommands, startCommands, stopCommands, userAndGroup, emmFilterParams, extraEmmaParams, extraNeedMergeFileList);
    }

    public static EmmaTool newInstance(Config config) {
        return newInstance(config.serverGroup(), config.appPath(), config.isEnable(), config.getStopEnvironmentCommands(), config.getRecoverEnvironmentCommands(), config.getStartCommands(), config.getStopCommands(), config.getRunUserAndGroup(), config.emmaFilterParams(), config.getExtraEmmaParams(), config.getExtraNeedMergeFileList());
    }

    private EmmaTool(ServerGroup serverGroup, String appPath, boolean enable, String stopEnvironmentCommands, String recoverEnvironmentCommands, String startCommands, String stopCommands, String userAndGroup, String emmFilterParams, String extraEmmaParams, String extraNeedMergeFileList) {
        super(serverGroup, appPath, enable, stopEnvironmentCommands, recoverEnvironmentCommands, extraEmmaParams, startCommands, stopCommands, userAndGroup);
        this.emmaFilterParams = emmFilterParams;
        this.extraNeedMergeFileList = extraNeedMergeFileList;
    }

    @Override
    public String getToolName() {
        return "codecoverage_emma";
    }


    @Override
    public void install() {
        if (!isEnable()) {
            return;
        }
        execute(new ServerGroup.ServerHandler() {
            @Override
            public void execute(Server server) {
                //TODO need to change auto get after ssh2 support
                String javaHome = "JAVA_HOME";
                String javaHomeLocation = server.getSshClient().getVariableValue(javaHome);
                String remoteEmmaFile = javaHomeLocation + "/jre/lib/ext/emma.jar";
                boolean isEmmaInstalled = SshUtils.isFileExisted(server, remoteEmmaFile);
                if (isEmmaInstalled) {
                    logger.info("emma has been installed already , skip to install again!");
                    return;
                }
                SshUtils.uploadResourceLevelFileToServer(server, "emma/emma.jar", remoteEmmaFile);
                server.getSshClient().executeCommand(System.out, "chmod 777 " + remoteEmmaFile);
            }
        });

    }

    @Override
    public void start() {
        if (!isEnable()) {
            return;
        }
        String emFile = getEmmaEmFile();
//        final String rmOlder = "rm -rf " + emFile + " && ";
        final String rmOlder = "";
        String emmaInjectCommand =
                rmOlder + "java emma instr -m overwrite -cp " + getAppPath() + " -ix " + emmaFilterParams + " -Dmetadata.out.file=" + emFile;
        String command = emmaInjectCommand + getChownAndChmodCommands(getAppPath()) + getChownAndChmodCommands(emFile) + " && " + getStartCommands();
        executeCommands(command);
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getChownAndChmodCommands(String path) {
        return " && chown " + getRunUserAndGroup() + " " + path + " && chmod 777 " + path;
    }

    public void collectCoverageDataOnEachServer() {
        String ecFile = getEmmaECFile();
        String command = "rm -rf " + ecFile + " && java emma ctl -connect localhost:47653 -command coverage.get," + ecFile + " && " + getStopCommands();
        executeCommands(command);
    }


    @Override
    public void merageCoverageDataToFirstServer() {
        if (!isEnable()) {
            return;
        }

        final Server firstServer = getFirstServerAsMergedServer();
        //remove older merge file
        String localMergePath = getWorkLocation() + "/" + getDownloadEachEcFileName(firstServer);
        String mergedPath = getEmmaECFile();

        String commands = "rm -rf " + localMergePath + " && " + "mv " + mergedPath + " " + localMergePath;
        firstServer.getSshClient().executeCommand(System.out, commands);

        String mergeFileList = localMergePath;
        Server[] servers = getServers();
        for (int i = 1; i < servers.length; i++) {

            Server server = servers[i];
            String downloadLocation = getLocalRootLocation();
            String downloadFileName = getDownloadEachEcFileName(server);
            final String localFilePath = downloadLocation + File.separator + downloadFileName;
            //download to local
            SshUtils.downloadFileToServer(server, getEmmaECFile(), localFilePath);

            //remove remote older data
            final String mergeFilePath = getWorkLocation() + "/" + downloadFileName;
            String rmOlderCommand = "rm -rf " + mergeFilePath;
            firstServer.getSshClient().executeCommand(System.out, rmOlderCommand);
            //upload the download to first for merge
            SshUtils.uploadFileToServer(firstServer, localFilePath, mergeFilePath);
            //add to merge list for merge finally
            mergeFileList = mergeFileList + "," + mergeFilePath;
        }

        if (StringUtils.isNotBlank(extraNeedMergeFileList)) {
            mergeFileList = mergeFileList + "," + extraNeedMergeFileList;
        }

        mergeFileList = skipNonExistedFiles(firstServer,mergeFileList);
        String ecFile = getMergedEmmaEcFilePath();
        String command = "rm -rf " + getHtmlReportLocaltion() + " && mkdir " + getHtmlReportLocaltion() + " && " + "java emma merge -input " + mergeFileList + " -out " + ecFile;
        firstServer.getSshClient().executeCommand(System.out, command);
    }

    private String skipNonExistedFiles(Server firstServer, String fileList) {
        if (logger.isInfoEnabled()) {
            logger.info("wait to merge emma coverage file list [" + fileList + "]");
        }
        String[] files = fileList.split(",");
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        for (String file : files) {
            try {
                if (SshUtils.isFileExisted(firstServer, file)) {
                    if (!isFirst) {
                        result.append(",");
                    }
                    result.append(file);
                    isFirst = false;
                } else {
                    logger.error("ignore non-existed emma coverage file or it is folder path as [" + file + "] on merge server [" + firstServer + "]");
                }
            } catch (Exception e) {
                logger.error("emma data file path [" + file + "] is wrong!", e);
            }
        }
        return result.toString();
    }

    @Override
    public void downloadReportToLocal() {
        final Server firstServer = getFirstServerAsMergedServer();
        String reportFolder = UtilsAPI.getParentPath(getReportHtmlFilePath(), "/");
        SshUtils.downloadFolderFromServer(firstServer, reportFolder, getLocalRootLocation());
    }

    private String getSourceLocation() {
        return getHtmlReportLocaltion() + "/sources";
    }

    private String getUnzipSourceCommand() {
        return "unzip -o '" + getWorkLocation() + "/*-sources.jar' -d " + getSourceLocation();
    }

    @Override
    public void generateHtmlMergedReportOnFirstServer() {
        if (!isEnable()) {
            return;
        }
        final Server firstServer = getFirstServerAsMergedServer();
        String command = getUnzipSourceCommand() + " && " + getEmmaReportCommand();
        firstServer.getSshClient().executeCommand(System.out, command);
    }

    private String getEmmaReportCommand() {
        return getEmmaReportCommandBy("html") + " && " + getEmmaReportCommandBy("xml") + " && " + getEmmaReportCommandBy("txt");
    }


    private String getEmmaReportCommandBy(String fileType) {
        String extraEmmaParas = "";
        if (StringUtils.isNotBlank(getExtraToolParams())) {
            extraEmmaParas = getExtraToolParams();
        }
        return "java emma report -r " + fileType + " -sp " + getSourceLocation() + " -in " + getEmmaEmFile() + "," + getMergedEmmaEcFilePath() + " " + extraEmmaParas + " -Dreport.out.encoding=UTF-8 -Dreport." + fileType + ".out.file=" + getReportFilePath(fileType);
    }


    private String getMergedEmmaEcFilePath() {
        return getHtmlReportLocaltion() + "/" + getToolName() + ".ec";
    }


    private String getDownloadEachEcFileName(Server server) {
        return getToolName() + "_" + server.getIp() + "_.ec";
    }

    private String getEmmaEmFile() {
        return getWorkLocation() + "/" + getToolName() + ".em";
    }

    private String getEmmaECFile() {
        return getWorkLocation() + "/" + getToolName() + ".ec";
    }


}
