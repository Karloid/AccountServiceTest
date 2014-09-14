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
        while (true) {
            client.getAmount(10);
        }
    }
}
