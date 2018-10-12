package com.hzgc.collect.service.parser.impl;

import com.hzgc.collect.config.CollectContext;
import com.hzgc.collect.service.parser.AbstractParser;
import com.hzgc.collect.service.parser.FtpPathMetaData;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class DaHuaParser_HF8600E extends AbstractParser {
    public DaHuaParser_HF8600E(CollectContext collectContext) {
        super(collectContext);
    }


    /**
     * eg: /IPC-HF8600E/2G04C2APAW00199/2018-10-08/001/jpg/18/46/34[M][0@0][0]
     */
    @Override
    public boolean canParse(String path) {
        if (path.contains("unknown") || path.contains("DVRWorkDirectory")){
            return false;
        }
        String tmpStr = path.substring(path.lastIndexOf("[") + 1, path.lastIndexOf("]"));
        return Integer.parseInt(tmpStr) == 0;
    }

    @Override
    public FtpPathMetaData parse(String path) {
        FtpPathMetaData message = new FtpPathMetaData();
        try{
            String ipcID = path.substring(path.indexOf("/", 1))
                    .substring(1, path.substring(path.indexOf("/", 1)).indexOf("/", 1));

            message.setIpcid(ipcID);
            message.setTimeStamp(dateFormat.format(new Date()));
        }catch (Exception e) {
            log.error("Parse failed, path is:?" + path);
        }
        return message;
    }

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
