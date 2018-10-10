package com.hzgc.collect.service.parser.impl;

import com.hzgc.collect.config.CollectContext;
import com.hzgc.collect.service.ftp.impl.DefaultFtpServerContext;
import com.hzgc.collect.service.parser.AbstractParser;
import com.hzgc.collect.service.parser.FtpPathMetaData;
import com.hzgc.common.util.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 大华抓拍机路径解析
 * 相机配置根路径: ITC302_RF2D
 */
@Slf4j
public class DaHuaParser_ITC302_RF2D extends AbstractParser {
    public DaHuaParser_ITC302_RF2D(CollectContext collectContext) {
        super(collectContext);
    }

    @Override
    public boolean canParse(String path) {
        if (path.contains("unknown") || !path.contains(".jpg")) {
            return false;
        }
        String tmpStr = path.substring(path.lastIndexOf("_") + 1, path.lastIndexOf("."));
        return Integer.parseInt(tmpStr) == 0;
    }
    /**
     * eg: /ITC302-RF2D/4C0282CPAJ7740E/2018/10/09/18/2018_10_09_18_53_51_0.jpg
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
     * ftp://s100:2121/ITC302-RF2D/4C0282CPAJ7740E/2018/10/09/18/2018_10_09_18_53_51_1.jpg
     * ------>
     * ftp://s100:2121/ITC302-RF2D/4C0282CPAJ7740E/2018/10/09/18/2018_10_09_18_53_51_0.jpg
     */
    @Override
    public String surlToBurl(String surl) {
        return surl.substring(0, surl.lastIndexOf("_") + 1)
                + 0
                + surl.substring(surl.lastIndexOf("_") + 2);
    }

    @Override
    public String ftpUrl_b2s(String burl, String type, int index) {
        return burl.substring(0, burl.lastIndexOf("_") + 1)
                + type
                + index
                + burl.substring(burl.lastIndexOf("_") + 2);
    }

    @Override
    public String path_b2s(String burl, String type, int index) {
        return burl.substring(0, burl.lastIndexOf("_") + 1)
                + type
                + index
                + burl.substring(burl.lastIndexOf("_") + 2);
    }
}
