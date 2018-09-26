package com.hzgc.service.collect.service;

import com.hzgc.common.collect.facedis.FtpRegisterClient;
import com.hzgc.common.collect.facedis.FtpRegisterInfo;
import com.hzgc.common.collect.facesub.FtpSubscribeClient;
import com.hzgc.service.collect.util.FtpUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FtpService implements Serializable {
    @Autowired
    private FtpRegisterClient register;

    @Autowired
    private FtpSubscribeClient subscribe;

    @Autowired
    private FtpService ftpService;

    @Value("${mysql.user}")
    private String user;
    @Value("${mysql.password}")
    private String password;
    @Value("${mysql.url}")
    private String url;
    @Value("${mysql.driver}")
    private String driver;
    private Statement statement = null;
    private Connection connection = null;
    private String sql;

    public byte[] getPhotoByFtpUrl(String ftpUrl) {
        // FTP匿名账号Anonymous和密码
        return FtpUtils.downloadftpFile2Bytes(ftpUrl, "anonymous", null);
    }

    /**
     * 获取Ftp相关配置参数
     *
     * @return ftp相关配置参数
     */
    public Map <String, String> getProperties(String ftpType) {
        Map <String, String> map = new HashMap <>();
        List <FtpRegisterInfo> list = null;
        if ("face".equals(ftpType)) {
            list = register.getFaceFtpRegisterInfoList();
        }
        if ("person".equals(ftpType)) {
            list = register.getPersonFtpRegisterInfoList();
        }
        if ("car".equals(ftpType)) {
            list = register.getCarFtpRegisterInfoList();
        }
        //分配绑定次数最小的返回
        if (list != null && list.size() > 0) {
            List <String> ftps = ftpService.queryIpByASC();
            for (FtpRegisterInfo registerInfo : list) {
                String ftpIPAddress = registerInfo.getFtpIPAddress();
                if (ftps.size() > 0) {
                    for (String ftp : ftps) {
                        if (ftpIPAddress.equals(ftp)) {
                            map.put("ftpIp", registerInfo.getFtpIPAddress());
                            map.put("ftpPort", registerInfo.getFtpPort());
                            map.put("proxyIP", registerInfo.getProxyIP());
                            map.put("proxyPort", registerInfo.getProxyPort());
                            map.put("username", registerInfo.getFtpAccountName());
                            map.put("password", registerInfo.getFtpPassword());
                            map.put("pathRule", registerInfo.getPathRule());
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * 通过主机名获取FTP的IP地址
     *
     * @param hostname 主机名
     * @return IP地址
     */
    public String getIPAddress(String hostname) {
        if (!StringUtils.isBlank(hostname)) {
            return register.getFtpIpMapping().get(hostname);
        }
        return null;
    }

    public boolean openFtpSubscription(String sessionId, List <String> ipcIdList) {
        if (!sessionId.equals("") && !ipcIdList.isEmpty()) {
            subscribe.updateSessionPath(sessionId, ipcIdList);
            return true;
        }
        return false;
    }

    public boolean closeFtpSubscription(String sessionId) {
        if (!sessionId.equals("")) {
            subscribe.deleteSessionPath(sessionId);
            return true;
        }
        return false;
    }

    public List <String> queryIpByASC() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            sql = "select ip from t_ftp order by count ASC";
            ResultSet resultSet = statement.executeQuery(sql);
            ArrayList <String> ftpIps = new ArrayList <>();
            while (resultSet.next()) {
                String ip = resultSet.getString("ip");
                ftpIps.add(ip);
            }
            return ftpIps;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != statement) {
                    statement.close();
                }
                if (null != connection) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList <>();
    }
}

