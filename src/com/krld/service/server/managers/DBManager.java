package com.krld.service.server.managers;

import com.krld.service.server.contracts.SQLContract;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static com.krld.service.server.contracts.PropertiesContract.*;

public class DBManager {
    private ComboPooledDataSource cpds;

    public void init(Properties prop) {
        cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(prop.getProperty(DB_DRIVER)); //loads the jdbc driver

            cpds.setJdbcUrl(prop.getProperty(DB_URL));
            cpds.setUser(prop.getProperty(DB_USERNAME));
            cpds.setPassword(prop.getProperty(DB_PASSWORD));

            cpds.setMinPoolSize(Integer.valueOf(prop.getProperty(CACHE_MIN_POOL_SIZE)));
            cpds.setAcquireIncrement(Integer.valueOf(prop.getProperty(CACHE_ACQUIRE_INCREMENT)));
            cpds.setMaxPoolSize(Integer.valueOf(prop.getProperty(CACHE_MAX_POOL_SIZE)));
            createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() throws SQLException {
        Connection connection = cpds.getConnection();
        executeUpdate(connection, SQLContract.DROP_TABLE);
        executeUpdate(connection, SQLContract.CREATE_TABLE);
    }

    private void executeUpdate(Connection connection, String sql) {
        PreparedStatement prep = null;
        try {
            prep = connection.prepareStatement(sql);
            prep.executeUpdate();
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
    }

    public Long getAmount(Integer id) {
        Connection conn = null;
        PreparedStatement prep = null;
        ResultSet resultSet = null;
        try {
            conn = cpds.getConnection();
            prep = conn.prepareStatement(SQLContract.SELECT_ACCOUNT_AMOUNT);
            prep.setInt(1, id);
            resultSet = prep.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
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
            result = prep.executeUpdate() == 1;
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
