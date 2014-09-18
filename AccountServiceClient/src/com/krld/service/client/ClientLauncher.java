package com.krld.service.client;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ClientLauncher {
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
        Client client = new RmiClient();
        client.init(prop);
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

    private static Properties loadProperties() {
        Properties prop = new Properties();
        String fileName = PROPERTIES_FILE_NAME;
        if (!new File(fileName).exists()) {
            throw new RuntimeException("Config file: " + fileName + " not found!");
        }
        try {
            prop.load(new FileReader(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while reading config file!");
        }
        return prop;
    }
}
