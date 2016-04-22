package com.googlecode.openbox.xtools;

import com.googlecode.openbox.server.ServerGroup;

/**
 * Created by jerrycai on 3/29/16.
 */
public interface TestTool {

    boolean isEnable();

    String getToolName();

    ServerGroup getTargetServerGroup();

    void stopEnvironment();

    void install();

    void start();

    void stop();

    void recoverEnvironment();

    String getAppPath();

    String getStartCommands();

    String getStopCommands();

    String getRunUserAndGroup();

    String getExtraToolParams();

}
