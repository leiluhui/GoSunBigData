package com.hzgc.common.collect.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class CollectUrlUtil {

    /**
     * 拼装http请求
     *
     * @param host 主机名称或者IP
     * @param port 端口号
     * @param absolutePath 请求图片绝对路径
     * @return 最终结果
     */
    public static String toHttpPath(String host, String port, String absolutePath) {
        return "http://" +
                host +
                ":" +
                port +
                "/image" +
                "?url=" +
                absolutePath;
    }

    /**
     * 将带hostname的http url转为带ip的http url
     * old:http://s105:2573/image_zip?url=/opt/ftpdata/2L04129PAU01933/2018/08/10/15/2018_08_10_15_16_32_17706_0.jpg
     * new:http://172.18.18.105:2573/image_zip?url=/opt/ftpdata/2L04129PAU01933/2018/08/10/15/2018_08_10_15_16_32_17706_0.jpg
     *
     * @param httpUrl 带hostname的http url
     * @param mapping 主机名合ip映射表 ex: [s105 -> 172.18.18.105]
     * @return 最终结果
     */
    public static String httpHostNameToIp(String httpUrl, Map<String, String> mapping) {
        if (httpUrl != null && !"".equals(httpUrl)) {
            try {
                URL url = new URL(httpUrl);
                String host = url.getHost();
                System.out.println(host);
                String ip = mapping.get(host);
                if (ip != null) {
                    return url.toString().replace("/" + host + ":", "/" + ip + ":");
                } else {
                    return httpUrl;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return httpUrl;
    }
}
