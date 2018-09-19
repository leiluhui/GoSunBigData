package com.hzgc.service.collect.service;

import com.hzgc.common.collect.facedis.FtpRegisterClient;
import com.hzgc.common.collect.facedis.FtpRegisterInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;

@Component
@Slf4j
public class FtpPersistence implements CommandLineRunner {
    @Autowired
    FtpRegisterClient ftpRegisterClient;
    @Autowired
    FtpPersistence ftpPersistence;
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

    @Override
    public void run(String... strings) {
        List <FtpRegisterInfo> ftpRegisterInfoList = ftpRegisterClient.getFtpRegisterInfoList();
        for (FtpRegisterInfo ftpRegisterInfo : ftpRegisterInfoList) {
            String ftpIPAddress = ftpRegisterInfo.getFtpIPAddress();
            //查询数据库，是否存在，存在count + 1,不存在count = 1
            ftpPersistence.queryDataBase(ftpIPAddress);
        }
        ftpPersistence.closeConnection(statement, connection);
    }

    //查询数据库
    public void queryDataBase(String ftpIPAddress) {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            sql = "select ip from t_ftp where ip = " + ftpIPAddress;
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                //count + 1
                int count = resultSet.getInt("count");
                String ip = resultSet.getString("ip");
                count++;
                log.info("FTP address count + 1,ftpAddress is " + ip);
                ftpPersistence.updateDataBase(count, ip);
            } else {
                //count置1
                log.info("FTP address is new register,ftpAddress is " + ftpIPAddress);
                ftpPersistence.insertDataBase(ftpIPAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //新增count置1
    public void insertDataBase(String ftpIPAddress) {
        try {
            sql = "insert into t_ftp(ip,count) values(" + ftpIPAddress + ",1) ";
            int i = statement.executeUpdate(sql);
            if (i > 0) {
                log.info("Insert ftpAddress is successful,new address is " + ftpIPAddress);
            } else {
                log.info("Insert ftpAddress is failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //修改count + 1
    public void updateDataBase(int count, String ftpIPAddress) {
        try {
            sql = "update t_ftp set count =" + count + "where ip = " + ftpIPAddress;
            int i = statement.executeUpdate(sql);
            if (i > 0) {
                log.info("Update ftpAddress is successful,this ftpAddress is " + ftpIPAddress);
            } else {
                log.info("Update ftpAddress is failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(Statement statement, Connection connection) {
        try {
            if (null != statement) {
                statement.close();
            }
            if (null != connection) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
