package com.krld.service.client;

import com.krld.service.server.AccountService;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Andrey on 9/14/2014.
 */
public class RmiClient implements Client {
    public static final String SERVICE_NAME = "AccountService";
    private static final long VALUE = 10;
    public static final int THREADS_N = 40;
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
            Long amount = service.getAmount(id);
            log("getAmount. id: " + id + "; amount:" + amount);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addAmount(int id, long value) {
        try {
            service.addAmount(id, value);
            log("addAmount. id: " + id + "; value:" + value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void log(String s) {
        System.out.println(s);
    }

    @Override
    public void runConcurrenceThreads(int rCount, int wCount, int[] idList) {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_N);
        for (int i = 0; i < rCount; i++) {
            for (int id : idList) {
                executor.execute(new ReadRunnable(id));
            }
        }
        for (int i = 0; i < rCount; i++) {
            for (int id : idList) {
                executor.execute(new WriteRunnable(id, VALUE));
            }
        }
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
