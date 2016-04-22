package com.googlecode.openbox.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentServerGroup extends AbstractServerGroup {
    private static final Logger logger = LogManager.getLogger();

    public ConcurrentServerGroup() {
        super();
    }

    public static ConcurrentServerGroup newInstance() {
        return new ConcurrentServerGroup();
    }

    @Override
    public <T> Map<Server, T> visit(final ServerAction<T> action) {
        precheckServerGroup();

        Server[] servers = listServers();
        int num = servers.length;
        ExecutorService executorService = Executors.newFixedThreadPool(num);
        try {
            @SuppressWarnings("unchecked")
            final Future<T>[] results = new Future[num];
            for (int i = 0; i < num; i++) {
                final Server server = servers[i];
                Callable<T> task = new Callable<T>() {
                    @Override
                    public T call() throws Exception {
                        try {
                            return action.access(server);
                        } catch (Exception e) {
                            throw new RuntimeException("action.access on server: "
                                    + server, e);
                        }
                    }
                };
                results[i] = executorService.submit(task);
            }
            Map<Server, T> responses = new HashMap<Server, T>(num);
            for (int i = 0; i < num; i++) {
                Future<T> result = results[i];
                try {
                    responses.put(servers[i], result.get());
                } catch (Exception e) {
                    throw new RuntimeException(
                            "wait and get future result failed !", e);
                }
            }

            return responses;
        } finally {
            executorService.shutdown();
        }
    }


}
