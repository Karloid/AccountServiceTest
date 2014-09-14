package com.krld.service.server;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.Permission;

public class ServerLauncher {

    public static final String SERVICE_NAME = "AccountService";

    public static void main(String[] args) {
        launchServer();
    }

    private static void launchServer() {
        initSecurityManager();
        try {
            AccountService service = new AccountServiceImpl();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(SERVICE_NAME, service);
            System.out.println("Server rebind!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initSecurityManager() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager (new RMISecurityManager() {
                public void checkConnect(String host, int port) {
                }

                public void checkConnect(String host, int port, Object context) {
                }

                @Override
                public void checkPermission(Permission perm) {
                }

                @Override
                public void checkPermission(Permission perm, Object context) {
                }
            });
         }
    }
}
