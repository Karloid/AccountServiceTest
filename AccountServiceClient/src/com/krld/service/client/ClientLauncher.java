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
        int rCount = 100;
        int wCount = 100;
        int[] idList = new int[]{10,20,30,40,50};
        client.runConcurrenceThreads(rCount, wCount, idList);
    }
}
