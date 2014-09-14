package com.krld.service.client;

import com.krld.service.server.AccountService;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.security.Permission;

/**
 * Created by Andrey on 9/14/2014.
 */
public class RmiClient implements Client {
    public static final String SERVICE_NAME = "AccountService";
    private AccountService service;

    @Override
    public void init() {
        try {
            service = (AccountService) Naming.lookup(SERVICE_NAME);
            System.out.println("Get service!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAmount(int id) {
        try {
            System.out.println(service.getAmount(id) + "");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
