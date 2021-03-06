package com.googlecode.openbox.config;

import com.googlecode.openbox.common.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;

public final class ConfigLoader {
    private static final Logger logger = LogManager.getLogger();

    private String path = null;
    private Properties PROP = null;

    private ConfigLoader(String path) {
        this.path = path;
        this.PROP = loadConfig(this.path);
    }

    public String getConfigPath() {
        return this.path;
    }

    public static ConfigLoader newInstance() {
        return newInstance(ConfigIndex.CONFIG_PATH);
    }

    public static ConfigLoader newInstance(String path) {
        return new ConfigLoader(path);
    }

    public String getConfigItem(final String key, final String defaultValue) {
        String value = getConfigItem(key);
        if (null == value || value.trim().length() <= 0) {
            return defaultValue;
        }
        return value;
    }

    public void validateRequiredConfigItems(
            String sampleConfigOrReadMeRelativePath,
            String... requiredConfigKeys) {
        for (String requiredConfigKey : requiredConfigKeys)
            checkRequired(requiredConfigKey, sampleConfigOrReadMeRelativePath);
    }

    public void validateRequiredConfigItems(String... requiredConfigKeys) {
        validateRequiredConfigItems(null, requiredConfigKeys);
    }

    private void checkRequired(String itemName,
                               String sampleConfigOrReadMeRelativePath) {
        String value = getConfigItem(itemName);
        if (null == value || "".equals(value.trim())) {
            String msg = "check " + getConfigPath() + " item [" + itemName
                    + "] is required , please config its value";
            logger.error(msg);
            if (null != sampleConfigOrReadMeRelativePath) {
                logger.error(ContentLoader
                        .getContent(sampleConfigOrReadMeRelativePath));
            }
            throw new RuntimeException(msg);
        }
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
                    + "] is not configed or empty value in config file [" + path + "]";
            if (logger.isWarnEnabled()) {
                logger.warn(message);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("load config item [" + key + "]=[" + item + "]");
        }
        return item;
    }

    public void setConfigItem(final String key, final String value) {
        PROP.setProperty(key, value);
    }

    public void saveConfig() {
        doBak();
        String file = getClass().getClassLoader().getResource(getConfigPath())
                .getFile();
        IOUtils.outputProperties(PROP, file);
    }

    private void doBak() {
        String configAbsPath = getClass().getClassLoader()
                .getResource(getConfigPath()).getFile();
        IOUtils.copyFile(configAbsPath, configAbsPath + ".bak_ConfigLoader");
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
