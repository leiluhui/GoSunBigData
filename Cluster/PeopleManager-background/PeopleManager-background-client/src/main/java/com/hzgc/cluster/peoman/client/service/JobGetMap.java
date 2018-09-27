package com.hzgc.cluster.peoman.client.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
@Component
public class JobGetMap {
    @Autowired
    @Value("${database.people.url}")
    private String url;
    @Autowired
    @Value("${mysql.driver.class}")
    private String driver;
    @Autowired
    @Value("${database.user}")
    private String user;
    @Autowired
    @Value("${database.password}")
    private String password;

    public String getCount(){
        //连接数据库获取照片库中的总条数
        Connection connection;
        String count = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,user,password);
            if (!connection.isClosed()){
                log.info("Succeeded connectiong to the Database!");
                Statement statement = connection.createStatement();
                String sql = "select count(*) as count from t_picture,t_people, t_flag where t_picture.peopleid=t_people.id and t_picture.peopleid=t_flag.peopleid;";
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    count = resultSet.getString("count");
                    log.info("Count is : " + count);
                }
                resultSet.close();
                connection.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
}
