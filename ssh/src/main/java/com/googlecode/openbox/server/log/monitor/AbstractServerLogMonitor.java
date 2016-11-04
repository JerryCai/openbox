package com.googlecode.openbox.server.log.monitor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.server.log.ServerLog;

public abstract class AbstractServerLogMonitor<T> implements
        ServerLogMonitor<T> {
    private static final Logger logger = LogManager.getLogger();

    private boolean enable;
    private List<ServerLogHandler<T>> handlers;
    private ConcurrentMap<String, Integer> startLineNumbers;
    private List<ServerLogProvider> serverLogProviders;
    private List<ServerLog> serverLogs;
    private int parallelCount;

    public AbstractServerLogMonitor(boolean enable) {
        this.enable = enable;
        this.serverLogs = new LinkedList<ServerLog>();
        this.serverLogProviders = new LinkedList<ServerLogProvider>();
        this.handlers = new LinkedList<ServerLogHandler<T>>();
        this.startLineNumbers = new ConcurrentHashMap<String, Integer>();
        this.parallelCount = -1;
    }

    @Override
    public ServerLogMonitor<T> addServerLogProvider(
            ServerLogProvider serverLogProvider) {
        serverLogProviders.add(serverLogProvider);
        return this;
    }

    @Override
    public ServerLogMonitor<T> addServerLog(ServerLog serverLog) {
        serverLogs.add(serverLog);
        return this;
    }

    @Override
    public ServerLogMonitor<T> addServerLogHandler(ServerLogHandler<T> checker) {
        handlers.add(checker);
        return this;
    }

    @Override
    public ServerLogMonitor<T> setParallelCount(int parallelCount) {
        this.parallelCount = parallelCount;
        return this;
    }

    @Override
    public T execute() throws Exception {
        if (this.enable) {
            return executeWithMonitorEnabled();
        }
        return triggerActions();
    }

    private T executeWithMonitorEnabled() throws Exception {
        init();
        start();
        T t = null;
        String logs = null;
        try {
            t = triggerActions();
        } finally {
            logs = getMergedTriggerDuringLogs(t);
        }
        verify(t, logs);
        return t;
    }

    private void init() {
        cleanPreviousExecutedLogs();
        collectServerLogsFromServerLogProvider();
    }

    private void cleanPreviousExecutedLogs() {
        serverLogs.clear();
    }

    private void collectServerLogsFromServerLogProvider() {
        for (ServerLogProvider serverLogProvider : serverLogProviders) {
            List<ServerLog> serverLogs = serverLogProvider.getServerLogs();
            if (null != serverLogs) {
                for (ServerLog serverLog : serverLogs) {
                    addServerLog(serverLog);
                }
            }
        }
    }

    private interface ServerLogTask<A, R> {
        A action(final ServerLog serverLog);

        R getResult(Future<A>[] serverLogTaskResults);
    }

    private <A, R> R operateLogs(final ServerLogTask<A, R> task) {
        int poolSize = getParallelCount();
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        int taskCount = serverLogs.size();
        @SuppressWarnings("unchecked")
        Future<A>[] serverLogTaskResults = new Future[taskCount];
        for (int i = 0; i < taskCount; i++) {
            final ServerLog serverLog = serverLogs.get(i);
            serverLogTaskResults[i] = executorService.submit(new Callable<A>() {
                @Override
                public A call() throws Exception {
                    return task.action(serverLog);
                }
            });
        }
        executorService.shutdown();
        return task.getResult(serverLogTaskResults);
    }

    private void start() {
        operateLogs(new ServerLogTask<Integer, int[]>() {

            @Override
            public Integer action(ServerLog serverLog) {

                try {
                    int currentLineNum = serverLog.getCurrentLineNum();
                    startLineNumbers.put(serverLog.toString(), currentLineNum);
                    return currentLineNum;
                } catch (Exception e) {
                    logger.error(e);
                    return -1;
                }
            }

            @Override
            public int[] getResult(Future<Integer>[] serverLogTaskResults) {
                int size = serverLogTaskResults.length;
                int[] results = new int[size];
                for (int i = 0; i < serverLogTaskResults.length; i++) {
                    try {
                        results[i] = serverLogTaskResults[i].get(1, TimeUnit.MINUTES);
                    } catch (Exception e) {
                        results[i] = -1;
                    }
                }
                return results;
            }

        });
    }

    private int getParallelCount() {
        int logNum = serverLogs.size();
        if (parallelCount <= 0 || parallelCount > logNum) {
            parallelCount = logNum;
        }
        return parallelCount;
    }

    private String getMergedTriggerDuringLogs(final T t) {
        return operateLogs(new ServerLogTask<String, String>() {

            @Override
            public String action(ServerLog serverLog) {
                try {
                    int startLineNum = startLineNumbers.get(serverLog.toString());
                    String[] logFilterKeys = null;
                    if (null != t) {
                        logFilterKeys = getLogFilterKeys(t);
                    }
                    String logs = null;
                    if ((null == logFilterKeys) || (logFilterKeys.length <= 0)) {
                        logs = serverLog.getLastestContent(startLineNum);
                    } else {
                        logs = serverLog.grepContentByKeysFrom(startLineNum,
                                logFilterKeys);
                    }

                    if (logger.isInfoEnabled()) {
                        logger.info("\n" + serverLog.getServer() + "\n" + logs);
                    }
                    return logs;
                } catch (Exception e) {
                    logger.error(e);
                    return "\n" + serverLog.getServer() + "\n fetch server log error as :" + e.getMessage();
                }
            }

            @Override
            public String getResult(Future<String>[] serverLogTaskResults) {
                StringBuilder logs = new StringBuilder();
                for (Future<String> serverLogTaskResult : serverLogTaskResults) {
                    try {
                        logs.append(serverLogTaskResult.get(1, TimeUnit.MINUTES)).append("\n");
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
                return logs.toString();
            }

        });
    }

    private void verify(T t, String logs) {
        for (ServerLogHandler<T> handler : handlers) {
            handler.action(t, logs);
        }
    }

}
