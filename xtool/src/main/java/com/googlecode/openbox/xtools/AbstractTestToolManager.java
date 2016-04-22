package com.googlecode.openbox.xtools;


import com.googlecode.openbox.xtools.TestTool;
import com.googlecode.openbox.xtools.TestToolManager;

import java.util.*;

/**
 * Created by jerrycai on 3/31/16.
 */
public abstract class AbstractTestToolManager implements TestToolManager {

    private List<TestTool> tools;

    public AbstractTestToolManager() {
        this.tools = new ArrayList<TestTool>();
    }


    @Override

    public void addToolProvider(TestTool testTool) {
        if (!tools.contains(testTool)) {
            tools.add(testTool);
        }
    }


    @Override
    public void start() {

        visitTools(new TestToolVisitor() {
            @Override
            public void action(TestTool testTool) {
                testTool.stopEnvironment();
                testTool.install();
                testTool.start();
            }
        });

    }

    @Override
    public void stop() {

        visitTools(new TestToolVisitor() {
            @Override
            public void action(TestTool testTool) {
                testTool.stop();
                testTool.recoverEnvironment();
            }
        });
    }

    interface TestToolVisitor {
        void action(TestTool testTool);
    }

    private void visitTools(final TestToolVisitor visitor){
        visitTools(tools,visitor);
    }

    protected abstract void visitTools(List<TestTool> tools, final TestToolVisitor visitor);

}
