package com.hzgc.common.service.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Slf4j
public class CreateTable<T extends Table> implements CommandLineRunner {
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
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> actualTypeArguments = (Class<T>) superClass.getActualTypeArguments()[0];
        Map<String, String> sql = SqlUtil.getSql(actualTypeArguments);
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
                            log.info("Create table " + name + " successfully!!!");
                        } else {
                            log.error("Create table " + name + "  failed!!!");
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
