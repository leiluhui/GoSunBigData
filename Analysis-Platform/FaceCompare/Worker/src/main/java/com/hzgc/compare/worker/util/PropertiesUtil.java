package com.hzgc.compare.worker.util;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;
public class PropertiesUtil {
    private static Logger log = Logger.getLogger(PropertiesUtil.class);
    //private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtil.class);

    private static InputStream loadResourceInputStream(String resourceName) {
        if (resourceName != null && resourceName.length() > 0) {
            InputStream resourceStream = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName);
            if (resourceStream != null) {
                log.info("Load resource file:" + ClassLoader.getSystemResource(resourceName).getPath() + " successful!");
                return resourceStream;
            } else {
                log.error("Resource file:" +
                        ClassLoader.getSystemResource("") + resourceName + " is not exist!");
                System.exit(1);
            }
        } else {
            log.error("The file name is not vaild!");
        }
        return null;
    }

    public static Properties getProperties() {
        Properties ps = new Properties();
        try {
            ps.load(loadResourceInputStream("worker.properties"));
//            ps.load(loadResourceInputStream("hbase-site.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ps;
    }
}
