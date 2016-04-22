package com.googlecode.openbox.xtools;


import java.util.List;

/**
 * Created by jerrycai on 3/31/16.
 */
public class OrderedTestToolManager extends AbstractTestToolManager {


    private OrderedTestToolManager() {
        super();
    }

    public static OrderedTestToolManager newInstance() {
        return new OrderedTestToolManager();
    }

    @Override
    protected void visitTools(List<TestTool> tools, final TestToolVisitor visitor) {
        for (final TestTool tool : tools) {
            visitor.action(tool);
        }
    }

}
