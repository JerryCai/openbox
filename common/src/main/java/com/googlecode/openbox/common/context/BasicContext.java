package com.googlecode.openbox.common.context;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.openbox.common.ArgsUtils;

public class BasicContext implements CommonContext {

	private final BasicContext parentContext;
	private Map<String, Object> map = null;

	public BasicContext() {
		this(null);
	}

	public BasicContext(final BasicContext parentContext) {
		super();
		this.parentContext = parentContext;
	}

	public Object getAttribute(final String id) {
		ArgsUtils.notNull(id, "Id");
		Object obj = null;
		if (this.map != null) {
			obj = this.map.get(id);
		}
		if (obj == null && this.parentContext != null) {
			obj = this.parentContext.getAttribute(id);
		}
		return obj;
	}

	public void setAttribute(final String id, final Object obj) {
		ArgsUtils.notNull(id, "Id");
		if (this.map == null) {
			this.map = new HashMap<String, Object>();
		}
		this.map.put(id, obj);
	}

	public Object removeAttribute(final String id) {
		ArgsUtils.notNull(id, "Id");
		if (this.map != null) {
			return this.map.remove(id);
		} else {
			return null;
		}
	}

	public void clear() {
		if (this.map != null) {
			this.map.clear();
		}
	}

	@Override
	public String toString() {
		if (this.map != null) {
			return this.map.toString();
		} else {
			return "{}";
		}
	}

}
