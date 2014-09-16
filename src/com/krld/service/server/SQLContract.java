package com.krld.service.server;

/**
 * Created by Andrey on 9/16/2014.
 */
public class SQLContract {
    public static final String TABLE_NAME = "accounts";
    public static final String DROP_TABLE = "DROP TABLE " + TABLE_NAME + ";";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" +
            "id integer NOT NULL," +
            "amount bigint," +
            "CONSTRAINT \"" + TABLE_NAME.toUpperCase() + "_PK\" PRIMARY KEY (id)" +
            ")" +
            "WITH (" +
            "OIDS=FALSE" +
            ");";
    public static final String SELECT_ACCOUNT_AMOUNT = "SELECT AMOUNT FROM " + TABLE_NAME + ";";
    public static final String UPDATE_ACCOUNT_AMOUNT = "UPDATE " + TABLE_NAME + " SET AMOUNT = AMOUNT + ? " +
            "WHERE ID = ?";
    public static final java.lang.String INSERT_ACCOUNT_AMOUNT = "INSERT INTO " + TABLE_NAME + " (id, amount) VALUES (?, ?);";
}
