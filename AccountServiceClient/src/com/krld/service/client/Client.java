package com.krld.service.client;

/**
 * Created by Andrey on 9/14/2014.
 */
public interface Client {
    void init();

    void getAmount(int id);

    void addAmount(int id, long value);

    void runConcurrenceThreads(int rCount, int wCount, int[] idList);
}
