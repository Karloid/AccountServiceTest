package com.krld.service.client;

import com.krld.service.server.AccountService;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public class ClientLauncher {
    private static final String SERVICE_NAME = "AccountService";
    private static final String RMI_HOSTNAME = "hostname";
    private static final String RMI_PORT = "port";
    private static final String PROPERTIES_FILE_NAME = "client.properties";
    private static final String R_COUNT = "rCount";
    private static final String W_COUNT = "wCount";
    private static final String ID_LIST = "idList";
    private static final String SPLITTER = ":";

    public static void main(String[] args) {
        launchClient();
    }

    private static void launchClient() {
        System.out.println("Launch client");
        Properties prop = loadProperties();
        RmiClient client = new RmiClient();
        initClient(prop, client);
        performTest(prop, client);
    }

    private static void performTest(Properties prop, RmiClient client) {
        int rCount = Integer.valueOf(prop.getProperty(R_COUNT));
        int wCount = Integer.valueOf(prop.getProperty(W_COUNT));
        String[] idListRange = prop.getProperty(ID_LIST).split(SPLITTER);
        int idStart = Integer.valueOf(idListRange[0]);
        int idEnd = Integer.valueOf(idListRange[1]);
        int[] idList = new int[idEnd - idStart + 1];
        for (int i = idStart; i <= idEnd; i++) {
            idList[i] = i;
        }
        client.runConcurrenceThreads(rCount, wCount, idList);
    }

    private static void initClient(Properties prop, RmiClient client) {
        try {
            Registry registry = LocateRegistry.getRegistry(prop.getProperty(RMI_HOSTNAME),
                    Integer.valueOf(prop.getProperty(RMI_PORT)));

            AccountService service = (AccountService) registry.lookup(SERVICE_NAME);
            System.out.println("Obtained RMI service!");
            client.init(prop, service);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An exception occurred while launching client!");
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
