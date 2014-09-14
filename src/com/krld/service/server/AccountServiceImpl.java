package com.krld.service.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Andrey on 9/14/2014.
 */
public class AccountServiceImpl extends UnicastRemoteObject implements AccountService {
    protected AccountServiceImpl() throws RemoteException {
    }

    @Override
    public Long getAmount(Integer id) throws RemoteException {
        System.out.println("Get value: " + id);
        return ((Double)(Math.random() * 100)).longValue();
    }

    @Override
    public void addAmount(Integer id, Long value) throws RemoteException {
        System.out.println("Add amount id: " + id + " value: " + value);
    }
}
