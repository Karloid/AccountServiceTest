package com.krld.service.server.managers;

import com.krld.service.server.contracts.SQLContract;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static com.krld.service.server.contracts.PropertiesContract.*;

public class DBManager {
    public static final int MILLIS_BETWEEN_RECONNECTION = 10000;
    public static final String EXCEPTION_TEXT = "Internal server exception!";
    private ComboPooledDataSource cpds;

    public void init(Properties prop) {
        cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(prop.getProperty(DB_DRIVER));

            cpds.setJdbcUrl(prop.getProperty(DB_URL));
            cpds.setUser(prop.getProperty(DB_USERNAME));
            cpds.setPassword(prop.getProperty(DB_PASSWORD));

            cpds.setMinPoolSize(Integer.valueOf(prop.getProperty(CACHE_MIN_POOL_SIZE)));
            cpds.setAcquireIncrement(Integer.valueOf(prop.getProperty(CACHE_ACQUIRE_INCREMENT)));
            cpds.setMaxPoolSize(Integer.valueOf(prop.getProperty(CACHE_MAX_POOL_SIZE)));
            System.out.println("Attempt connect to DB service...");
            while (!isConnected()) {
            //    cpds.close();
                System.out.println("DB service is offline, try reconnect...");
                Thread.sleep(MILLIS_BETWEEN_RECONNECTION);
            //    init(prop);
            }
            System.out.println("Connected to DB service...");
            createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isConnected() {
        try (Connection conn = cpds.getConnection()) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private void createTable() throws SQLException {
        Connection connection = cpds.getConnection();
        executeUpdate(connection, SQLContract.DROP_TABLE);
        executeUpdate(connection, SQLContract.CREATE_TABLE);
        connection.close();
    }

    private void executeUpdate(Connection connection, String sql) {
        try (PreparedStatement prep = connection.prepareStatement(sql)) {
            prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Long getAmount(Integer id) throws RemoteException {
        ResultSet resultSet = null;
        try (Connection conn = cpds.getConnection();
             PreparedStatement prep = conn.prepareStatement(SQLContract.SELECT_ACCOUNT_AMOUNT)) {
            prep.setInt(1, id);
            resultSet = prep.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                return 0L;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException(EXCEPTION_TEXT);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addAmount(Integer id, Long value) throws RemoteException {
        try (Connection conn = cpds.getConnection()) {
            if (tryUpdateAmount(conn, id, value)) {
                return;
            }
            if (tryInsertAmount(conn, id, value)) {
                return;
            }
            if (!tryUpdateAmount(conn, id, value)) {
                throw new RemoteException(EXCEPTION_TEXT);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException(EXCEPTION_TEXT);
        }
    }

    private boolean tryInsertAmount(Connection conn, Integer id, Long value) {
        try (PreparedStatement prep = conn.prepareStatement(SQLContract.INSERT_ACCOUNT_AMOUNT);) {
            prep.setInt(1, id);
            prep.setLong(2, value);
            prep.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean tryUpdateAmount(Connection conn, Integer id, Long value) {
        try (PreparedStatement prep = conn.prepareStatement(SQLContract.UPDATE_ACCOUNT_AMOUNT)) {
            prep.setLong(1, value);
            prep.setInt(2, id);
            return prep.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
