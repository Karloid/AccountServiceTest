package com.krld.service.server;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Andrey on 9/16/2014.
 */
public class DBManager {
    public static final String KEY_DB_DRIVER = "dbDriver";
    private static final java.lang.String KEY_DB_PATH = "dbPath";
    public static final String KEY_DB_USERNAME = "username";
    private static final java.lang.String KEY_DB_PASSWORD = "password";
    private Properties prop;
    private ComboPooledDataSource cpds;

    public void init() {
        loadProperties();
        cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(prop.getProperty(KEY_DB_DRIVER)); //loads the jdbc driver

            cpds.setJdbcUrl(prop.getProperty(KEY_DB_PATH));
            cpds.setUser(prop.getProperty(KEY_DB_USERNAME));
            cpds.setPassword(prop.getProperty(KEY_DB_PASSWORD));

            cpds.setMinPoolSize(5);
            cpds.setAcquireIncrement(5);
            cpds.setMaxPoolSize(20);
            createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadProperties() {
        prop = new Properties();
        String fileName = "config.properties";
        if (!new File(fileName).exists()) {
            throw new RuntimeException("file: " + fileName + " not found!");
        }
        try {
            prop.load(new FileReader(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTable() throws SQLException {
        Connection connection = cpds.getConnection();
        executeUpdate(connection, SQLContract.DROP_TABLE);
        executeUpdate(connection, SQLContract.CREATE_TABLE);
    }

    private void executeUpdate(Connection connection, String sql) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Long getAmount(Integer id) {
        //TODO implement
        return null;
    }

    public void addAmount(Integer id, Long value) {
        //TODO implement
    }
}
