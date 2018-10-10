package com.hzgc.compare.util;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static Logger LOG = Logger.getLogger(PropertiesUtil.class);

    private static InputStream loadResourceInputStream() {
        String resourceName = "master.properties";
        InputStream resourceStream = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName);
        if (resourceStream != null) {
            LOG.info("Load resource file:" + ClassLoader.getSystemResource(resourceName).getPath() + " successful!");
            return resourceStream;
        } else {
            LOG.error("Resource file:" +
                    ClassLoader.getSystemResource("") + resourceName + " is not exist!");
            System.exit(1);
        }
        return null;
    }

    public static Properties getProperties() {
        Properties ps = new Properties();
        try {
            ps.load(loadResourceInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ps;
    }
}
