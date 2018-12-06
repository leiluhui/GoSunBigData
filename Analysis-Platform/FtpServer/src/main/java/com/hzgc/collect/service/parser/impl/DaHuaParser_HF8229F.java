package com.hzgc.collect.service.parser.impl;

import com.hzgc.collect.config.CollectContext;
import com.hzgc.collect.service.parser.AbstractParser;
import com.hzgc.collect.service.parser.FtpPathMetaData;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 大华抓拍机路径解析
 * 相机配置根路径: IPC-HF8229F
 */
@Slf4j
public class DaHuaParser_HF8229F extends AbstractParser {
    public DaHuaParser_HF8229F(CollectContext collectContext) {
        super(collectContext);
    }

    @Override
    public boolean canParse(String path) {
        if (path.contains("unknown") || !path.contains(".jpg")) {
            return false;
        }
        String tmpStr = path.substring(path.lastIndexOf("[") + 1, path.lastIndexOf("]"));
        return Integer.parseInt(tmpStr) == 0;
    }

    /**
     * eg: /IPC-HF8229F/3J030FCPAK00194/3J030FCPAK00194/2018-10-10/001/jpg/15/07/47[M][0@0][0].jpg
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
     * /IPC-HF8229F/3J030FCPAK00194/3J030FCPAK00194/2018-10-10/001/jpg/15/07/47[M][0@0][1].jpg
     * ------>
     * /IPC-HF8229F/3J030FCPAK00194/3J030FCPAK00194/2018-10-10/001/jpg/15/07/47[M][0@0][0].jpg
     */
    @Override
    public String surlToBurl(String surl) {
        return surl.substring(0, surl.lastIndexOf("[") + 1)
                + 0
                + surl.substring(surl.lastIndexOf("[") + 2);
    }

    @Override
    public String ftpUrl_b2s(String burl, String type, int index) {
        return burl.substring(0, burl.lastIndexOf("[") + 1)
                + type
                + index
                + burl.substring(burl.lastIndexOf("[") + 2);
    }

    @Override
    public String path_b2s(String burl, String type, int index) {
        return burl.substring(0, burl.lastIndexOf("[") + 1)
                + type
                + index
                + burl.substring(burl.lastIndexOf("[") + 2);
    }
}
