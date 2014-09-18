package com.krld.service.server;

import com.krld.service.server.contracts.PropertiesContract;
import com.krld.service.server.managers.CacheManager;
import com.krld.service.server.managers.DBManager;
import com.krld.service.server.managers.StatsManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;


public class AccountServiceImpl extends UnicastRemoteObject implements AccountService {

    private DBManager dbManager;
    private StatsManager statsManager;
    private CacheManager cacheManager;
    private Properties prop;

    protected AccountServiceImpl() throws RemoteException {
        loadProperties();
        initDBManager();
        initStatsManager();
        initCacheManager();
    }

    private void loadProperties() {
            prop = new Properties();
            String fileName = PropertiesContract.PROPERTIES_FILE_NAME;
            if (!new File(fileName).exists()) {
                throw new RuntimeException("Config file: " + fileName + " not found!");
            }
            try {
                prop.load(new FileReader(fileName));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error while reading config file!");
            }
    }

    private void initCacheManager() {
        cacheManager = new CacheManager();
        cacheManager.init(prop);
    }

    private void initStatsManager() {
        statsManager = new StatsManager();
        statsManager.init(prop);
    }

    private void initDBManager() {
        dbManager = new DBManager();
        dbManager.init(prop);
    }

    @Override
    public Long getAmount(Integer id) throws RemoteException {
        statsManager.incGetAmountCalls();
        Long amount = cacheManager.getAmount(id);
        if (amount == null) {
            amount = dbManager.getAmount(id);
            cacheManager.addAmount(id, amount);
        }
        return amount;
    }

    @Override
    public void addAmount(Integer id, Long value) throws RemoteException {
        statsManager.incAddAmountCalls();
        if (value == 0L) {
            return;
        }
        dbManager.addAmount(id, value);
    }

    @Override
    public void resetStats() throws RemoteException {
        statsManager.reset();
    }
}
