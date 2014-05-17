package com.googlecode.openbox.common.context;

public interface CommonContext {
    public static final String RESERVED_PREFIX  = "cc.";

    Object getAttribute(String id);

    void setAttribute(String id, Object obj);

    Object removeAttribute(String id);
}
