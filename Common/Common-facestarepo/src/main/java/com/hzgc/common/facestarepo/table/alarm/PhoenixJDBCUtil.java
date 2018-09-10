package com.hzgc.common.facestarepo.table.alarm;

import java.sql.*;

class PhoenixJDBCUtil {

    private static Connection conn;

    private PhoenixJDBCUtil() {
    }

    static Connection getPhoenixJdbcConn(String jdbcUrl) {
        if (conn == null) {
            initConnection(jdbcUrl);
        }
        return conn;
    }

    private static void initConnection(String jdbCUrl) {
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            conn = DriverManager.getConnection(jdbCUrl);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}