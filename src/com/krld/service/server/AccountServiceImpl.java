package com.krld.service.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class AccountServiceImpl extends UnicastRemoteObject implements AccountService {

    private DBManager dbManager;
    private StatsManager statsManager;

    protected AccountServiceImpl() throws RemoteException {
        initDBManager();
        initStatsManager();
    }

    private void initStatsManager() {
        statsManager = new StatsManager();
        statsManager.init();
    }

    private void initDBManager() {
        dbManager = new DBManager();
        dbManager.init();
    }

    @Override
    public Long getAmount(Integer id) throws RemoteException {
        statsManager.incGetAmountCalls();
        return dbManager.getAmount(id);
    }

    @Override
    public void addAmount(Integer id, Long value) throws RemoteException {
        statsManager.incAddAmountCalls();
        dbManager.addAmount(id, value);
    }
}
