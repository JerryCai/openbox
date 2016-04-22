package com.googlecode.openbox.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jerrycai on 3/29/16.
 */
public class ServerVisitor<T> {

    private Server[] servers;

    private ServerVisitor(Server[] servers) {
        this.servers = servers;
    }

    public static ServerVisitor newIntance(Server[] servers) {
        return new ServerVisitor(servers);
    }

    public interface Action<T> {
        T access(Server server);
    }

    public Map<Server, T> visit(Action<T> action) {
        Map<Server, T> results = new HashMap<Server,T>();
        for (Server server : servers) {
            results.put(server, action.access(server));
        }
        return results;
    }
}
