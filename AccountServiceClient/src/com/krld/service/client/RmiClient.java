package com.krld.service.client;

import com.krld.service.server.AccountService;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RmiClient {
    private static final String SERVICE_NAME = "AccountService";
    private static final String W_VALUE = "wValue";
    private static final String N_THREADS = "nThreads";
    private long value;
    private int nThreads;
    private AccountService service;

    public void init(Properties prop, AccountService service) {
        try {
            value = Long.valueOf(prop.getProperty(W_VALUE));
            nThreads = Integer.valueOf(prop.getProperty(N_THREADS));
            this.service = service;

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void getAmount(int id) {
        try {
            Long amount = service.getAmount(id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addAmount(int id, long value) {
        try {
            service.addAmount(id, value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void runConcurrenceThreads(int rCount, int wCount, int[] idList) {
        System.out.println("runConcurrenceThreads rCount: " + rCount + "; wCount: "
                + wCount + "; idList.length(): " + idList.length);
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < rCount; i++) {
            for (int id : idList) {
                executor.execute(new ReadRunnable(id));
            }
        }
        for (int i = 0; i < wCount; i++) {
            for (int id : idList) {
                executor.execute(new WriteRunnable(id, value));
            }
        }
        executor.shutdown();
    }


    private class ReadRunnable implements Runnable {
        private final int id;

        public ReadRunnable(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            getAmount(id);
        }
    }

    private class WriteRunnable implements Runnable {
        private final int id;
        private final long value;

        public WriteRunnable(int id, long value) {
            this.id = id;
            this.value = value;
        }

        @Override
        public void run() {
            addAmount(id, value);
        }
    }
}
