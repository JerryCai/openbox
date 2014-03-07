package com.google.test.openbox.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.test.openbox.common.IOUtils;

public class ConfigLoader {
	private static final Logger logger = LogManager.getLogger();

	private String path = null;
	private Properties PROP = null;

	private ConfigLoader(String path) {
		this.path = path;
		this.PROP = loadConfig(this.path);
		doBak();

	}

	private void doBak() {
		String configAbsPath = getClass().getClassLoader()
				.getResource(getConfigPath()).getFile();
		IOUtils.copyFile(configAbsPath, configAbsPath + ".bak_ConfigLoader");
	}

	public String getConfigPath() {
		return this.path;
	}

	public static ConfigLoader newInstance(String path) {
		return new ConfigLoader(path);
	}

	public String getConfigItem(final String key) {
		String item = null;
		try {
			item = PROP.getProperty(key);
		} catch (MissingResourceException e) {
			String message = "getConfigItem() by key =[" + key
					+ "] error as MissingResourceException ";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}

		if (null == item || "".equals(item.trim())) {
			String message = "config item [" + key
					+ "] is not configed in config file [" + path + "]";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("load config item [" + key + "]=[" + item + "]");
		}
		return item;
	}

	public void writeConfigItem(final String key, final String value) {
		PROP.setProperty(key, value);
		String file = getClass().getClassLoader().getResource(getConfigPath())
				.getFile();
		IOUtils.outputProperties(PROP, file);
	}

	private Properties loadConfig(String path) {
		InputStream is = null;
		try {
			Properties PROP = new Properties();
			is = IOUtils.getInputStreamByProjectRelativePath(path);
			if (null != is) {
				PROP.load(is);
				return PROP;
			} else {
				String msg = "load config file =[" + path + "]failed!";
				logger.error(msg);
				throw new RuntimeException(msg);
			}
		} catch (IOException e) {
			String msg = "load config file =[" + path + "]failed!";
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("close config input stream failed !", e);
				}
			}
		}
	}
}
