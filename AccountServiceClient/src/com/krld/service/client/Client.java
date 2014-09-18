package com.krld.service.client;

import java.util.Properties;

interface Client {
    void init(Properties prop);

    void runConcurrenceThreads(int rCount, int wCount, int[] idList);
}
