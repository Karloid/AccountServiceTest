package com.krld.service.server;

import com.krld.service.server.contracts.PropertiesContract;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.Permission;
import java.util.Properties;

import static com.krld.service.server.contracts.PropertiesContract.*;

public class ServerLauncher {



    public static void main(String[] args) {
        launchServer();
    }

    private static void launchServer() {
        initSecurityManager();
        try {
            Properties prop = loadProperties();
            AccountService service = new AccountServiceImpl(prop);
            Registry registry = LocateRegistry.getRegistry(prop.getProperty(RMI_HOSTNAME),
                    Integer.valueOf(prop.getProperty(PropertiesContract.RMI_PORT)));
            registry.rebind(SERVICE_NAME, service);
            System.out.println("Server rebind!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot connect to rmiregistry!");
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
        String fileName = PROPERTIES_FILE_NAME;
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
