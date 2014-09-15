package com.krld.service.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class AccountServiceImpl extends UnicastRemoteObject implements AccountService {

    private DBManager dbManager;

    protected AccountServiceImpl() throws RemoteException {
        initDBManager();
    }

    private void initDBManager() {
        dbManager = new DBManager();
        dbManager.init();
    }

    @Override
    public Long getAmount(Integer id) throws RemoteException {
        return dbManager.getAmount(id);
    }

    @Override
    public void addAmount(Integer id, Long value) throws RemoteException {
        dbManager.addAmount(id, value);
    }
}
