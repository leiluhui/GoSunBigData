package com.hzgc.collect.config;

import com.hzgc.collect.ActiveProfiles;
import com.hzgc.collect.service.ftp.ConnectionConfigFactory;
import com.hzgc.collect.service.ftp.FtpServer;
import com.hzgc.collect.service.ftp.FtpServerFactory;
import com.hzgc.collect.service.ftp.command.CommandFactoryFactory;
import com.hzgc.collect.service.ftp.ftplet.FtpException;
import com.hzgc.collect.service.ftp.listener.ListenerFactory;
import com.hzgc.collect.service.ftp.nativefs.filesystem.NativeFileSystemFactory;
import com.hzgc.collect.service.ftp.usermanager.PropertiesUserManagerFactory;
import com.hzgc.collect.service.ftp.util.BaseProperties;
import com.hzgc.collect.service.parser.FtpPathBootStrap;
import com.hzgc.common.collect.facedis.FtpRegisterClient;
import com.hzgc.common.collect.facedis.FtpRegisterInfo;
import com.hzgc.jniface.FaceFunction;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

@Component
@Data
@Slf4j
public class CollectContext implements Serializable {
    @Autowired
    private Environment environment;
    @Autowired
    private PlateCheck plateCheck;

    private String profiles_active;

    private Integer faceDetectorNumber;

    private Integer receiveQueueCapacity;

    private Integer receiveNumber;

    private String ftpIp;

    private Integer ftpPort;

    private String zookeeperAddress;

    private String kafkaFaceObjectTopic;

    private String kafkaPersonObjectTopic;

    private String kafkaCarObjectTopic;

    private String hostname;

    private String ftpVersion;

    private String ftpAccount;

    private String ftpPassword;

    private String seemmoUrl;

    private Boolean faceDetectorOpen;

    @Autowired
    //Spring-kafka-templage
    private KafkaTemplate<String, String> kafkaTemplate;

    private FtpRegisterClient ftpRegisterClient;

    private FtpPathBootStrap ftpPathBootStrap;

    public CollectContext() {
    }

    /*
     * 加载有顺序
     */
    public void initAll() {
        initProperties();
        if (faceDetectorOpen) {
            initDetector();
        }
        switch (profiles_active) {
            case ActiveProfiles.FTP:
                initFtpPathBoostrap();
                initFtpServer();
                log.info("Active profiles is:{}, start ftpserver", profiles_active);
                break;
            case ActiveProfiles.PROXY:
                initFtpRegisterClient();
                log.info("Active profiles is:{}, start ftp proxy server", profiles_active);
                break;
            case ActiveProfiles.LOCAL:
                if (faceDetectorOpen) {
                    initDetector();
                }
                initFtpPathBoostrap();
                initFtpRegisterClient();
                initFtpServer();
                log.info("Active profiles is:{}, start ftp proxy server", profiles_active);
                break;
            default:
                log.error("Active profile is error, start ftpserver failed");
                System.exit(0);
                break;
        }
    }

    private void initProperties() {
        profiles_active = environment.getProperty("spring.profiles.active");

        String faceDetecStr = environment.getProperty("face.detector.number");
        faceDetectorNumber = faceDetecStr != null ? Integer.parseInt(faceDetecStr) : null;

        String receiveQueue = environment.getProperty("receive.queue.capacity");
        receiveQueueCapacity = receiveQueue != null ? Integer.parseInt(receiveQueue) : null;

        String receiveStr = environment.getProperty("receive.number");
        receiveNumber = receiveStr != null ? Integer.parseInt(receiveStr) : null;

        ftpIp = environment.getProperty("ftp.ip");

        ftpPort = Integer.parseInt(environment.getProperty("ftp.port"));

        zookeeperAddress = environment.getProperty("zookeeper.address");

        kafkaFaceObjectTopic = environment.getProperty("kafka.faceobject.topic");

        kafkaPersonObjectTopic = environment.getProperty("kafka.personobject.topic");

        kafkaCarObjectTopic = environment.getProperty("kafka.carobject.topic");

        hostname = environment.getProperty("ftp.hostname");

        ftpVersion = environment.getProperty("ftp.version");

        ftpAccount = environment.getProperty("ftp.account");

        ftpPassword = environment.getProperty("ftp.password");

        seemmoUrl = environment.getProperty("seemmo.url");

        String detectorOpenStr = environment.getProperty("face.detector.enable");
        faceDetectorOpen = detectorOpenStr != null ? Boolean.parseBoolean(detectorOpenStr) : null;

    }

