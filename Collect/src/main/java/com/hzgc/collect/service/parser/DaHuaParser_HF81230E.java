package com.hzgc.collect.service.parser;

import com.hzgc.collect.config.CollectContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class DaHuaParser_HF81230E extends AbstractParser {

    DaHuaParser_HF81230E(CollectContext collectContext) {
        super(collectContext);
    }

    @Override
    public boolean canParse(String path) {
        if (path.contains("unknown") || path.contains("DVRWorkDirectory")) {
            return false;
        }
        String tmpStr = path.substring(path.lastIndexOf("[") + 1, path.lastIndexOf("]"));
        return Integer.parseInt(tmpStr) == 0;
    }

    /**
     * eg:/IPC-HF81230E/2J04439PAG00010/2018-10-08/001/jpg/19/07/39[M][0@0][0]
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
     * ftp://s100:2121/IPC-HFW5238M-AS-I1/3J07C6FPAU00272/2018-07-25/001/jpg/09/50/04[M][0@0][1].jpg
     * ------>
     * ftp://s100:2121/IPC-HFW5238M-AS-I1/3J07C6FPAU00272/2018-07-25/001/jpg/09/50/04[M][0@0][0].jpg
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
