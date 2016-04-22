package com.googlecode.openbox.server;

/**
 * Created by jerrycai on 3/29/16.
 */
public final class ServerAccessor {
    private ServerVisitor<Void> visitor;

    private ServerAccessor(Server[] servers) {
        this.visitor = ServerVisitor.newIntance(servers);
    }

    public static ServerAccessor newInstance(Server[] servers) {
        return new ServerAccessor(servers);
    }

    public static abstract class AccessAction implements ServerVisitor.Action<Void> {
        public abstract void action(Server server);

        @Override
        public Void access(Server server) {
            action(server);
            return null;

        }
    }

    public void access(AccessAction action) {
        visitor.visit(action);
    }
}
