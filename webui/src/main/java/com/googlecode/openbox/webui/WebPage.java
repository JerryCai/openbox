package com.googlecode.openbox.webui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.googlecode.openbox.webui.obj.Attribute;
import com.googlecode.openbox.webui.obj.Tag;

public abstract class WebPage {

	private WebUI webUI;

	public WebPage(WebUI webUI) {
		this.webUI = webUI;
	}

	public WebPage(WebDriver webDriver) {
		this(WebUI.newInstance(webDriver));
	}

	/**
	 * this method is used for hook since if you click a element , you must to
	 * switch to current frame etc. so this method is hook for fulfill this
	 * requirements .
	 */
	protected abstract void comebackCurrent(WebUI webUI);

	protected WebUI getWebUI() {
		comebackCurrent(webUI);
		return webUI;
	}

	protected void clickById(String id) {
		getWebUI().clickById(id);
	}

	protected void inputById(String id, String value) {
		getWebUI().inputById(id, value);
	}

	protected void inputByTag(Tag tag, String value){
		getWebUI().inputByTag(tag, value);
	}
	
	protected void emptyById(String id) {
		getWebUI().clean(id);
	}

	protected WebElement getElementById(String id) {
		return getWebUI().getWebElementById(id);
	}

	protected  WebElement getElementByAttribute(String name, String value) {
		return getWebUI().getWebElementByAttribute(name, value);
	}

	protected  WebElement getElementByAttribute(String tagName, String name,
			String value) {
		return getWebUI().getWebElementByAttribute(tagName, name, value);
	}

	protected  WebElement getElementByAttribute(Attribute attribute) {
		return getWebUI().getWebElementByAttribute(attribute);
	}

	protected WebElement getElementByTagWithoutWait(Tag tag){
		return getWebUI().findWebElementByTagWithoutWait(tag);
	}
	
	protected  WebElement getElementByTag(Tag tag) {
		return getWebUI().getWebElementByTag(tag);
	}
	
	protected  String getAttribute(String id, String attrName) {
		return getWebUI().getAttribute(id, attrName);
	}
	
	protected void acceptComfirm(){
		webUI.acceptComfirm();
	}
	
	protected void acceptCancel(){
		webUI.acceptCancel();
	}
	
	protected void inputDialog(String msg){
		webUI.inputDialog(msg);
	}
	
	protected void waitPageLoaded(){
		getWebUI().getWait().until(WebUI.WAIT_LOADED);
	}
}
