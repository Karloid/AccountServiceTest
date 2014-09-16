package com.krld.service.server;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public static final String CONFIG_FILE_NAME = "config.properties";
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
        String fileName = CONFIG_FILE_NAME;
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
        Connection conn = null;
        PreparedStatement prep = null;
        ResultSet resultSet = null;
        try {
            conn = cpds.getConnection();
            prep = conn.prepareStatement(SQLContract.SELECT_ACCOUNT_AMOUNT);
            resultSet = prep.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(0);
            } else {
                return 0L;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (prep != null) {
                    prep.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void addAmount(Integer id, Long value) {
        Connection conn = null;
        try {
            conn = cpds.getConnection();
            if (tryUpdateAmount(conn, id, value)) {
                return;
            }
            if (tryInsertAmount(conn, id, value)) {
                return;
            }
            if (!tryUpdateAmount(conn, id, value)) {
                System.out.println("Update after trying insert, failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean tryInsertAmount(Connection conn, Integer id, Long value) {
        PreparedStatement prep = null;
        try {
            prep = conn.prepareStatement(SQLContract.INSERT_ACCOUNT_AMOUNT);
            prep.setInt(1, id);
            prep.setLong(2, value);
            prep.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (prep != null) {
                    prep.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean tryUpdateAmount(Connection conn, Integer id, Long value) {
        PreparedStatement prep = null;
        try {
            prep = conn.prepareStatement(SQLContract.UPDATE_ACCOUNT_AMOUNT);
            prep.setLong(1, value);
            prep.setInt(2, id);
            boolean result;
            if (prep.executeUpdate() == 1) {
                result = true;
            } else {
                result = false;
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (prep != null) {
                    prep.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
