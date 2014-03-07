package com.google.test.openbox.http.requests;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import com.google.test.openbox.http.HttpClientException;
import com.google.test.openbox.http.Request;

public abstract class FormBodyRequest extends Request {

	private List<NameValuePair> forms = null;

	public FormBodyRequest(String url) {
		super(url);
	}

	public FormBodyRequest(String scheme, String host, int port, String path) {
		super(scheme, host, port, path);
	}

	public void addForm(String name, String value) {
		if (null == forms) {
			forms = new LinkedList<NameValuePair>();
		}
		add(forms, name, value);
	}

	public void setForm(String name, String value) {
		if (null == forms) {
			forms = new LinkedList<NameValuePair>();
		}
		set(forms, name, value);
	}

	public void removeForm(String name) {
		remove(forms, name);
	}

	@Override
	public HttpEntity getEntity() {
		if (null != forms && forms.size() > 0) {
			try {
				return new UrlEncodedFormEntity(forms, CHARSET_UTF_8);
			} catch (UnsupportedEncodingException e) {
				throw new HttpClientException(
						"UnsupportedEncodingException as charset ="
								+ CHARSET_UTF_8, e);
			}
		}
		return null;
	}
}
