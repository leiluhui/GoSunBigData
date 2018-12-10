package com.hzgc.cluster.dispatch.service;

import com.hzgc.common.util.basic.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Component
@Slf4j
public class CreateTable implements CommandLineRunner {

    @Autowired
    @Value(value = "${spring.datasource.url}")
    private String jdbcUrl;

    @Autowired
    @Value(value = "${spring.datasource.username}")
    private String username;

    @Autowired
    @Value(value = "${spring.datasource.password}")
    private String password;

    @Autowired
    @Value(value = "${spring.datasource.driver-class-name}")
    private String driver;

    @Override
    public void run(String... strings) {

        Map<String, String> sql = SqlUtil.getSql(Dispatch_background_table.class);
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            if (sql != null) {

                for (String name : sql.keySet()) {
                    Statement statement = null;
                    try {
                        if (connection != null) {
                            statement = connection.createStatement();
                        }
                        int result = 0;
                        if (statement != null) {
                            result = statement.executeUpdate(sql.get(name));
                        }
                        if (result != -1) {
                            System.out.println("Create table " + name + " successfully!!!");
                        } else {
                            System.out.println("Create table " + name + "  failed!!!");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (statement != null) {
                                statement.close();
                            }
                        } catch (SQLException e) {
                            log.error(e.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }
}
