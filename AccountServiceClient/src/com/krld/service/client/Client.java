package com.krld.service.client;

import java.util.Properties;

/**
 * Created by Andrey on 9/14/2014.
 */
public interface Client {
    void init(Properties prop);

    void getAmount(int id);

    void addAmount(int id, long value);

    void runConcurrenceThreads(int rCount, int wCount, int[] idList);
}
