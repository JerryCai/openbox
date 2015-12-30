package com.googlecode.openbox.webui;

import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.googlecode.openbox.webui.obj.Attribute;
import com.googlecode.openbox.webui.obj.Tag;

/**
 * Rules: 
 * 1. use the page object based webUI test framework
 *  2. in WebPageObject,switch frame action only called by hook . 
 *  3. the webPageObject whether need to cache is determined by Web page itself whether reloaded 
 *  4. the each web page object recommend as final class for avoid further inherit .
 * 
 * @author Jerry Cai
 * 
 */
public class WebUI {
	private static final Logger logger = LogManager.getLogger();
	public static final long DEFAULT_TIMEOUT = 30;
	private WebDriver driver;
	private WebDriverWait wait;
	private long timeout;

	private final AtomicInteger _frameDepth = new AtomicInteger(0);
	private final Stack<String> _frameStack = new Stack<String>();

	public static final ExpectedCondition<Boolean> WAIT_LOADED = new ExpectedCondition<Boolean>() {
		public Boolean apply(WebDriver driver) {

			String readyState = "unknown";
			try {
				readyState = ((JavascriptExecutor) driver).executeScript("return document.readyState").toString();
			} catch (Exception e) {
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				logger.warn("This is maybe ajax call , so return true directly after sleep 2s");
				return true;
			}

			if (logger.isDebugEnabled()) {
				logger.debug(readyState);
			}
			if ("complete".equals(readyState)) {
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					logger.warn(e);
				}
				return "complete"
						.equals(((JavascriptExecutor) driver).executeScript("return document.readyState").toString());
			}
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				logger.warn(e);
			}
			return false;
		}
	};

	private WebUI(WebDriver driver, long timeout) {
		this.driver = driver;
		this.timeout = timeout;
		this.driver.manage().deleteAllCookies();
		this.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		this.wait = new WebDriverWait(driver, timeout);
	}

	public static WebUI newInstance(WebDriver driver, long timeout) {
		return new WebUI(driver, timeout);
	}

	public static WebUI newInstance(WebDriver driver) {
		return new WebUI(driver, DEFAULT_TIMEOUT);
	}

	public WebUI open(final String url) {
		return new PageLoadedAction(this) {

			@SuppressWarnings("unchecked")
			@Override
			public WebUI action(WebUI webUI) {
				driver.get(url);
				switchToTopParentFrame();
				if (logger.isInfoEnabled()) {
					logger.info("GET : " + url);
				}
				return webUI;
			}

		}.execute();

	}

	public WebElement getWebElementById(final String id) {
		try {
			return new PageLoadedAction() {

				@SuppressWarnings("unchecked")
				@Override
				public WebElement action(WebUI webUI) {
					return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
				}

			}.execute();
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("wait element to be visibility By.id(" + id + ")");
			}
		}
	}

	public WebElement getWebElementByAttribute(String name, String value) {
		return getWebElementByAttribute("*", name, value);
	}

	public WebElement getWebElementByAttribute(String tagName, String name, String value) {
		final String xPath = "//" + tagName + "[@" + name + "='" + value + "']";
		try {

			return new PageLoadedAction() {

				@SuppressWarnings("unchecked")
				@Override
				public WebElement action(WebUI webUI) {
					return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
				}

			}.execute();

		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("find element By.xpath(" + xPath + ")");
			}
		}
	}

	public WebElement getWebElementByAttribute(Attribute attribute) {
		return getWebElementByAttribute("*", attribute.getName(), attribute.getValue());
	}

	public WebElement findWebElementByTagWithoutWait(Tag tag) {
		final String xPath = tag.toXPath();
		try {
			return new PageLoadedAction() {

				@SuppressWarnings("unchecked")
				@Override
				public WebElement action(WebUI webUI) {
					return driver.findElement(By.xpath(xPath));
				}

			}.execute();

		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("find element By.xpath(" + xPath + ")");
			}
		}
	}

	public WebElement getWebElementByTag(Tag tag) {
		final String xPath = tag.toXPath();
		try {
			return new PageLoadedAction() {

				@SuppressWarnings("unchecked")
				@Override
				public WebElement action(WebUI webUI) {
					return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
				}

			}.execute();

		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("find element By.xpath(" + xPath + ")");
			}
		}
	}

	public WebUI clickById(final String id) {
		WebElement element = getWebElementById(id);
		if (logger.isInfoEnabled()) {
			logger.info("click : " + getDesc(element));
		}
		element.click();
		return this;
	}

	private String getDesc(WebElement element) {
		if (null == element) {
			return null;
		}
		return "tag=[" + element.getTagName() + "]id=[" + element.getAttribute("id") + "]text=[" + element.getText()
				+ "]";
	}

	public WebUI inputById(final String id, final String value) {
		WebElement element = getWebElementById(id);
		if (logger.isInfoEnabled()) {
			logger.info("input : " + getDesc(element) + "<==input value=[" + value + "]");
		}
		element.clear();
		element.sendKeys(value);
		return this;
	}

	public WebUI inputByTag(final Tag tag, final String value) {
		WebElement element = getWebElementByTag(tag);
		if (logger.isInfoEnabled()) {
			logger.info("input : " + getDesc(element) + "<==input value=[" + value + "]");
		}
		element.clear();
		element.sendKeys(value);
		return this;
	}

	public Alert getAlert() {
		return wait.until(ExpectedConditions.alertIsPresent());
	}

	public WebUI acceptComfirm() {
		getAlert().accept();
		return this;
	}

	public WebUI acceptCancel() {
		getAlert().dismiss();
		return this;
	}

	public WebUI inputDialog(String msg) {
		getAlert().sendKeys(msg);
		return this;
	}

	public WebUI clean(String id) {
		WebElement element = getWebElementById(id);
		if (logger.isInfoEnabled()) {
			logger.info("clean : " + getDesc(element));
		}
		element.clear();
		return this;
	}

	public String getAttribute(final String id, final String attrName) {
		return new PageLoadedAction() {

			@SuppressWarnings("unchecked")
			@Override
			public String action(WebUI webUI) {
				return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id))).getAttribute(attrName);
			}

		}.execute();

	}

	public WebUI switchToFrame(final String frameName) {
		return new PageLoadedAction(this) {

			@SuppressWarnings("unchecked")
			@Override
			public WebUI action(WebUI webUI) {
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name(frameName)));
				_frameDepth.incrementAndGet();
				_frameStack.push(frameName);
				if (logger.isDebugEnabled()) {
					logger.debug("switch to frame [" + frameName + "] .");
				}
				return webUI;
			}

		}.execute();
	}

	private String getFramePath(String... frameNames) {
		StringBuilder framesDesc = new StringBuilder();
		for (int i = 0; i < frameNames.length; i++) {
			if (i != 0) {
				framesDesc.append("->");
			}
			framesDesc.append(frameNames[i]);
		}
		return framesDesc.toString();
	}

	public String getCurrentFramePath() {
		String[] frameNames = new String[_frameStack.size()];
		for (int i = 0; i < _frameStack.size(); i++) {
			frameNames[i] = _frameStack.get(i);
		}
		return getFramePath(frameNames);
	}

	public String getCurrentFrameName() {
		return _frameStack.peek();
	}

	public WebUI switchToFrames(final String... frameNames) {
		return new PageLoadedAction(this) {

			@SuppressWarnings("unchecked")
			@Override
			public WebUI action(WebUI webUI) {

				String framePath = getFramePath(frameNames);
				for (String frameName : frameNames) {
					wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name(frameName)));
					_frameDepth.incrementAndGet();
					_frameStack.push(frameName);
				}

				if (logger.isDebugEnabled()) {
					logger.debug("switch to frame [" + framePath + "]");
				}
				return webUI;
			}

		}.execute();
	}

	public WebUI resetAndswitchToFrames(String... frameNames) {
		this.switchToTopParentFrame();
		return switchToFrames(frameNames);
	}

	public WebUI switchToTopParentFrame() {
		return new PageLoadedAction(this) {

			@SuppressWarnings("unchecked")
			@Override
			public WebUI action(WebUI webUI) {
				_frameStack.clear();
				_frameDepth.set(0);
				if (logger.isDebugEnabled()) {
					logger.debug("switch frames to top frame !");
				}
				driver.switchTo().defaultContent();
				return webUI;
			}

		}.execute();

	}

	public WebUI switchToParentFrame() {
		return new PageLoadedAction(this) {

			@SuppressWarnings("unchecked")
			@Override
			public WebUI action(WebUI webUI) {

				if (_frameDepth.get() > 0) {
					driver.switchTo().parentFrame();
					_frameDepth.decrementAndGet();
					String currentFrame = _frameStack.pop();
					if (logger.isDebugEnabled()) {
						logger.debug(
								"switch frame [" + currentFrame + "] to parent frame [" + getCurrentFramePath() + "]");
					}
				}

				return webUI;
			}

		}.execute();

	}

	public boolean clickBrowserBack() {
		return new PageLoadedAction() {

			@SuppressWarnings("unchecked")
			@Override
			public Boolean action(WebUI webUI) {
				driver.navigate().back();
				driver.switchTo().defaultContent();
				_frameStack.clear();
				_frameDepth.set(0);
				if (logger.isDebugEnabled()) {
					logger.debug("browser.navigate().back() success !");
				}
				return true;
			}

		}.execute();
	}

	public String getCurrentURL() {
		return new PageLoadedAction(this) {
			@SuppressWarnings("unchecked")
			@Override
			public String action(WebUI webUI) {
				return webUI.getDriver().getCurrentUrl();
			}
		}.execute();
	}

	public WebDriver getDriver() {
		return driver;
	}

	public WebDriverWait getWait() {
		return wait;
	}

	public void close() {
		driver.quit();
		if (logger.isInfoEnabled()) {
			logger.info("web driver close and quit !");
		}
	}

	public abstract class PageLoadedAction {
		private final WebUI webUI;

		public PageLoadedAction(WebUI webUI) {
			this.webUI = webUI;
		}

		public PageLoadedAction() {
			this.webUI = null;
		}

		public abstract <T> T action(WebUI webUI);

		public <T> T execute() {
			if (wait.until(WAIT_LOADED)) {
				return action(webUI);
			} else {
				throw WebTestException
						.create("The page can't be load complate during the timeout [" + timeout + "]");
			}

		}
	}
}
