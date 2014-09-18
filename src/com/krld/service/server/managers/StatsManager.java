package com.krld.service.server.managers;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Andrey on 9/16/2014.
 */
public class StatsManager {
    private AtomicInteger addAmountCalls;
    private AtomicInteger getAmountCalls;
    private int updateFreq;
    private Thread runner;
    private int addAmountCallsLastPeriod;
    private int getAmountCallsLastPeriod;

    public void init(Properties prop) {
        updateFreq = 10000;
        addAmountCalls = new AtomicInteger(0);
        getAmountCalls = new AtomicInteger(0);
        startRunner();
    }

    private void startRunner() {
        if (runner != null) {
            runner.interrupt();
            try {
                runner.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        runner = new Thread(() -> updateLoop());
        runner.start();
    }

    private void updateLoop() {
        try {
            while (true) {
                Thread.sleep(updateFreq);
                addAmountCallsLastPeriod = addAmountCalls.getAndSet(0);
                getAmountCallsLastPeriod = getAmountCalls.getAndSet(0);
                long total = getAmountCallsLastPeriod + addAmountCallsLastPeriod;
                float seconds = updateFreq / 1000f;
                System.out.println("addAmount: " + addAmountCallsLastPeriod +
                        "; getAmount: " + getAmountCallsLastPeriod + " total: " + total
                        + "; average time: " + Math.floor(updateFreq / (total +1))
                        + "ms; period: " + seconds + " seconds");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void incGetAmountCalls() {
        getAmountCalls.incrementAndGet();
    }

    public void incAddAmountCalls() {
        addAmountCalls.incrementAndGet();
    }
}
