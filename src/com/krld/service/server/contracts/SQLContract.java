package com.krld.service.server.contracts;

public class SQLContract {
    private static final String TABLE_NAME = "accounts";
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
    public static final String SELECT_ACCOUNT_AMOUNT = "SELECT amount FROM " + TABLE_NAME + " WHERE id = ?;";
    public static final String UPDATE_ACCOUNT_AMOUNT = "UPDATE " + TABLE_NAME + " SET amount = amount + ? " +
            "WHERE id = ?";
    public static final java.lang.String INSERT_ACCOUNT_AMOUNT = "INSERT INTO " + TABLE_NAME + " (id, amount) VALUES (?, ?);";
}
