package com.project.artconnect.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Utility class to manage JDBC connections.
 * TODO: Students must implementation the getConnection logic.
 */
import com.project.artconnect.config.DatabaseConfig;
import java.sql.DriverManager;

public class ConnectionManager {

    /**
     * Provides a connection to the MySQL database.
     * 
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        // TODO: Students should implement this using DatabaseConfig properties
        // return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER,
        // DatabaseConfig.PASSWORD);
        return DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USER,
                DatabaseConfig.PASSWORD
        );

    }
}
