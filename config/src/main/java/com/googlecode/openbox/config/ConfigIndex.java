package com.googlecode.openbox.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigIndex {

	private static final Logger logger = LogManager.getLogger();
	private static final String INDEX = "config";
	private static String CONFIG_LOCATION = null;

	public static final String CONFIG_PATH = getConfigPath("config.properties");

	public static String getConfigPath(String configFileName) {
		if (null == CONFIG_LOCATION) {
			String configPath = System.getProperty(INDEX);
			if (logger.isInfoEnabled()) {
				logger.info("ConfigIndex Read : System.getProperty(" + INDEX
						+ ") = [" + configPath + "]");
			}
			if (StringUtils.isBlank(configPath)) {
				String index = INDEX + "/index.properties";
				logger.warn("use default index configured in : [" + index + "]");
				CONFIG_LOCATION = INDEX
						+ "/"
						+ ConfigLoader.newInstance(INDEX + "/index.properties")
								.getConfigItem(INDEX) + "/";
			} else {
				CONFIG_LOCATION = INDEX + "/" + configPath + "/";
			}
		}
		return CONFIG_LOCATION + configFileName;

	}
}
