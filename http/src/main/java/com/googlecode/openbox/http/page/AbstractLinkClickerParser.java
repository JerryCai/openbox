package com.googlecode.openbox.http.page;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.googlecode.openbox.http.HttpClientException;

public abstract class AbstractLinkClickerParser implements LinkClickerParser {

	public abstract String parserLink(String host, Document html);

	@Override
	public LinkClicker parserNext(LinkClicker parent) {
		parent.checkIfClicked();
		try {
			String content = parent.getResponseContent();
			Document html = Jsoup.parse(content);
			String host = getHost(parent.getLink());
			String nextLink = parserLink(host, html);
			return DefaultLinkClicker.newInstance(parent, nextLink);
		} catch (Exception e) {
			throw HttpClientException.create(
					"Parser next link jump error !!!", e);
		}

	}

	private String getHost(String url) {
		return url.substring(0, url.indexOf('/', url.indexOf("//") + 2));
	}

}