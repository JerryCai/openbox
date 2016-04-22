package com.googlecode.openbox.xtools;

/**
 * Created by jerrycai on 3/29/16.
 */
public interface TestToolManager {

    void addToolProvider(TestTool testTool);

    void start();

    void stop();

}
