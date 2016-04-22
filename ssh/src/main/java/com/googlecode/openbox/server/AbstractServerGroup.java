package com.googlecode.openbox.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractServerGroup implements ServerGroup {

    private Map<String, Server> serverGroup;

    public AbstractServerGroup() {
        this.serverGroup = new HashMap<String, Server>();
    }

    @Override
    public void addServer(Server server) {
        serverGroup.put(server.getIp(), server);
    }


    @Override
    public void addServers(ServerGroup serverGroup) {
        Server[] servers = serverGroup.listServers();
        for (Server server : servers) {
            addServer(server);
        }
    }

    @Override
    public int getNum() {
        return serverGroup.size();
    }

    @Override
    public boolean isEmpty() {
        return serverGroup.isEmpty();
    }

    @Override
    public Server getServer(String host) {
        return serverGroup.get(host);
    }

    @Override
    public void removeServer(String host) {
        serverGroup.remove(host);
    }

    @Override
    public void clearAll() {
        serverGroup.clear();
    }

    @Override
    public Server[] listServers() {
        return serverGroup.values().toArray(new Server[0]);
    }

    @Override
    public String[] listServerHosts() {
        Server[] servers = listServers();
        int num = servers.length;
        String[] serverHosts = new String[num];
        for (int i = 0; i < num; i++) {
            serverHosts[i] = servers[i].getIp();
        }
        return serverHosts;
    }

    @Override
    public Map<Server, String> executeShell(final String shell) {

        return visit(new ServerAction<String>() {
            @Override
            public String access(Server server) {
                return server.getSshClient().executeShell(System.out, shell);
            }
        });
    }

    @Override
    public Map<Server, String[]> executeCommands(final String commands) {
        return visit(new ServerAction<String[]>() {
            @Override
            public String[] access(Server server) {
                return server.getSshClient().executeCommand(System.out, commands);
            }
        });
    }

    @Override
    public Map<Server, String> executeSingleCommandGetResponse(final String command) {
        return visit(new ServerAction<String>() {
            @Override
            public String access(Server server) {
                return server.getSshClient().executeSingleCommand(System.out, command);
            }
        });
    }

    @Override
    public void visit(final ServerHandler serverHandler) {
        visit(new ServerAction<Void>() {

            @Override
            public Void access(Server server) {
                serverHandler.execute(server);
                return null;
            }
        });
    }

    protected Collection<Server> getAllServers() {
        return serverGroup.values();
    }

    protected void precheckServerGroup() {
        if (getAllServers().size() <= 0) {
            throw new RuntimeException(
                    "execute shell failed as server group is empty , can't found any server in it");
        }

    }
}
