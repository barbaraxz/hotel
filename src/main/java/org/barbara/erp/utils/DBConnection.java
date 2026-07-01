package org.barbara.erp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/Hotel";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "duque";

    public static Connection getConnection() {
        try{
            return DriverManager.getConnection(URL,USERNAME,PASSWORD);
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
}
