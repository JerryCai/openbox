package com.googlecode.openbox.xtools;


import java.util.List;
import java.util.concurrent.*;

/**
 * Created by jerrycai on 3/31/16.
 */
public class DefaultTestToolManager extends AbstractTestToolManager {


    private DefaultTestToolManager() {
        super();
    }

    public static DefaultTestToolManager newInstance() {
        return new DefaultTestToolManager();
    }

    @Override
    protected void visitTools(List<TestTool> tools, final TestToolVisitor visitor) {
        ExecutorService executorService = Executors.newFixedThreadPool(tools.size());
        Future<Object>[] results = new Future[tools.size()];
        try {
            for (int i = 0 ; i< tools.size() ; i++) {
                final TestTool tool = tools.get(i);
                results[i] = executorService.submit(new Callable<Object>() {

                    @Override
                    public Object call() throws Exception {
                        visitor.action(tool);
                        return null;
                    }
                });
            }

            for(int j = 0 ; j< results.length; j++) {
                try {
                    results[j].get(10, TimeUnit.MINUTES);
                } catch (Exception e) {
                    throw new RuntimeException(" execute tool command error",e);
                }
            }
        } finally {
            executorService.shutdown();
        }
    }

}