    private void initFtpServer() {
        FtpServerFactory ftpServerFactory = new FtpServerFactory();

        ListenerFactory listenerFactory = new ListenerFactory();

        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory(getUserMangerProperties());

        CommandFactoryFactory commandFactoryFactory = new CommandFactoryFactory();

        NativeFileSystemFactory nativeFileSystemFactory = new NativeFileSystemFactory();

        ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();

        ftpServerFactory.createCustomContext(this);

        //set the port of the listener
        listenerFactory.setPort(getFtpPort());
        // replace the default listener
        ftpServerFactory.addListener("default", listenerFactory.createListener());
        // set customer user manager
        ftpServerFactory.setUserManager(userManagerFactory.createUserManager());
        //set customer cmd factory
        ftpServerFactory.setCommandFactory(commandFactoryFactory.createCommandFactory());
        //set local file system
        ftpServerFactory.setFileSystem(nativeFileSystemFactory);
        //set connection manager
        ftpServerFactory.setConnectionConfig(connectionConfigFactory.createUDConnectionConfig());

        //create ftp server
        FtpServer server = ftpServerFactory.createServer();
        try {
            server.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }
        //print ftp log
        log.info("\n" + getLogo());
    }

    private void initFtpPathBoostrap() {
        ftpPathBootStrap = new FtpPathBootStrap(this);
    }

    private void initDetector() {
        log.info("Init face detector, number is " + faceDetectorNumber);
        if (faceDetectorNumber == 0) {
            FaceFunction.init();
        } else {
            for (int i = 0; i < faceDetectorNumber; i++) {
                FaceFunction.init();
            }
        }
    }

    private void initFtpRegisterClient() {
        ftpRegisterClient = new FtpRegisterClient(zookeeperAddress);
        ftpRegisterClient.createNode(new FtpRegisterInfo(null, null, null,
                ftpAccount, ftpPassword, ftpIp, hostname, ftpPort + "", "face,car,person"));
    }

