package com.googlecode.openbox.log;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.testng.Reporter;

@Plugin(name = "TestngReportAppender", category = "Core", elementType = "appender", printObject = true)
public final class TestngReportAppender extends AbstractAppender {
	private boolean justContent;

	private TestngReportAppender(String name, Filter filter,
			Layout<? extends Serializable> layout, String justContent) {
		super(name, filter, layout);
		if (null == justContent) {
			this.justContent = false;
		} else {
			this.justContent = Boolean.parseBoolean(justContent);
		}
	}

	@PluginFactory
	public static TestngReportAppender createAppender(
			@PluginElement("Layout") Layout<? extends Serializable> layout,
			@PluginElement("Filters") final Filter filter,
			@PluginAttribute("name") final String name,
			@PluginAttribute("justContent") final String justContent) {
		if (name == null) {
			LOGGER.error("No name provided for TestngReportAppender");
			return null;
		}
		if (layout == null) {
			layout = PatternLayout.createLayout(null, null, null, null, null);
		}

		return new TestngReportAppender(name, filter, layout, justContent);
	}

	@Override
	public void append(LogEvent event) {
		String logContent = null;
		if (justContent) {
			logContent = event.getMessage().getFormattedMessage();
		} else {
			byte[] bytes = getLayout().toByteArray(event);
			try {
				logContent = new String(bytes, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		Reporter.log(logContent);
	}

	private static void install() {
		install(TestngReportAppender.class);
	}

	public static boolean install(Class<?> loadClass) {
		String startPackage = TestngReportAppender.class.getPackage().getName();
		String output = loadClass.getClassLoader().getResource("//").getPath();

		try {
			PluginManager.main(new String[] { output, startPackage });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	public static void main(String... args) {
		install();
		Logger logger = LogManager.getLogger();
		logger.info("\n!!!!test install success !!!!\n");
	}
}
