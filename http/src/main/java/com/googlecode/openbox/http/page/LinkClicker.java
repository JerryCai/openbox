package com.googlecode.openbox.http.page;

import org.apache.http.impl.client.CloseableHttpClient;

public interface LinkClicker {

	LinkClicker click();

	String download(String localFolder);

	LinkClicker getNextLinkClicker(LinkClickerParser parser);

	void checkIfClicked();

	CloseableHttpClient getHttpClient();

	LinkClicker getParent();

	LinkClicker getRootParent();

	String getResponseContent();

	String getLink();

	boolean isClicked();

	boolean isGotoRetireTipPage(String tipMsg);

}