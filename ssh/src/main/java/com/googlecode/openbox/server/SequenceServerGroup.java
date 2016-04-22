package com.googlecode.openbox.server;

import java.util.HashMap;
import java.util.Map;

public class SequenceServerGroup extends AbstractServerGroup {

    public SequenceServerGroup() {
        super();
    }

    public static SequenceServerGroup newInstance() {
        return new SequenceServerGroup();
    }


    @Override
    public <T> Map<Server, T> visit(final ServerAction<T> action) {
        precheckServerGroup();
        Server[] servers = listServers();
        Map<Server, T> responses = new HashMap<Server, T>(servers.length);
        for (Server server : servers) {
            responses.put(server, action.access(server));
        }
        return responses;
    }
}
