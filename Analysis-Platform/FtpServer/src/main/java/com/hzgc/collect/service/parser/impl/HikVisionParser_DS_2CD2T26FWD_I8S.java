package com.hzgc.collect.service.parser.impl;

import com.hzgc.collect.config.CollectContext;
import com.hzgc.collect.service.ftp.impl.DefaultFtpServerContext;
import com.hzgc.collect.service.parser.AbstractParser;
import com.hzgc.collect.service.parser.FtpPathMetaData;
import com.hzgc.common.util.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 海康抓拍机路径解析
 */
@Slf4j
public class HikVisionParser_DS_2CD2T26FWD_I8S extends AbstractParser {
    public HikVisionParser_DS_2CD2T26FWD_I8S(CollectContext collectContext) {
        super(collectContext);
    }

    @Override
    public boolean canParse(String path) {
        return !path.contains("unknown") && path.contains(".jpg");
    }

    /**
     * /DS-2CD2T26FWD-I8S/DS-2CD2T26FWD-I8S20180717AACHC35801538/192.168.1.64_01_20181009163538868_FACE_DETECTION.jpg
     */
    @Override
    public FtpPathMetaData parse(String path) {
        FtpPathMetaData message = new FtpPathMetaData();
        try {
            String ipcID = path.substring(path.indexOf("/", 1))
                    .substring(1, path.substring(path.indexOf("/", 1)).indexOf("/", 1));
            message.setIpcid(ipcID);
            message.setTimeStamp(dateFormat.format(new Date()));
        } catch (Exception e) {
            log.error("Parse failed, path is:?" + path);
        }
        return message;
    }

    /**
     * eg:
     * ftp://s100:2121/DS-2CD2T26FWD-I8S/DS-2CD2T26FWD-I8S20180717AACHC35801538/192.168.1.64_01_20181009163538868_FACE_DETECTION_face1.jpg
     * ------>
     * ftp://s100:2121/DS-2CD2T26FWD-I8S/DS-2CD2T26FWD-I8S20180717AACHC35801538/192.168.1.64_01_20181009163538868_FACE_DETECTION.jpg
     */
    @Override
    public String surlToBurl(String surl) {
        return surl.substring(0, surl.lastIndexOf("_")) + ".jpg";
    }

    @Override
    public String ftpUrl_b2s(String burl, String type, int index) {
        return burl.replace(".jpg", "_" +type + index + ".jpg");
    }

    @Override
    public String path_b2s(String burl, String type, int index) {
        return burl.replace(".jpg", "_" + type + index + ".jpg");
    }
}

