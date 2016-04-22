package com.googlecode.openbox.xtools;

import com.googlecode.openbox.common.UtilsAPI;
import com.googlecode.openbox.server.Server;
import com.googlecode.openbox.server.ServerGroup;
import com.googlecode.openbox.server.ssh.SshUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by jerrycai on 3/29/16.
 */
public abstract class CodeCoverageTool implements TestTool {
    private static final Logger LOG = LogManager.getLogger();

    private boolean enable;

    private ServerGroup serverGroup;
    private String appPath;
    private String stopEnvironmentCommands;
    private String recoverEnvironmentCommands;
    private String extraToolParams;
    private Server[] servers;
    private String startCommands;
    private String stopCommands;
    private String userAndGroup;


    public CodeCoverageTool(ServerGroup serverGroup, String appPath, boolean enable, String stopEnvironmentCommands, String recoverEnvironmentCommands, String extraToolParams, String startCommands, String stopCommands, String userAndGroup) {
        this.serverGroup = serverGroup;
        this.servers = serverGroup.listServers();
        this.appPath = appPath;
        this.enable = enable;
        this.stopEnvironmentCommands = stopEnvironmentCommands;
        this.recoverEnvironmentCommands = recoverEnvironmentCommands;
        this.extraToolParams = extraToolParams;
        this.startCommands = startCommands;
        this.stopCommands = stopCommands;
        this.userAndGroup = userAndGroup;
    }


    public abstract void collectCoverageDataOnEachServer();

    public abstract void merageCoverageDataToFirstServer();

    public abstract void generateHtmlMergedReportOnFirstServer();

    public abstract void downloadReportToLocal();


    @Override
    public String getExtraToolParams() {
        return extraToolParams;
    }

    @Override
    public boolean isEnable() {
        return enable;
    }

    @Override
    public void stopEnvironment() {
        if (!isEnable()) {
            return;
        }
        executeCommands(stopEnvironmentCommands);

    }

    @Override
    public void recoverEnvironment() {
        if (!isEnable()) {
            return;
        }
        executeCommands(recoverEnvironmentCommands);

    }

    @Override
    public ServerGroup getTargetServerGroup() {
        return serverGroup;
    }


    @Override
    public void stop() {
        if (!isEnable()) {
            return;
        }
        collectCoverageDataOnEachServer();
        merageCoverageDataToFirstServer();
        generateHtmlMergedReportOnFirstServer();
        downloadReportToLocal();
    }


    public String getWorkLocation() {
        return UtilsAPI.getParentPath(getAppPath(), "/");
    }

    public String getHtmlReportLocaltion() {
        return getWorkLocation() + "/htmlreport_" + getToolName();
    }

    @Override
    public String getAppPath() {
        return appPath;
    }


    protected String getLocalRootLocation() {
        return getClass().getResource(File.separator).getFile();
    }

    protected Server getFirstServerAsMergedServer() {
        return getServers()[0];
    }

    public Server[] getServers() {
        return servers;
    }

    public String getReportHtmlFilePath() {
        return getReportFilePath("html");
    }

    public String getReportXmlFilePath() {
        return getReportFilePath("xml");
    }


    public String getReportFilePath(String fileType) {
        return getHtmlReportLocaltion() + "/" + getToolName() + "." + fileType;
    }


    public void executeCommands(final String commands) {
        if(StringUtils.isNotBlank(commands)) {
            getTargetServerGroup().executeCommands(commands);
        }

    }

    public void execute(ServerGroup.ServerHandler action) {
        if (!isEnable()) {
            return;
        }
        getTargetServerGroup().visit(action);
    }

    @Override
    public String getRunUserAndGroup() {
        return userAndGroup;
    }


    @Override
    public String getStartCommands() {
        return startCommands;
    }

    @Override
    public String getStopCommands() {
        return stopCommands;
    }
}


