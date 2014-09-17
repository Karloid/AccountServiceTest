package com.krld.service.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class AccountServiceImpl extends UnicastRemoteObject implements AccountService {

    private DBManager dbManager;
    private StatsManager statsManager;
    private CacheManager cacheManager;

    protected AccountServiceImpl() throws RemoteException {
        initDBManager();
        initStatsManager();
        initCacheManager();
    }

    private void initCacheManager() {
        cacheManager = new CacheManager();
        cacheManager.init();
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
        Long amount = null;
        amount = cacheManager.getAmount(id);
        if (amount == null) {
            amount = dbManager.getAmount(id);
        }
        return amount;
    }

    @Override
    public void addAmount(Integer id, Long value) throws RemoteException {
        statsManager.incAddAmountCalls();
        dbManager.addAmount(id, value);
        cacheManager.addAmount(id, value);
    }
}
