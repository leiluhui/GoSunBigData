package com.hzgc.collect.service;

import com.hzgc.collect.config.CollectContext;
import com.hzgc.collect.service.ftp.ClusterOverFtp;
import com.hzgc.collect.service.ftp.ConnectionConfigFactory;
import com.hzgc.collect.service.ftp.FtpServer;
import com.hzgc.collect.service.ftp.FtpServerFactory;
import com.hzgc.collect.service.ftp.command.CommandFactoryFactory;
import com.hzgc.collect.service.ftp.ftplet.FtpException;
import com.hzgc.collect.service.ftp.listener.ListenerFactory;
import com.hzgc.collect.service.ftp.nativefs.filesystem.NativeFileSystemFactory;
import com.hzgc.collect.service.ftp.usermanager.PropertiesUserManagerFactory;
import com.hzgc.jniface.FaceFunction;
import com.hzgc.jniface.PictureFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.stream.FileImageInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

@Component
@Slf4j
public class FTP extends ClusterOverFtp implements Serializable {
    @Autowired
    private CollectContext collectContext;


    @Override
    public void startFtpServer() {
        collectContext.initAll();
    }
}
