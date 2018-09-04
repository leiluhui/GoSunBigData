package com.hzgc.collect.expand.parser;

import com.hzgc.collect.expand.util.CollectProperties;

public class FtpPathParse {
    private static Parser boxParser = new BoxParser();
    private static Parser daHuaParser_zhuaPaiJi = new DaHuaParser_ZhuaPaiJi();
    private static Parser daHuaParser_gongChengJi = new DaHuaParser_GongChengJi();

    public static boolean isParse(String fileName) {
        if (fileName.contains(DeviceUtil.DaHua_ZhuaPaiJi)) {
            return daHuaParser_zhuaPaiJi.canParse(fileName);
        } else if (fileName.contains(DeviceUtil.DaHua_GongChengJi)) {
            return daHuaParser_gongChengJi.canParse(fileName);
        } else {
            return boxParser.canParse(fileName);
        }
    }

    public static FtpPathMetaData parse(String fileName) {
        if (fileName.contains(DeviceUtil.DaHua_ZhuaPaiJi)) {
            return daHuaParser_zhuaPaiJi.parse(fileName);
        } else if (fileName.contains(DeviceUtil.DaHua_GongChengJi)) {
            return daHuaParser_gongChengJi.parse(fileName);
        } else {
            return boxParser.parse(fileName);
        }
    }

    /**
     * 通过上传文件路径解析到ftpUrl
     *
     * @param filePath ftp接收数据路径
     * @return 带IP的ftpUrl
     */
    public static String getFtpUrl_ip(String filePath) {
        return "ftp://" + CollectProperties.getFtpIp() + ":" + CollectProperties.getFtpPort() + filePath;
    }

    /**
     * 通过上传文件路径解析到ftpUrl（ftp发送至kafka的key）
     *
     * @param filePath ftp接收数据路径
     * @return 带hostname的ftpUrl
     */
    public static String getFtpUrl_hostname(String filePath) {
        return "ftp://" + CollectProperties.getHostname() + ":" + CollectProperties.getFtpPort() + filePath;
    }

    /**
     * 小图ftpUrl转大图ftpUrl
     *
     * @param surl 小图ftpUrl
     * @return 大图ftpUrl
     */
    public static String surlToBurl(String surl) {
        if (surl.contains(DeviceUtil.DaHua_ZhuaPaiJi)) {
            return daHuaParser_zhuaPaiJi.surlToBurl(surl);
        } else if (surl.contains(DeviceUtil.DaHua_GongChengJi)) {
            return daHuaParser_gongChengJi.surlToBurl(surl);
        } else {
            return boxParser.surlToBurl(surl);
        }
    }

    /**
     * 大图ftpUrl转小图ftpUrl(带ip,hostname的ftpUrl都可以转)
     *
     * @param burl  大图ftpUrl
     * @param index 小图下标
     * @return 小图ftpUrl
     */
    public static String ftpUrl_b2s(String burl, String type, int index) {
        if (burl.contains(DeviceUtil.DaHua_ZhuaPaiJi)) {
            return daHuaParser_zhuaPaiJi.ftpUrl_b2s(burl, type, index);
        } else if (burl.contains(DeviceUtil.DaHua_GongChengJi)) {
            return daHuaParser_gongChengJi.ftpUrl_b2s(burl, type, index);
        } else {
            return boxParser.ftpUrl_b2s(burl, type, index);
        }
    }

    /**
     * 大图路径转小图路径(绝对路径和ftp相对路径都可以转)
     *
     * @param bigRelativePath  大图路径
     * @param index 小图下标
     * @return 小图ftpUrl
     */
    public static String path_b2s(String bigRelativePath, String type, int index) {
        if (bigRelativePath.contains(DeviceUtil.DaHua_ZhuaPaiJi)) {
            return daHuaParser_zhuaPaiJi.path_b2s(bigRelativePath, type, index);
        } else if (bigRelativePath.contains(DeviceUtil.DaHua_GongChengJi)) {
            return daHuaParser_gongChengJi.path_b2s(bigRelativePath, type, index);
        } else {
            return boxParser.path_b2s(bigRelativePath, type, index);
        }
    }
}
