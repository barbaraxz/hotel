package org.barbara.erp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgres://"+"localhost:5462/";
    private static final String DATABASE = "hotel";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "300326";

    public static Connection getConnection() {
        try{
            return DriverManager.getConnection(URL+DATABASE,USERNAME,PASSWORD);
        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
}
