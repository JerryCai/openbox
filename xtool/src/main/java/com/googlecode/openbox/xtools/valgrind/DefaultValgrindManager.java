package com.googlecode.openbox.xtools.valgrind;

import com.googlecode.openbox.common.DateHelper;
import com.googlecode.openbox.common.IOUtils;
import com.googlecode.openbox.server.Server;
import com.googlecode.openbox.server.ssh.SshUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by jerrycai on 3/8/16.
 */
public class DefaultValgrindManager implements ValgrindManager {

    private Server server;
    private String reportLocation;
    private String preconditionCommands;
    private String postCommands;
    private String applicationRunCommand;
    private String reportFileName;
    private boolean isEnableXmlReport;

    public static final String REPORT_FILE_PREFIX = "/valgrind_test_result";
    public static final String REPORT_FILE_LOG = ".txt";
    public static final String REPORT_FILE_XML = ".xml";

    private DefaultValgrindManager(Server server) {
        if (null == server) {
            throw new RuntimeException("server object is null , please fix !");
        }
        this.server = server;
        this.reportLocation = "."; //default report location is .
        this.reportFileName = getReportFileName();
        this.isEnableXmlReport = false;
    }


    public static DefaultValgrindManager newInstance(Server server) {
        return new DefaultValgrindManager(server);
    }

    @Override
    public void setPostCommands(String postCommands) {
        this.postCommands = postCommands;
    }

    @Override
    public String getReport() {
        getReport(REPORT_FILE_LOG);
        return getReport(REPORT_FILE_XML);
    }

    private String getReport(String reportType) {
        String reportContent = server.getSshClient().executeSingleCommand(System.out, "cat " + getLogReportFilePath() + reportType);
        IOUtils.appendContentToFile("./target" + REPORT_FILE_PREFIX + reportType, reportContent);

        return reportContent;
    }

    @Override
    public void setReportLocation(String reportLocation) {
        this.reportLocation = reportLocation;
    }

    @Override
    public void setPreconditionCommands(String preconditionCommands) {
        this.preconditionCommands = preconditionCommands;
    }

    @Override
    public void setApplicationRunCommand(String applicationRunCommand) {
        this.applicationRunCommand = applicationRunCommand;
    }

    @Override
    public boolean isInstalled() {
        return server.getSshClient().isInstalledRpm("valgrind");
    }

    @Override
    public void install() {
        if (isInstalled()) {
            return;
        }
        executeCommands("yum install -y valgrind");

        if (!isInstalled()) {
            throw new RuntimeException("Try auto install valgrind by : yum install -y valgrind failed , Please fix or you can manual install it !");
        }
    }


    @Override
    public String start() {
        install();
        executeCommands(getValgrindCommand());
        executeCommands("echo 'valgrind successfully started now , please start your testing , once it is done , you can call stop it !'");
        return getLogReportFilePath();
    }


    @Override
    public String stop() {
        executeCommands("kill -15 " + getValgrindProcessID());
        linkToLatestReport(REPORT_FILE_LOG);
        linkToLatestReport(REPORT_FILE_XML);
        return getReport();
    }

    private void linkToLatestReport(String reportType) {
        executeCommands("ln -sf " + getLogReportFilePath() + " " + getLogReportLatestFilePath(reportType));
    }

    private String getValgrindCommand() {
        return (StringUtils.isBlank(preconditionCommands) ? "" : (preconditionCommands + " ; "))
                + "valgrind --tool=memcheck --leak-check=full --show-reachable=yes --undef-value-errors=yes --track-origins=yes --child-silent-after-fork=no --trace-children=yes --error-exitcode=1 "
                + (isEnableXmlReport ? "--xml=yes" : "") + " --xml-file="
                + getLogReportFilePath() + REPORT_FILE_XML + " --log-file=" + getLogReportFilePath() + REPORT_FILE_LOG + " "
                + applicationRunCommand + " &";
    }

    private String getLogReportFilePath() {
        return reportLocation + reportFileName;
    }

    private String getLogReportLatestFilePath(String reportType) {
        return reportLocation + REPORT_FILE_PREFIX + reportType;
    }

    private String getReportFileName() {
        return REPORT_FILE_PREFIX + "_" + DateHelper.getTimeString();
    }


    private void executeCommands(String commands) {
        if (StringUtils.isNotBlank(commands)) {
            server.getSshClient().executeCommand(System.out, commands);
        }
    }

    private String getValgrindProcessID() {
        return SshUtils.getApplicationPID(server, "valgrind");
    }

    @Override
    public void setEnableXmlReport(boolean enable) {
        this.isEnableXmlReport = enable;
    }
}
