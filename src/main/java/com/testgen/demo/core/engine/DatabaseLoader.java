package com.testgen.demo.core.engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseLoader {

    private static final String URL = "jdbc:mariadb://localhost:3306/CVUT";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnector() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
