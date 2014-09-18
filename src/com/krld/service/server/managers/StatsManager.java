package com.krld.service.server.managers;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import static com.krld.service.server.contracts.PropertiesContract.*;

public class StatsManager {
    private AtomicInteger addAmountCalls;
    private AtomicInteger getAmountCalls;
    private AtomicInteger totalCalls;
    private int updateFreq;
    private Thread runner;
    private int addAmountCallsLastPeriod;
    private int getAmountCallsLastPeriod;
    private Properties prop;

    public void init(Properties prop) {
        this.prop = prop;
        updateFreq = Integer.valueOf(prop.getProperty(STATS_UPDATE_FREQUENCY));
        addAmountCalls = new AtomicInteger(0);
        getAmountCalls = new AtomicInteger(0);
        totalCalls = new AtomicInteger(0);
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
        runner = new Thread(this::updateLoop);
        runner.start();
    }

    private void updateLoop() {
        try {
            while (true) {
                Thread.sleep(updateFreq);
                addAmountCallsLastPeriod = addAmountCalls.getAndSet(0);
                getAmountCallsLastPeriod = getAmountCalls.getAndSet(0);
                log("addAmountCalls: " + addAmountCallsLastPeriod +
                        "; getAmountCalls: " + getAmountCallsLastPeriod + "; total: " + totalCalls.get());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void log(String s) {
        System.out.println(s);
    }

    public void incGetAmountCalls() {
        getAmountCalls.incrementAndGet();
        totalCalls.incrementAndGet();
    }

    public void incAddAmountCalls() {
        addAmountCalls.incrementAndGet();
        totalCalls.incrementAndGet();
    }

    public void reset() {
        init(prop);
    }
}
