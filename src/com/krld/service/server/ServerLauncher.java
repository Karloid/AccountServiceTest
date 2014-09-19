package com.krld.service.server;

import com.krld.service.server.contracts.PropertiesContract;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.Permission;
import java.util.Properties;

public class ServerLauncher {

    private static final String SERVICE_NAME = "AccountService";

    public static void main(String[] args) {
        launchServer();
    }

    private static void launchServer() {
        initSecurityManager();
        try {
            AccountService service = new AccountServiceImpl(loadProperties());
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(SERVICE_NAME, service);
            System.out.println("Server rebind!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initSecurityManager() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager() {

                @Override
                public void checkConnect(String host, int port) {
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

    private static Properties loadProperties() {
        Properties prop = new Properties();
        String fileName = PropertiesContract.PROPERTIES_FILE_NAME;
        if (!new File(fileName).exists()) {
            throw new RuntimeException("Properties file: " + fileName + " not found!");
        }
        try {
            prop.load(new FileReader(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while reading properties file!");
        }
        return prop;
    }
}
