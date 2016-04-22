package com.googlecode.openbox.xtools.valgrind.providers;


import com.googlecode.openbox.server.Server;
import com.googlecode.openbox.server.ServerGroup;
import com.googlecode.openbox.server.ssh.SshUtils;
import com.googlecode.openbox.xtools.TestTool;

/**
 * Created by jerrycai on 3/29/16.
 */
public abstract class ValgrindProvider implements TestTool {


    private ServerGroup serverGroup;

    public ValgrindProvider(ServerGroup serverGroup) {
        this.serverGroup = serverGroup;
    }

    @Override
    public ServerGroup getTargetServerGroup() {
        return serverGroup;
    }

    private void execute( ServerGroup.ServerHandler serverHandler) {
        getTargetServerGroup().visit(serverHandler);
    }

    @Override
    public void install() {

        execute(new ServerGroup.ServerHandler(){
            @Override
            public void execute(Server server) {
                SshUtils.yumInstall(server,"valgrind");
            }
        });

    }

    @Override
    public void start() {
        execute(new ServerGroup.ServerHandler(){
            @Override
            public void execute(Server server) {
                SshUtils.yumInstall(server,"valgrind");
            }
        });

    }

    @Override
    public void stop() {

    }


}