    private BaseProperties getUserMangerProperties() {
        BaseProperties properties = new BaseProperties();
        properties.setProperty("com.hzgc.ftpserver.user.admin.userpassword",
                environment.getProperty("com.hzgc.ftpserver.user.admin.userpassword"));
        properties.setProperty("com.hzgc.ftpserver.user.admin.homedirectory",
                environment.getProperty("com.hzgc.ftpserver.user.admin.homedirectory"));
        properties.setProperty("com.hzgc.ftpserver.user.admin.enableflag",
                environment.getProperty("com.hzgc.ftpserver.user.admin.enableflag"));
        properties.setProperty("com.hzgc.ftpserver.user.admin.writepermission",
                environment.getProperty("com.hzgc.ftpserver.user.admin.writepermission"));
        properties.setProperty("com.hzgc.ftpserver.user.admin.maxloginnumber",
                environment.getProperty("com.hzgc.ftpserver.user.admin.maxloginnumber"));
        properties.setProperty("com.hzgc.ftpserver.user.admin.maxloginperip",
                environment.getProperty("com.hzgc.ftpserver.user.admin.maxloginperip"));
        properties.setProperty("com.hzgc.ftpserver.user.admin.idletime",
                environment.getProperty("com.hzgc.ftpserver.user.admin.idletime"));
        properties.setProperty("com.hzgc.ftpserver.user.admin.uploadrate",
                environment.getProperty("com.hzgc.ftpserver.user.admin.uploadrate"));
        properties.setProperty("com.hzgc.ftpserver.user.admin.downloadrate",
                environment.getProperty("com.hzgc.ftpserver.user.admin.downloadrate"));
        properties.setProperty("com.hzgc.ftpserver.user.admin.groups",
                environment.getProperty("com.hzgc.ftpserver.user.admin.groups"));
        properties.setProperty("com.hzgc.ftpserver.user.anonymous.userpassword",
                environment.getProperty("com.hzgc.ftpserver.user.anonymous.userpassword"));
        properties.setProperty("com.hzgc.ftpserver.user.anonymous.homedirectory",
                environment.getProperty("com.hzgc.ftpserver.user.anonymous.homedirectory"));
        properties.setProperty("com.hzgc.ftpserver.user.anonymous.enableflag",
                environment.getProperty("com.hzgc.ftpserver.user.anonymous.enableflag"));
        properties.setProperty("com.hzgc.ftpserver.user.anonymous.writepermission",
                environment.getProperty("com.hzgc.ftpserver.user.anonymous.writepermission"));
        properties.setProperty("com.hzgc.ftpserver.user.anonymous.maxloginnumber",
                environment.getProperty("com.hzgc.ftpserver.user.anonymous.maxloginnumber"));
        properties.setProperty("com.hzgc.ftpserver.user.anonymous.maxloginperip",
                environment.getProperty("com.hzgc.ftpserver.user.anonymous.maxloginperip"));
        properties.setProperty("com.hzgc.ftpserver.user.anonymous.idletime",
                environment.getProperty("com.hzgc.ftpserver.user.anonymous.idletime"));
        properties.setProperty("com.hzgc.ftpserver.user.anonymous.uploadrate",
                environment.getProperty("com.hzgc.ftpserver.user.anonymous.uploadrate"));
        properties.setProperty("com.hzgc.ftpserver.user.anonymous.downloadrate",
                environment.getProperty("com.hzgc.ftpserver.user.anonymous.downloadrate"));
        properties.setProperty("com.hzgc.ftpserver.user.anonymous.groups",
                environment.getProperty("com.hzgc.ftpserver.user.anonymous.groups"));
        properties.setProperty("com.hzgc.ftpserver.user.maxLoginFailures",
                environment.getProperty("com.hzgc.ftpserver.user.maxLoginFailures"));
        properties.setProperty("com.hzgc.ftpserver.user.loginFailureDelay",
                environment.getProperty("com.hzgc.ftpserver.user.loginFailureDelay"));
        properties.setProperty("com.hzgc.ftpserver.user.maxThreads",
                environment.getProperty("com.hzgc.ftpserver.user.maxThreads"));
        return properties;
    }

    public int getConnectionMaxLogins() {
        return Integer.parseInt(environment.getProperty("com.hzgc.ftpserver.user.admin.maxloginnumber"));
    }

    public boolean getConnectionAnonymousLoginEnable() {
        return Boolean.parseBoolean(environment.getProperty("com.hzgc.ftpserver.user.anonymous.enableflag"));
    }

    public int getConnectionMaxAnonymousLogins() {
        return Integer.parseInt(environment.getProperty("com.hzgc.ftpserver.user.anonymous.maxloginnumber"));
    }

    public int getConnectionMaxLoginFailures() {
        return Integer.parseInt(environment.getProperty("com.hzgc.ftpserver.user.maxLoginFailures"));
    }

    public int getConnectionLoginFailureDelay() {
        return Integer.parseInt(environment.getProperty("com.hzgc.ftpserver.user.loginFailureDelay"));
    }

    public int getConnectionMaxThread() {
        return Integer.parseInt(environment.getProperty("com.hzgc.ftpserver.user.maxThreads"));
    }

    private String getLogo() {
        return " _____  _             ____                                 \n" +
                "|  ___|| |_  _ __    / ___|   ___  _ __ __   __  ___  _ __ \n" +
                "| |_   | __|| '_ \\   \\___ \\  / _ \\| '__|\\ \\ / / / _ \\| '__|\n" +
                "|  _|  | |_ | |_) |   ___) ||  __/| |    \\ V / |  __/| |   \n" +
                "|_|     \\__|| .__/   |____/  \\___||_|     \\_/   \\___||_|       version " + ftpVersion + "\n" +
                "            |_|";
    }
}
