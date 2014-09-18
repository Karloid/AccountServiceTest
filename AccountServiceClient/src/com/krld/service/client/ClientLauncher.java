package com.krld.service.client;

/**
 * Created by Andrey on 9/14/2014.
 */
public class ClientLauncher {
    public static void main(String[] args) {
        launchClient();
    }

    private static void launchClient() {
        System.out.println("Launch client");
        Client client = new RmiClient();
        client.init();
        int rCount;
        int wCount;
        rCount = 100;
        wCount = 10;
        int idListSize = 5000;
        int[] idList = new int[idListSize];
        for (int i = 0; i< idListSize; i++) {
            idList[i] = i /*+ (int)(Math.random() * 20)*/;
        }
        client.runConcurrenceThreads(rCount, wCount, idList);
    }
}
