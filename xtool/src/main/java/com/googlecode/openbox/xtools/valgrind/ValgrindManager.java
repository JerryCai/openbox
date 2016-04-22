package com.googlecode.openbox.xtools.valgrind;

/**
 * Created by jerrycai on 3/8/16.
 */
public interface ValgrindManager {

    void setReportLocation(String reportLocation);

    void setPreconditionCommands(String preconditionCommands);

    void setPostCommands(String postCommands);

    void setApplicationRunCommand(String command);

    boolean isInstalled();

    void install();

    String start();

    String stop();

    String getReport();

    void setEnableXmlReport(boolean enable);
}
